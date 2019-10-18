package com.galaxy.shop.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.galaxy.shop.dto.GalaxyShopResponse;

/**
 * 
 * Interface for Transaction statements Processing Service.

 * @author kandalakar.r
 *
 */
public interface GalaxyShopService
{

    public GalaxyShopResponse<List<String>> processTransactionStatments(MultipartFile multipartFile) throws IOException;
}
