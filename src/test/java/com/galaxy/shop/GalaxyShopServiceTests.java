package com.galaxy.shop;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.galaxy.shop.service.impl.GalaxyShopServiceImpl;
import com.galaxy.shop.util.RomanNumeralsConverter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GalaxyShopServiceTests
{
    @Autowired
    GalaxyShopServiceImpl demoServiceImpl;


    /**
     * Roman Numeral conversion test
     */
    @Test
    public void convertRomanNumeralsToIntegerTest()
    {

        String romanNumeralValue = "VII";

        Integer expetdedIntValueOfromanNumeral = 7;

        Integer actualIntValueOfromanNumeral = RomanNumeralsConverter.convertRomanNumeralsToInteger(romanNumeralValue);

        assertEquals(actualIntValueOfromanNumeral, expetdedIntValueOfromanNumeral);

    }


    /**
     * Processing currency statements and currency and its Roman numeral into map test
     */
    @Test
    public void calculateGalaxyCurrencyRomanNumeralsTest()
    {

        List<String> inputList = new ArrayList<String>();

        inputList.add("groot is I");
        inputList.add("kree is V");
        inputList.add("pish is X");
        inputList.add("tegj is L");

        Map<String, String> expectedValueMap = new HashMap<String, String>();
        expectedValueMap.put("groot", "I");
        expectedValueMap.put("kree", "V");
        expectedValueMap.put("pish", "X");
        expectedValueMap.put("tegj", "L");

        Map<String, String> actualValueMap = demoServiceImpl.calculateGalaxyCurrencyRomanNumerals(inputList);

        assertThat(actualValueMap, is(expectedValueMap));

    }


    /**
     *  
     * Processing metal statements and mapping metal and its value into map test
     * 
     */
    @Test
    public void calculateGalaxyMetalValuesTest()
    {

        List<String> inputList = new ArrayList<String>();

        inputList.add("groot groot Silver is 34 Credits");
        inputList.add("groot kree Gold is 57800 Credits");
        inputList.add("pish pish Iron is 3910 Credits");

        Map<String, String> galaxyCurrencyDetailsMap = new HashMap<String, String>();
        galaxyCurrencyDetailsMap.put("groot", "I");
        galaxyCurrencyDetailsMap.put("kree", "V");
        galaxyCurrencyDetailsMap.put("pish", "X");
        galaxyCurrencyDetailsMap.put("tegj", "L");

        Map<String, Integer> expectedValueMap = new HashMap<String, Integer>();
        expectedValueMap.put("Silver", 17);
        expectedValueMap.put("Gold", 14450);
        expectedValueMap.put("Iron", 195);

        Map<String, Integer> actualValueMap = demoServiceImpl.calculateGalaxyMetalValues(inputList, galaxyCurrencyDetailsMap);

        assertThat(actualValueMap, is(expectedValueMap));

    }


    /**
     *  
     * Processing transaction statements and calculating its value and storing into list test
     * 
     */
    @Test
    public void calculateCreditDetailsTest()
    {

        List<String> inputList = new ArrayList<String>();

        inputList.add("how much is pish tegj groot groot ?");
        inputList.add("how many Credits is groot kree Silver ?");
        inputList.add("how many Credits is groot kree Gold ?");
        inputList.add("how many Credits is groot kree Iron ?");
        inputList.add("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");

        Map<String, String> galaxyCurrencyDetailsMap = new HashMap<String, String>();
        galaxyCurrencyDetailsMap.put("groot", "I");
        galaxyCurrencyDetailsMap.put("kree", "V");
        galaxyCurrencyDetailsMap.put("pish", "X");
        galaxyCurrencyDetailsMap.put("tegj", "L");

        Map<String, Integer> galaxyMetalValuesDetailsMap = new HashMap<String, Integer>();
        galaxyMetalValuesDetailsMap.put("Silver", 17);
        galaxyMetalValuesDetailsMap.put("Gold", 14450);
        galaxyMetalValuesDetailsMap.put("Iron", 195);

        List<String> expectedOutputList = new ArrayList<String>();

        expectedOutputList.add("pish tegj groot groot is 42");
        expectedOutputList.add("groot kree Silver is 68 credits");
        expectedOutputList.add("groot kree Gold is 57800 credits");
        expectedOutputList.add("groot kree Iron is 780 credits");
        expectedOutputList.add("I have no idea what you are talking about");

        List<String> actualValue = demoServiceImpl.calculateCreditDetails(inputList, galaxyCurrencyDetailsMap, galaxyMetalValuesDetailsMap);

        assert (actualValue.containsAll(expectedOutputList));

    }

}
