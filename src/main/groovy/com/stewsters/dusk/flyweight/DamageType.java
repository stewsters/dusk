package com.stewsters.dusk.flyweight;


public enum DamageType {

    SLASH("slash"),
    PIERCE("pierce"),
    BASH("bash"),
    WOOD("wood"),
    SILVER("silver"),
    IRON("iron"),
    FIRE("fire");

    String name;

    DamageType(String name) {
        this.name = name;
    }

}
