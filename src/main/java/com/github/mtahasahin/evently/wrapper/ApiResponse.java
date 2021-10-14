package com.github.mtahasahin.evently.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    private T data;
    private ResponseResultType resultType;
    private List<String> messages;

    public static <T> ApiResponse<T> Success(T data){
        return new ApiResponse<>(data, ResponseResultType.SUCCESS, new ArrayList<>());
    }

    public static <T> ApiResponse<T> Success(T data, String message){
        return new ApiResponse<>(data, ResponseResultType.SUCCESS, new ArrayList<>(){{add(message);}});
    }

    public static <T> ApiResponse<T> Success(T data, List<String> messages){
        return new ApiResponse<>(data, ResponseResultType.SUCCESS, messages);
    }

    public static <T> ApiResponse<T> Error(T data){
        return new ApiResponse<>(data, ResponseResultType.ERROR, new ArrayList<>(){{add("Error");}});
    }

    public static <T> ApiResponse<T> Error(T data, String message){
        return new ApiResponse<>(data, ResponseResultType.ERROR, new ArrayList<>(){{add(message);}});
    }

    public static <T> ApiResponse<T> Error(T data, List<String> messages){
        return new ApiResponse<>(data, ResponseResultType.ERROR, messages);
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
