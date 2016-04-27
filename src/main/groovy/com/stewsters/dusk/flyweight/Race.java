package com.stewsters.dusk.flyweight;

public enum Race {
    HUMAN("human"),
    ELF("elf"),
    DWARF("dwarf");

    final String name;

    Race(String name) {
        this.name = name;
    }

}
