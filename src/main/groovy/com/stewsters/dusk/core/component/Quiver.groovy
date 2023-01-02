package com.stewsters.dusk.core.component

import com.stewsters.dusk.core.flyweight.AmmoType
import com.stewsters.util.math.MatUtils
import groovy.transform.CompileStatic

@CompileStatic
class Quiver {

    //this if for counter items.
    public int maxAmmo = 100
    Map<AmmoType, Integer> pouch = [:]

    int getAmmoCount(AmmoType ammoType) {
        return pouch[ammoType] ?: 0
    }


    void addAmmo(AmmoType ammoType, int quantity) {
        pouch[ammoType] = MatUtils.limit((pouch[ammoType] ?: 0) + quantity, 0, maxAmmo)
    }

    boolean useAmmo(AmmoType ammoType) {
        if (pouch[ammoType]) {
            pouch[ammoType] = pouch[ammoType] - 1
            return true
        } else return false

    }

}
