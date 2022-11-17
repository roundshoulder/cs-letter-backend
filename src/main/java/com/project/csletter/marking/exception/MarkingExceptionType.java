package com.project.csletter.marking.exception;

import com.project.csletter.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MarkingExceptionType implements BaseExceptionType {

    MarkingOverFlow(409,HttpStatus.CONFLICT, "1일 채점 횟수를 초과했습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    MarkingExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
