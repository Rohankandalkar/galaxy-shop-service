package com.galaxy.shop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * Utility to for file related operations
 * 
 * @author kandalakar.r
 *
 */
public class GalaxyFileUtils
{
    /**
     * Accepts file and convert its lines into list of string
     * 
     * @param multipartFile - file contains transitional statements
     * @return - list file transaction statements 
     * @throws IOException - file related exceptions
     */
    public static List<String> extracTransactionStatmentsFromFile(MultipartFile multipartFile) throws IOException
    {

        List<String> inputList = new ArrayList<String>();

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));

        String line = null;
        while ((line = br.readLine()) != null)
        {
            inputList.add(line);
        }

        br.close();

        return inputList;
    }

}
