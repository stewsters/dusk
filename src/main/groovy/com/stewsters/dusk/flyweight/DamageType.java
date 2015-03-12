package com.stewsters.dusk.flyweight;


public enum DamageType {

    SLASH("Slash"),
    PIERCE("Pierce"),
    BASH("Bash"),
    WOOD("Wood"),
    SILVER("Silver"),
    IRON("Iron"),
    FIRE("Fire");

    String name;

    DamageType(String name) {
        this.name = name;
    }

}
