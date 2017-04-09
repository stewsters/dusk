package com.stewsters.dusk.core.flyweight;

public enum AmmoType {
    ARROW("arrow");

    final String name;

    AmmoType(String name) {
        this.name = name;
    }
}