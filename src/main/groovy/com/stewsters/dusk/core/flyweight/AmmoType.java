package com.stewsters.dusk.core.flyweight;

public enum AmmoType {
    ARROW("arrow"),
    BULLET("bullet");

    final String name;

    AmmoType(String name) {
        this.name = name;
    }
}