package com.stewsters.dusk.map.gen.name

import com.stewsters.dusk.flyweight.Gender
import com.stewsters.util.math.MatUtils

/**
 * Created by stewsters on 3/9/15.
 */
class KnightNameGen {

    public static String generate(Gender gender = null,maxLength = 20) {


        if (!gender)
            gender = MatUtils.boolean ? Gender.MALE : Gender.FEMALE

        String name = MatUtils.randVal(new File("assets/names/HUMAN/${gender.name}.txt").text.split("\\s"))

        if (MatUtils.boolean && name.length() < maxLength) {
            String prefix = gender == Gender.MALE ? MatUtils.rand(["Sir", "Lord"]) : MatUtils.rand(["Dame", "Lady"])

            String tempName = "$prefix $name"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        if (MatUtils.boolean && name.length() < maxLength) {
            String suffix = MatUtils.rand(["Bold", "Slayer", "Dark",
                                           "White", "Black", "Red", "Green",
                                           "Vigilant", "Devious", "Bastard",
                                           "Lion", "Dragon", "Viper", "Manticore",
                                           "Gaunt", "Great", "Strong",
                                           "Younger", "Older"
            ])

            String tempName = "$name the $suffix"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        // The
//        "the Bold"
//        "the Slayer"
//        "the Color" // white, black, red, green,

        if (MatUtils.boolean && name.length() < maxLength) {
            String country = MatUtils.rand([
                    "Alumir", "Bador",
                    "the East", "the West", "the North", "the South",
                    "the Sea", "the Mist","the Mountains"
            ])

            String tempName = "$name of $country"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        return name
    }
}
