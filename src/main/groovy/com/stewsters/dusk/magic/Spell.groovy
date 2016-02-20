package com.stewsters.dusk.magic

import com.stewsters.dusk.entity.Entity
import com.stewsters.dusk.graphic.MessageLog
import squidpony.squidcolor.SColor

public trait Spell {

    public int level
    public Character key;

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        level
    }

    public Character getKey() {
        key
    }

    public String getDescription() {
        return "Unknown.  How mysterious!"
    }


    public boolean cast(Entity castor) {
        MessageLog.send("The spell does something unknown.", SColor.GREEN)
    };

}
