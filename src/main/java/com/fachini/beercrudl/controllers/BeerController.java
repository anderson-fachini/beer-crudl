package com.fachini.beercrudl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;

@Api(tags = "Server root")
@Controller
public class BeerController {

    @GetMapping("/")
    public String list() {
        return "beers";
    }
}
