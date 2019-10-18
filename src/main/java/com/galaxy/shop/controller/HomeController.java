package com.galaxy.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import springfox.documentation.annotations.ApiIgnore;

/**
 * Controlled to redirect to swagger ui
 * 
 * @author kandalakar.r
 *
 */
@Controller
@ApiIgnore
public class HomeController
{

    @GetMapping("/")
    public String swaggerUrl()
    {
        return "redirect:swagger-ui.html";

    }
}
