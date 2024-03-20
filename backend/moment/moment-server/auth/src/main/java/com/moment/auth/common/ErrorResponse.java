package com.moment.auth.common;


import com.moment.auth.common.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;                 // 에러 상태 코드
    private String resultMsg;           // 에러 메시지
    private Object errors;    // 상세 에러 메시지
    private String reason;              // 에러 이유


    @Builder
    protected ErrorResponse(final ErrorCode code) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.errors = "";
        this.reason = "";
    }


    @Builder
    protected ErrorResponse(final ErrorCode code, final String reason) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.reason = reason;
        this.errors = "";
    }


    @Builder
    protected ErrorResponse(final ErrorCode code, final Object errors) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.reason = "";
    }



    /**
     * Global Exception 전송 타입-2
     *
     * @param code ErrorCode
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    /**
     * Global Exception 전송 타입-3
     *
     * @param code   ErrorCode
     * @param reason String
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }
    public static ErrorResponse of(final ErrorCode code, final Object errors) {
        return new ErrorResponse(code, errors);
    }

}
