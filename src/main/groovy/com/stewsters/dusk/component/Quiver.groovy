package com.stewsters.dusk.component

import com.stewsters.dusk.flyweight.AmmoType
import com.stewsters.util.math.MatUtils


public class Quiver {

    //this if for counter items.
    public int maxAmmo = 100
    Map<AmmoType, Integer> pouch = [:]

    public int getAmmoCount(AmmoType ammoType) {
        return pouch[ammoType] ?: 0
    }


    public addAmmo(AmmoType ammoType, int quantity) {
        pouch[ammoType] = MatUtils.limit((pouch[ammoType] ?: 0) + quantity, 0, maxAmmo)
    }

    public boolean useAmmo(AmmoType ammoType) {
        if (pouch[ammoType]) {
            pouch[ammoType]--;
            return true
        } else return false;

    }

}
