package com.stewsters.dusk.flyweight;


public enum Slot {

    PRIMARY_HAND("primary hand"),
    OFF_HAND("off hand"),
    CHEST("chest"),
    LEGS("legs"),
    HEAD("head");

    public final String name;

    Slot(String name) {
        this.name = name;
    }

}