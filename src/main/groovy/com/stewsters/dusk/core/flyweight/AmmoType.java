package com.stewsters.dusk.core.flyweight;

public enum AmmoType {
    rifle("5.56x45 mm"),
    pistol("9 mm"),
    shotgun("12 gauge");


    final String technicalName;

    AmmoType(String technicalName) {
        this.technicalName = technicalName;
    }
}