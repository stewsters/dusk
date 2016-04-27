package com.stewsters.dusk.flyweight;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    final String name;

    Gender(String name) {
        this.name = name;
    }
}
