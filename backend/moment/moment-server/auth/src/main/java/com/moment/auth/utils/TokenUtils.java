package com.moment.auth.utils;

import com.auth0.jwk.JwkException;
import org.json.simple.parser.ParseException;

public interface TokenUtils {
    String getHeader(String token);
    String getPayload(String token);
    String getJsonHeader(String token);
    String getJsonPayload(String token);
    String getClaim(String token, String key, String scope) throws ParseException;
    boolean validate(String token) throws ParseException, JwkException;
    OauthInfo getOauthInfo(String token) throws ParseException;
}