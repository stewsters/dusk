package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import groovy.transform.CompileStatic
import squidpony.squidcolor.SColor

@CompileStatic
public trait Spell {

    private int level = 1
    private Character key
    private String name

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        level
    }

    public String getName() {
        name
    }

    public void setName(String name) {
        this.name = name
    }

    public Character getKey() {
        key
    }

    public void setKey(Character c) {
        key = c
    }

    public String getDescription() {
        return "Unknown.  How mysterious!"
    }


    public boolean cast(Entity castor) {
        MessageLog.send("The spell does something unknown.", SColor.GREEN)
        return false;
    }

}
