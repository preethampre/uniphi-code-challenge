package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author sandesha
 */
public class DataException extends Exception {

    private final String errorCode;

    private final String errorMessage;

    private final HttpStatus httpStatus;

    private final String errorRootCause;

    private final StackTraceElement[] stackTrace;

    /**
     */
    public DataException( String errorCode, String errorMessage, HttpStatus httpStatus )
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.errorRootCause = null;
        this.stackTrace = null;
    }

    public DataException( String errorCode, String errorMessage, String errorRootCause, StackTraceElement[] stackTrace,
            HttpStatus httpStatus )
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.errorRootCause = errorRootCause;
        this.stackTrace = stackTrace;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public String getErrorRootCause()

    {
        return errorRootCause;
    }

    public StackTraceElement[] getStackTrace()
    {
        return stackTrace;
    }
}
