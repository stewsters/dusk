package com.stewsters.dusk.core.map.gen.name

import com.stewsters.dusk.core.flyweight.Gender
import com.stewsters.util.math.MatUtils

class KnightNameGen {

    public static String generate(Gender gender = null, int maxLength = 20) {


        if (!gender)
            gender = MatUtils.boolean ? Gender.MALE : Gender.FEMALE

        String name = MatUtils.randVal(new File("assets/names/HUMAN/${gender.name}.txt").text.split("\\s"))

        // Prefix title
        if (MatUtils.boolean && name.length() < maxLength) {
            String prefix = gender == Gender.MALE ? MatUtils.rand(["Master", "Sir", "Lord"]) : MatUtils.rand(["Mistress", "Dame", "Lady"])

            String tempName = "$prefix $name"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        // The
//        "the Bold"
//        "the Slayer"
//        "the Color" // white, black, red, green,

        if (MatUtils.boolean && name.length() < maxLength) {
            String suffix = MatUtils.rand(["Bold", "Slayer", "Dark",
                                           "White", "Black", "Red", "Green", "Blue",
                                           "Vigilant", "Devious", "Bastard",
                                           "Lion", "Dragon", "Viper", "Manticore",
                                           "Gaunt", "Great", "Strong",
                                           "Younger", "Older", "Bulwark", "Ghost"
            ])

            String tempName = "$name the $suffix"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        // Of (place)
        if (MatUtils.boolean && name.length() < maxLength) {
            String country = MatUtils.rand([
                    "Alumir", "Bador", "Caerleon", "Donsburg",
                    "the East", "the West", "the North", "the South",
                    "the Sea", "the Mist", "the Lake", "the Hills", "the Desert", "the Mountains"
            ])

            String tempName = "$name of $country"
            if (tempName.length() < maxLength) {
                name = tempName
            }
        }

        return name
    }
}
