package com.aprivate.sean.ohcg

import java.io.Serializable
import kotlin.collections.HashMap

class CurrentHandState : Serializable {


    var currentOrderNumber = 1
    var dealCount: Int = 0

    var totalBidsForCurrentHand: Int = 0

    val playerOrderMapToIndex: HashMap<Int, ScoreBoardItem?>? = null

}
