package com.edgewalk.springbootshiro.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    public static ApiResponse success() {
        return ApiResponse.success(null);
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(Status.SUCCESS.getCode(), Status.SUCCESS.getMessage(), data);
    }

    public static ApiResponse error(Status status) {
        return new ApiResponse(status.getCode(), status.getMessage(), null);
    }
    public static ApiResponse error(int code,String message) {
        return new ApiResponse(code,message,null);
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        SUCCESS(200, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Unknown Internal Error"),
        NOT_VALID_PARAM(40005, "Not valid Params"),
        NOT_SUPPORTED_OPERATION(40006, "Operation not supported"),
        NOT_LOGIN(500, "authentication error"),
        NOT_AUTHORIZATION(501, "用户没有当前资源的访问权限");

        private int code;
        private String message;

    }
}
