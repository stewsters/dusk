package com.stewsters.dusk.flyweight;

public enum AmmoType {
    rifle("5.56x45 mm"),
    pistol("9 mm"),
    shotgun("12 gauge");


    String technicalName;

    AmmoType(String technicalName) {
        this.technicalName = technicalName;
    }
}