package com.example.demo.controller.resource;

import com.example.demo.controller.resource.UIErrorMessage;
import com.example.demo.controller.resource.UIResponse;

import com.example.demo.exceptions.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author sandesha
 */
@ControllerAdvice
public class AbstractController {


    protected <T> ResponseEntity<UIResponse<T>> buildResponse( final T t )
    {
        final UIResponse<T> uiResponse = new UIResponse<>(t);
        uiResponse.setStatus(HttpStatus.OK.value());
        uiResponse.setMessage("Success");
        return new ResponseEntity<>(uiResponse, HttpStatus.OK);
    }

    @ExceptionHandler( DataException.class )
    public ResponseEntity<UIErrorMessage> buildError( final DataException e )
    {
        final UIErrorMessage message = new UIErrorMessage();
        message.setMessageCode(e.getErrorCode());
        message.setMessage(e.getErrorMessage());
        if( e.getHttpStatus().equals(HttpStatus.BAD_REQUEST) )
        {
            message.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        if( e.getHttpStatus().equals(HttpStatus.FORBIDDEN) )
        {
            message.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }
        if( e.getHttpStatus().equals(HttpStatus.NOT_FOUND) )
        {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if( e.getHttpStatus().equals(HttpStatus.CONFLICT) )
        {
            message.setStatus(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
