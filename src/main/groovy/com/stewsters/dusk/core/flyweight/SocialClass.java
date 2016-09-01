package com.stewsters.dusk.core.flyweight;

public enum SocialClass {

    KNIGHT("knight"),
    PRIEST("priest"),
    POACHER("poacher");

    final String name;

    SocialClass(String name) {
        this.name = name;
    }


}
