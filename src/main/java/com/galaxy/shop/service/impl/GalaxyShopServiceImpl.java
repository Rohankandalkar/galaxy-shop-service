package com.galaxy.shop.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.galaxy.shop.constants.GalaxyConstants;
import com.galaxy.shop.dto.GalaxyShopResponse;
import com.galaxy.shop.service.GalaxyShopService;
import com.galaxy.shop.util.GalaxyFileUtils;
import com.galaxy.shop.util.RomanNumeralsConverter;

/**
 * Transaction statement Processing Service Implementation
 * 
 * @author kandalakar.r
 *
 */
@Service
public class GalaxyShopServiceImpl implements GalaxyShopService
{
    private static Logger log = LoggerFactory.getLogger(GalaxyShopServiceImpl.class);


    /**
     * This is Service Implementation for Content Processing which further calls
     * Resolution Service. Resolution Service decides which service would work AWS
     * or FFmpeg.
     * 
     * @param MultipartFile
     * @return GalaxyShopResponse<List<String>>
     * @throws IOException
     */
    @Override
    public GalaxyShopResponse<List<String>> processTransactionStatments(MultipartFile multipartFile)
        throws IOException
    {

        log.debug("GalaxyShopServiceImpl : extracting transactional statements from file into list of string");
        List<String> transactionalStatementsDetails = GalaxyFileUtils.extracTransactionStatmentsFromFile(multipartFile);

        log.debug("GalaxyShopServiceImpl : preparing galaxy currency values in map as key - Galaxy currency and value - roman numarel");
        
        Map<String, String> galaxyCurrencyDetailsMap = calculateGalaxyCurrencyRomanNumerals(
                     transactionalStatementsDetails.stream().filter(p -> p.split(" ").length == 3).collect(Collectors.toList()));
        
        log.debug("GalaxyShopServiceImpl : prepaired currency details into map ");

        log.debug("GalaxyShopServiceImpl : preparing galaxy metal values in map as key - Galaxy metal name and value - metal's value");
        
        Map<String, Integer> galaxyMetalValuesDetailsMap = calculateGalaxyMetalValues(
                    transactionalStatementsDetails.stream().filter(p -> p.split(" ").length == 6 && p.endsWith(GalaxyConstants.CREDITS)).
                    collect(Collectors.toList()),galaxyCurrencyDetailsMap);

        log.debug("GalaxyShopServiceImpl : processing transactional statements using galaxyCurrencyDetailsMap and "
                    + "galaxyMetalValuesDetailsMap and storing its output in list of string ");

        List<String> processedFinalOutput =  calculateCreditDetails(
                    transactionalStatementsDetails.stream().filter(p -> p.split(" ").length >= 8 && p.endsWith("?"))
                    .collect(Collectors.toList()),galaxyCurrencyDetailsMap, galaxyMetalValuesDetailsMap);

        log.debug("GalaxyShopServiceImpl : preparing response for user with processed statements results");
        GalaxyShopResponse<List<String>> respGalaxyShopResponse = new GalaxyShopResponse<List<String>>();
        respGalaxyShopResponse.setData(processedFinalOutput);
        respGalaxyShopResponse.setMessage(GalaxyConstants.PROCESSING_SUCCESS_STATUS);
        respGalaxyShopResponse.setStatus(HttpStatus.OK);

        return respGalaxyShopResponse;
    }


    /**
     * Processing Users transaction statements with prepared galaxyCurrencyDetailsMap and galaxyMetalValuesDetailsMap
     * calculating output credit values with the help of currency and metal details
     *   
     * 
     * @param transactionalGalaxyMetalStatementsDetails - transaction statements which we have to process 
     * @param galaxyCurrencyDetailsMap - map contains details of currency 
     * @param galaxyMetalValuesDetailsMap - map contains details of metal values
     * @return List<String> - processed output of transaction statements
     */
    public List<String> calculateCreditDetails(
        List<String> transactionalGalaxyMetalStatementsDetails,
        Map<String, String> galaxyCurrencyDetailsMap, Map<String, Integer> galaxyMetalValuesDetailsMap)

