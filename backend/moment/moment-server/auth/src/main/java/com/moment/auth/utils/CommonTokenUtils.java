package com.moment.auth.utils;

import com.auth0.jwk.JwkException;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component
public class CommonTokenUtils extends AbstractTokenUtils{
    @Override
    public boolean validate(String token) throws ParseException, JwkException {
        return false;
    }

    @Override
    public OauthInfo getOauthInfo(String token) throws ParseException {
        return null;
    }

}
