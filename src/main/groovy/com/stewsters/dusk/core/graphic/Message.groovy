package com.stewsters.dusk.core.graphic

import com.stewsters.dusk.core.entity.Entity
import squidpony.squidcolor.SColor

class Message {

    public String message
    public SColor color
    public List<Entity> concerning


    Message(Map params) {
        message = params.message
        color = params.color
        concerning = params.concerning
    }


}
