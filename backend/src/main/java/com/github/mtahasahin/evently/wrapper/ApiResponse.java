package com.github.mtahasahin.evently.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    private T data;
    private ResponseResultType resultType;
    private String message;
    private List<ApiSubError> errors;

    public static <T> ApiResponse<T> Success(T data) {
        return new ApiResponse<>(data, ResponseResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> Success(T data, String message) {
        return new ApiResponse<>(data, ResponseResultType.SUCCESS, message, null);
    }

    public static <T> ApiResponse<T> Error(T data) {
        return new ApiResponse<>(data, ResponseResultType.ERROR, "An error occurred.", null);
    }

    public static <T> ApiResponse<T> Error(T data, String message) {
        return new ApiResponse<>(data, ResponseResultType.ERROR, message, null);
    }

    public static <T> ApiResponse<T> Error(T data, String messages, List<ApiSubError> errors) {
        return new ApiResponse<>(data, ResponseResultType.ERROR, messages, errors);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.resultType == ResponseResultType.SUCCESS;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiSubError {
        private String field;
        private String message;
    }

}

@Getter
enum ResponseResultType {
    SUCCESS(1),
    ERROR(2);

    private final int code;

    ResponseResultType(int typeCode) {
        this.code = typeCode;
    }
}
