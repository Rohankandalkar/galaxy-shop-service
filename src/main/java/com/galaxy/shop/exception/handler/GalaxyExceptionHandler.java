package com.galaxy.shop.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.galaxy.shop.dto.GalaxyShopResponse;

/**
 * 
 * Exception handling controller 
 * <p> For generating exception related response. </p>
 * 
 * @author kandalakar.r
 *
 */
@ControllerAdvice
public class GalaxyExceptionHandler
{

    /**
     * @param exception
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<GalaxyShopResponse<Object>> fileProcessingFailedException(IOException exception)
    {

        GalaxyShopResponse<Object> errorResponse = new GalaxyShopResponse<Object>();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(exception.getMessage());
        return new ResponseEntity<GalaxyShopResponse<Object>>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
