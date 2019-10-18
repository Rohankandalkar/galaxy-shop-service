package com.galaxy.shop.dto;

import org.springframework.http.HttpStatus;

/**
 * Class for returning custom response object
 *  * 
 * @author kandalakar.r
 *
 * @param <T>
 */
public class GalaxyShopResponse<T>
{

    private HttpStatus status;

    private String message;

    private T data;


    public GalaxyShopResponse()
    {

    }


    public GalaxyShopResponse(HttpStatus status, String message, T data)
    {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public String getMessage()
    {
        return message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public HttpStatus getStatus()
    {
        return status;
    }


    public T getData()
    {
        return data;
    }


    public void setData(T data)
    {
        this.data = data;
    }


    public void setStatus(HttpStatus notFound)
    {
        this.status = notFound;
    }

}
