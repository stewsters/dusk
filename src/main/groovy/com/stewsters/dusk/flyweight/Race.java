package com.stewsters.dusk.flyweight;

public enum Race {
    HUMAN("human"),
    ELF("elf"),
    DWARF("dwarf");

    String name;

    Race(String name) {
        this.name = name;
    }

}
