package com.moment.auth.utils;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthInfo {
    String sub;
    String iss;
}
