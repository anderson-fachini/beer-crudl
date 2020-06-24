package com.fachini.beercrudl.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerColor {

    PALE_STRAW("Pale Straw [SRM 2 | EBC 4]", "F8F753"), //
    STRAW("Straw [SRM 3 | EBC 6]", "F6F513"), //
    PALE_GOLD("Pale Gold [SRM 4 | EBC 8]", "ECE61A"), //
    DEEP_GOLD("Deep Gold [SRM 6 | EBC 12]", "D5BC26"), //
    PALE_AMBER("Pale Amber [SRM 9 | EBC 18]", "BF923B"), //
    MEDIUM_AMBER("Medium Amber [SRM 12 | EBC 24]", "BF813A"), //
    DEEP_AMBER("Deep Amber [SRM 15 | EBC 30]", "BC6733"), //
    AMBER_BROWN("Amber Brown [SRM 18 | EBC 35]", "8D4C32"), //
    BROWN("Brown [SRM 20 | EBC 39]", "5D341A"), //
    RUBI_BROWN("Rubi Brown [SRM 24 | EBC 47]", "261716"), //
    DEEP_BROWN("Deep Brown [SRM 30 | EBC 59]", "0F0B0A"), //
    BLACK("Black [SRM 40 | EBC 79]", "080707");

    private String description;
    private String hexColor;
}
