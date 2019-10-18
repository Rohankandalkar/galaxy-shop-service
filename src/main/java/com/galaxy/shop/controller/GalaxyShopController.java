package com.galaxy.shop.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.galaxy.shop.dto.GalaxyShopResponse;
import com.galaxy.shop.service.GalaxyShopService;

/**
 * Request Mapping Controller for Galaxy Transaction statement Processing 
 * <p> 
 *     Controller class that provides functionality to map request for <b>Transaction statement Processing</b> 
 *     to Service Class.
 * </p>
 * 
 * @author kandalakar.r
 *
 */
@RestController
@RequestMapping(value = "/galaxy")
public class GalaxyShopController
{

    private static Logger log = LoggerFactory.getLogger(GalaxyShopController.class);

    @Autowired
    private GalaxyShopService demoService;


    /**
     * Controller method for 
     * <p> 
     *      Passes file(contains transaction statements) to service layer and
     *      Thereafter wrapping response and 
     *      Returning wrapped response.
     * </p> 
     * @param multipartFile - File contains transaction statements 
     * @return - Wrapped Response of transaction statements  
     * @throws IOException - throws when file related exception occurs 
     */
    @PostMapping(value = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GalaxyShopResponse<List<String>>> processGalaxyTransaction(MultipartFile multipartFile)
        throws IOException
    {

        log.debug("Controller : Ready for processing transaction statements");
        GalaxyShopResponse<List<String>> transactionStatementResult = this.demoService.processTransactionStatments(multipartFile);

        log.debug("Controller : Returning wrapped Response");
        return new ResponseEntity<GalaxyShopResponse<List<String>>>(transactionStatementResult, HttpStatus.OK);

    }
}
