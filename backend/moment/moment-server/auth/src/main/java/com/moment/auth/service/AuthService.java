package com.moment.auth.service;

import com.moment.auth.domain.Role;
import com.moment.auth.domain.user.User;
import com.moment.auth.domain.user.UserRepository;
import com.moment.auth.dto.request.AuthRequest;
import com.moment.auth.dto.response.TokenResponseDTO;
import com.moment.auth.exception.AlreadyRegisteredEmailException;
import com.moment.auth.utils.JwtTokenUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final String iss = "Moment";
    private final String provider = "Moment";
    private final MailService mailService;
    private final UserService userService;
    private final EntityManager em;

    public TokenResponseDTO.GetToken login(AuthRequest.Login login) {
        User user = userRepository.findByEmail(login.getEmail()).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 정보"));
        if(!passwordEncoder.matches(login.getPassword(), user.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        return jwtTokenUtils.generateToken(provider, iss, user.getId(), user.getRole());
    }

    @Transactional
    public TokenResponseDTO.GetTempToken sendCode(AuthRequest.SendCode sendCode) {
        log.info("sendCode.isSignUp : {}", sendCode.getIsSignUp());
        if (sendCode.getIsSignUp().equals("true")) {
            if(userRepository.existsByEmail(sendCode.getEmail())){
                User user = userRepository.findByEmail(sendCode.getEmail()).orElseThrow(() -> new AlreadyRegisteredEmailException("미 가입된 이메일"));
                if (user.getRole().equals(Role.ROLE_AUTH_USER)){
                    throw new AlreadyRegisteredEmailException("이미 가입된 이메일");
                }
                String randomCode = RandomStringUtils.randomAlphanumeric(6);
                String email = sendCode.getEmail();
                String code = mailService.sendMail(email, "Moment 인증코드", "인증코드 : " + randomCode);
                if (Objects.equals(code, "success")) {
                    user.setVerificationCode(randomCode);
                    userService.save(user);
                    String accessToken = jwtTokenUtils.generateAccessToken(provider, iss, user.getId(), Role.ROLE_TEMP_USER);
                    return TokenResponseDTO.GetTempToken.builder()
                            .grantType("Bearer")
                            .accessToken(accessToken)
                            .role(Role.ROLE_TEMP_USER)
                            .userId(user.getId())
                            .build();
                }
            }else{
                String randomCode = RandomStringUtils.randomAlphanumeric(6);
                String randomPassword = RandomStringUtils.randomAlphanumeric(10);
                String email = sendCode.getEmail();
                String code = mailService.sendMail(email, "Moment 인증코드", "인증코드 : " + randomCode);
                if (Objects.equals(code, "success")) {
                    User user = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode(randomPassword))
                            .role(Role.ROLE_TEMP_USER)
                            .verificationCode(randomCode)
                            .build();
                    userService.save(user);
                    String accessToken = jwtTokenUtils.generateAccessToken(provider, iss, user.getId(), Role.ROLE_TEMP_USER);
                    return TokenResponseDTO.GetTempToken.builder()
                            .grantType("Bearer")
                            .accessToken(accessToken)
                            .role(Role.ROLE_TEMP_USER)
                            .userId(user.getId())
                            .build();
                }
            }
        }
        else {
            User user = userRepository.findByEmail(sendCode.getEmail()).orElseThrow(() -> new AlreadyRegisteredEmailException("미 가입된 이메일"));
            String randomCode = RandomStringUtils.randomAlphanumeric(6);
            String code = mailService.sendMail(sendCode.getEmail(), "Moment 인증코드", "인증코드 : " + randomCode);
            if (Objects.equals(code, "success")) {
                user.setVerificationCode(randomCode);
                userService.save(user);
                String accessToken = jwtTokenUtils.generateAccessToken(provider, iss, user.getId(), user.getRole());
                return TokenResponseDTO.GetTempToken.builder()
                        .grantType("Bearer")
                        .accessToken(accessToken)
                        .role(user.getRole())
                        .userId(user.getId())
                        .build();
            }
            else throw new IllegalArgumentException("인증코드 전송 실패");
        }
        return null;
    }


    public TokenResponseDTO.GetToken verifyCode(AuthRequest.VerifyCode verifyCode, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 유저 정보"));
        if(!Objects.equals(user.getVerificationCode(), verifyCode.getCode())) throw new IllegalArgumentException("잘못된 인증코드");
        if (user.getRole() == Role.ROLE_TEMP_USER) {
            userService.registerUserToCoreServer(user);
            user.setRole(Role.ROLE_AUTH_USER);
        }
        userService.save(user);
        return jwtTokenUtils.generateToken(provider, iss, user.getId(), user.getRole());
    }

    public void changePassword(AuthRequest.ChangePassword changePassword, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 유저 정보"));
        if(!Objects.equals(user.getVerificationCode(), changePassword.getCode())) throw new IllegalArgumentException("잘못된 인증코드");
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userService.save(user);
    }
}
