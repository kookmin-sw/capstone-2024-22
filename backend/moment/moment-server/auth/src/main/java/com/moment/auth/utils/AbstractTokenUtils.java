package com.moment.auth.utils;



import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.lang.Nullable;

import java.util.Base64;

public abstract class AbstractTokenUtils implements TokenUtils {

    private final String JWT_SEPARATOR = "\\.";
    public final String SCOPE_HEADER = "header";
    public final String SCOPE_PAYLOAD = "payload";

    public String getHeader(String token) {
        String[] jwt = token.split(JWT_SEPARATOR);
        return jwt[0];
    }

    public String getPayload(String token) {
        String[] jwt = token.split(JWT_SEPARATOR);
        return jwt[1];
    }

    public String getJsonHeader(String token) {
        String header = getHeader(token);

        byte[] decodedBytes = Base64.getDecoder().decode(header);
        return new String(decodedBytes);
    }

    public String getJsonPayload(String token) {
        String payload = getPayload(token);

        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        return new String(decodedBytes);
    }

    public String getClaim(String token, String key, @Nullable String scope) throws ParseException {
        String payload = SCOPE_HEADER.equals(scope) ?
                getJsonHeader(token) :
                getJsonPayload(token);
        JSONParser parser = new JSONParser();

        JSONObject payloadObj = (JSONObject) parser.parse(payload);
        return (String) payloadObj.get(key);
    }

    public String getKeyId(String token) throws ParseException {
        return getClaim(token, "kid", SCOPE_HEADER);
    }

    public String getAlgorithm(String token) throws ParseException {
        return getClaim(token, "alg", SCOPE_HEADER);
    }

    public String getSubject(String token) throws ParseException {
        return getClaim(token, "sub", SCOPE_PAYLOAD);
    }

    public String getIssuer(String token) throws ParseException {
        return getClaim(token, "iss", SCOPE_PAYLOAD);
    }


}
