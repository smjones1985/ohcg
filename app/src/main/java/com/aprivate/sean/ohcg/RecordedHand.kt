package com.aprivate.sean.ohcg

import java.io.Serializable
import java.util.*

class RecordedHand : Serializable {
    var idNumber: UUID = UUID.randomUUID()
    var bid: Int = 0
    var hand: Int = 0
    var tricksTaken: Int = 0
    var pointsAtEndOfHand: Int = 0
}
