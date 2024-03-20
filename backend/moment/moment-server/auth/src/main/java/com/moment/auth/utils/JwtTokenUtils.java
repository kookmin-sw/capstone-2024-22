package com.moment.auth.utils;

import com.auth0.jwk.JwkException;
import com.moment.auth.domain.Role;
import com.moment.auth.dto.response.TokenResponseDTO;
import io.jsonwebtoken.*;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtils extends AbstractTokenUtils {
    private final String SECRET_KEY = "secretkey";

    public TokenResponseDTO.GetToken generateToken(String provider, String iss, Long userId, Role role){
        String accessToken = generateAccessToken(provider, iss, userId, role);
        String refreshToken = generateRefreshToken();
        return TokenResponseDTO.GetToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
    public String generateAccessToken(String sub, String iss, Long userId, Role role){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParams(createHeaders())
                .setClaims(createClaims(sub, iss, userId, role))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(12000).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public String generateRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(60).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    private Claims getClaims(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            // 토큰 유효성 확인
        } catch (SecurityException e) {
            System.out.println("e = " + e);
            System.out.println("Invalid JWT Signature.");
        } catch (MalformedJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Expired JWT token.");
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            System.out.println("e = " + e);
            System.out.println("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public Claims createClaims(String sub, String iss, Long userId, Role role){
        Claims claims = Jwts.claims();
        claims.put("sub", sub);
        claims.put("iss", iss);
        claims.put("userId", userId);
        claims.put("role", role);
        return claims;
    }

    public Map<String, Object> createHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "RS256");
        return header;
    }

    @Override
    public boolean validate(String token) throws ParseException, JwkException, ExpiredJwtException {
        return getClaims(token) != null;
    }

    @Override
    public OauthInfo getOauthInfo(String token) throws ParseException {
        return OauthInfo.builder()
                .sub(getSubject(token))
                .iss(getIssuer(token))
                .build();
    }

}
