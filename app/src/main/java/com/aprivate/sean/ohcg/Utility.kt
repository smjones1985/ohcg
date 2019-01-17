package com.aprivate.sean.ohcg

import java.util.Random

object Utility {
    val randomInt: Int
        get() {
            if (random == null) {
                random = Random()
            }
            return random!!.nextInt(Integer.MAX_VALUE)
        }

    private var random: Random? = null


}
