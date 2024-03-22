package com.moment.core.common.code;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400,"Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(500, "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400,  "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(500, "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(400, "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "Header에 데이터가 존재하지 않는 경우 "),

    // Untitled 여행 삭제 시도
    UNTITLED_TRIP_DELETE_ERROR(400, "Untitled Trip Delete Exception"),

//토큰 관련 에러
    UNAUTHORIZED_ERROR(401, "Forbidden Exception"),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "Internal Server Error Exception"),

    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // 이미 존재하는 값떄문에 생기는 에러
//    잘못된 인수를 인자로 받았을때
    INVALID_PARAMETER(400, "Invalid parameter"),
    VALIDATION_CONSTRAINT_NOT_FOUND(400, "No validator found for validation constraint"),


    // Transaction Insert Error
    INSERT_ERROR(200, "Insert Transaction Error Exception"),

    // Transaction CommentUpdate Error
    UPDATE_ERROR(200, "CommentUpdate Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "Delete Transaction Error Exception"),

    // 등록하고자 하는 유저아이디가 이미 존재함
    USER_ALREADY_EXIST(400, "User already exists"),

    USER_NOT_FOUND(400, "User not found"),

    ALREADY_BOOKED_DATE(400, "Already booked date"),

    ; // End

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

}
