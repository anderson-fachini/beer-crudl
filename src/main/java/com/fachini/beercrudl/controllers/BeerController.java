package com.fachini.beercrudl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeerController {

    @GetMapping("/")
    public String list() {
        return "beers";
    }
}
