package com.example.demo.controller.resource;

import java.io.Serializable;

/**
 * @author sandesha
 */
public class UIResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private transient T entity;
    private Integer status;
    private String message;

    /**
     * @param entity
     */
    public UIResponse( T entity )
    {
        this.entity = entity;
    }

    /**
     * No arg constructor
     */
    public UIResponse()
    {
        super();
    }

    public T getEntity()
    {
        return entity;
    }

    public void setEntity( T entity )
    {
        this.entity = entity;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus( Integer status )
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
}
