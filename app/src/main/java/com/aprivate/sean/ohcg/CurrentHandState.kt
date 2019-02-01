package com.aprivate.sean.ohcg

import java.io.Serializable
import java.util.*

class CurrentHandState : Serializable {
    var uid: UUID = UUID.randomUUID()
    var currentOrderNumber = 1
    var dealCount: Int = 0
    var totalBidsForCurrentHand: Int = 0

    val playerOrderMapToIndex: MutableMap<Int, ScoreBoardItem> = mutableMapOf()
}
