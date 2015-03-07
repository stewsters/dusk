package com.stewsters.dusk.flyweight;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    String name;

    Gender(String name) {
        this.name = name;
    }
}
