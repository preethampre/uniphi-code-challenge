package com.example.demo.controller.resource;

/**
 * @author sandesha
 */
public class UIErrorMessage<T> extends UIResponse<T> {

    /**
     *
     */
    private static final long serialVersionUID = 4311696013200578760L;
    private String messageCode;
    private String message;
    private Integer status;
    private String stackTrace;

    /**
     * @param t
     */
    public UIErrorMessage( T t )
    {
        super(t);
    }

    /**
     * No arg constructor
     */
    public UIErrorMessage()
    {
        super();
    }

    @Override
    public String toString()
    {
        return "UIErrorMessage [messageCode=" + messageCode + ", message=" + message + ", stackTrace=" + stackTrace
                + "]";
    }

    public String getMessageCode()
    {
        return messageCode;
    }

    public void setMessageCode( String messageCode )
    {
        this.messageCode = messageCode;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    @Override
    public void setMessage( String message )
    {
        this.message = message;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace( String stackTrace )
    {
        this.stackTrace = stackTrace;
    }

    @Override
    public Integer getStatus()
    {
        return status;
    }

    @Override
    public void setStatus( Integer status )
    {
        this.status = status;
    }

}