    {

        log.debug("GalaxyShopServiceImpl : processing transaction statements started ");

        List<String> transactionStatmentProcessedResult = new ArrayList<String>();

        transactionalGalaxyMetalStatementsDetails.stream().filter(predicate -> predicate.split(" ").length == 8).map(mapper -> mapper.split(" ")).forEach(action -> {

            int indexOfIs = Arrays.asList(action).indexOf(GalaxyConstants.IS);

            String metalName = action[indexOfIs + 3];

            String firstCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[indexOfIs + 1]);

            String secondCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[indexOfIs + 2]);

            boolean isContainsMetalCalculation = galaxyMetalValuesDetailsMap.keySet().stream().anyMatch(element -> Arrays.asList(action).contains(element));

            if (isContainsMetalCalculation)
            {
                Integer credits = galaxyMetalValuesDetailsMap.get(action[indexOfIs + 3]);

                Integer finalMetalCreditsValue = (RomanNumeralsConverter
                        .convertRomanNumeralsToInteger(firstCurrencyRomanNumerals + secondCurrencyRomanNumerals))
                        * credits;

                transactionStatmentProcessedResult
                    .add(
                        action[indexOfIs + 1]
                            + " " + action[indexOfIs + 2] + " "
                            + metalName + " is " + finalMetalCreditsValue + " credits");

            }
            else
            {
                String thirdCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[indexOfIs + 3]);

                String fourthCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[indexOfIs + 4]);

                Integer finalMetalCreditsValue =
                    (RomanNumeralsConverter
                        .convertRomanNumeralsToInteger(
                            firstCurrencyRomanNumerals
                                + secondCurrencyRomanNumerals
                                + thirdCurrencyRomanNumerals + fourthCurrencyRomanNumerals));

                transactionStatmentProcessedResult
                    .add(
                        action[indexOfIs + 1]
                            + " " + action[indexOfIs + 2] + " "
                            + action[indexOfIs + 3] + " " + action[indexOfIs + 4] + " is " + finalMetalCreditsValue);
            }

        });

        long invalidInputFrequency = transactionalGalaxyMetalStatementsDetails.stream().filter(predicate -> predicate.split(" ").length > 8).count();

        if (invalidInputFrequency != 0)
        {
            for (int index = 0; index < invalidInputFrequency; index++)
            {
                transactionStatmentProcessedResult.add("I have no idea what you are talking about");
            }

        }

        log.debug("GalaxyShopServiceImpl : processing transaction statements completed ");

        return transactionStatmentProcessedResult;

    }


    /**
     * Processing only metal related transaction statements  
     * Preparing map which contains key - metal name and value - its calculated value  
     * 
     * 
     * @param transactionalGalaxyMetalStatementsDetails - metal transaction statements
     * @param galaxyCurrencyDetailsMap - currency numeral values for processing metal statements
     * @return Map - key - metal name and value - its calculated value
     */
    public Map<String, Integer> calculateGalaxyMetalValues(
        List<String> transactionalGalaxyMetalStatementsDetails,
        Map<String, String> galaxyCurrencyDetailsMap)
    {

        log.debug("GalaxyShopServiceImpl : extracting metal details from metal transaction statements and storing it into map ");

        Map<String, Integer> metalValuesDetailsMap = new HashMap<String, Integer>();

        transactionalGalaxyMetalStatementsDetails.stream().map(mapper -> mapper.split(" ")).forEach(action -> {

            int indexOfIs = Arrays.asList(action).indexOf(GalaxyConstants.IS);

            String metalName = action[indexOfIs - 1];

            String firstCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[0]);

            String secondCurrencyRomanNumerals = galaxyCurrencyDetailsMap.get(action[1]);

            Integer credits = Integer.parseInt(action[indexOfIs + 1]);

            Integer finalMetalCreditsValue =
                (credits
                    / RomanNumeralsConverter
                        .convertRomanNumeralsToInteger(firstCurrencyRomanNumerals + secondCurrencyRomanNumerals));

            metalValuesDetailsMap.put(metalName, finalMetalCreditsValue);
        });

        log.debug("GalaxyShopServiceImpl : extracted metal details and stored it into map ");

        return metalValuesDetailsMap;

    }


    /**
     * Processing only currency statements and preparing map in which
     * key - currency name
     * value - Roman numeral 
     * as key-value pair map
     *  
     * 
     * @param transactionalGalaxyCurrencyStatementsDetails - currency statements list 
     * 
     * @return Map<String, String> - key - currency name and value - its roman numeral
     */
    public Map<String, String> calculateGalaxyCurrencyRomanNumerals(
        List<String> transactionalGalaxyCurrencyStatementsDetails)
    {
        log.debug("GalaxyShopServiceImpl : extracting currency details from transaction statements and storing it into map ");

        return transactionalGalaxyCurrencyStatementsDetails
            .stream().map(mapper -> mapper.split(" "))
            .collect(Collectors.toMap(key -> key[0], mapper -> mapper[2]));

    }

}
