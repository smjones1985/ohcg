package com.aprivate.sean.ohcg

import java.io.Serializable
import java.util.Random

class RecordedHand : Serializable {

    var idNumber: Int = 0

    var bid: Int = 0

    var hand: Int = 0

    var tricksTaken: Int = 0
    var pointsAtEndOfHand: Int = 0

    init {
        idNumber = random.nextInt(Integer.MAX_VALUE)
    }

    companion object {

        private val random = Random()
    }
}
