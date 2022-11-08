package com.project.csletter.member.exception;

import com.project.csletter.global.exception.BaseException;
import com.project.csletter.global.exception.BaseExceptionType;

public class MemberException extends BaseException {

    private BaseExceptionType exceptionType;

    public MemberException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }


    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
