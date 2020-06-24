package com.fachini.beercrudl.controllers;

import com.fachini.beercrudl.entities.BeerColor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BeerColorInfo {

    private String id;
    private String description;
    private String hexColor;

    private BeerColorInfo() {
    }

    public static BeerColorInfo fromBeerColor(BeerColor beerColor) {
        return new BeerColorInfo(beerColor.toString(), beerColor.getDescription(), beerColor.getHexColor());
    }
}
