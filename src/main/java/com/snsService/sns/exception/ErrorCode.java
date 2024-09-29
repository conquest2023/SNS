package com.snsService.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"user not Founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Token is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"Post not founded"),
    INVALID_PERMISSION(HttpStatus.NOT_FOUND,"Permission is invalid"),
    ALREADY_LIKED(HttpStatus.CONFLICT,"User already liked the post"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Sever error");

    private HttpStatus status;
    private  String message;
}
