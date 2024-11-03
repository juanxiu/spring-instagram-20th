package com.ceos20.instagram.exception;

public class BadRequestException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage()); // ExceptionCode의 메시지를 사용
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
