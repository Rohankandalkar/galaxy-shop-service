package com.galaxy.shop.util;

/**
 * Utility class for Galaxy shop app used to convert Roman numeral to integer
 * 
 * @author kandalakar.r
 *
 */
public class RomanNumeralsConverter
{

    /**
     * method accepts romanNumaral and returns its integer representation *
     * 
     * @param romanNumeral - Individual RomanNumaral
     * @return integer value of romanNumaral
     */
    public static int numericValueOfRomanNumerals(char romanNumeral)
    {
        if (romanNumeral == 'I')
            return 1;
        if (romanNumeral == 'V')
            return 5;
        if (romanNumeral == 'X')
            return 10;
        if (romanNumeral == 'L')
            return 50;
        if (romanNumeral == 'C')
            return 100;
        if (romanNumeral == 'D')
            return 500;
        if (romanNumeral == 'M')
            return 1000;
        return -1;
    }


    /**
     * @param romanNumaral - method convert this romanNumaral into integer value
     * @return int value of romanNumaral provided by user
     */
    public static int convertRomanNumeralsToInteger(String romanNumaral)
    {

        int romansDecimalValue = 0;

        // Splitting string of RomanNumaral to calculate value of individual Roman
        // number to integer value
        for (int temp = 0; temp < romanNumaral.length(); temp++)
        {

            int previousValue = numericValueOfRomanNumerals(romanNumaral.charAt(temp));

            if (temp + 1 < romanNumaral.length())
            {
                int nextValue = numericValueOfRomanNumerals(romanNumaral.charAt(temp + 1));

                // Comparing both values
                if (previousValue >= nextValue)
                {

                    // Value of current symbol is greater or equal to the next symbol
                    romansDecimalValue = romansDecimalValue + previousValue;
                }
                else
                {
                    romansDecimalValue = romansDecimalValue + nextValue - previousValue;
                    temp++;
                }
            }
            else
            {
                romansDecimalValue = romansDecimalValue + previousValue;
                temp++;
            }
        }
        return romansDecimalValue;
    }
}
