package com.ceos20.instagram.exception;


public class NotFoundException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public NotFoundException(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getErrorCode() {
        return exceptionCode;
    }
}
