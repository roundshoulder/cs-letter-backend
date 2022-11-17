package com.project.csletter.marking.exception;

import com.project.csletter.global.exception.BaseException;
import com.project.csletter.global.exception.BaseExceptionType;

public class MarkingException extends BaseException {

    private BaseExceptionType exceptionType;

    public MarkingException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }


    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
