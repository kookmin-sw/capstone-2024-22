package com.moment.auth.service;

import com.moment.auth.client.CoreClient;
import com.moment.auth.domain.user.User;
import com.moment.auth.domain.user.UserRepository;
import com.moment.auth.dto.request.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CoreClient coreClient;

    public void save(User user) {
        userRepository.save(user);
    }
    public void registerUserToCoreServer(User user) {
        try{
            coreClient.registerUser(UserRequestDTO.registerUser
                    .builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .notification(false)
                    .dataUsage(false)
                    .firebaseToken("")
                    .build()
            );
        }catch (Exception e){
            throw new RuntimeException("Core 서버에 유저 등록에 실패했습니다.");
        }

    }
}
