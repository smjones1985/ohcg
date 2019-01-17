package com.aprivate.sean.ohcg

//import android.arch.persistence.room.Entity

import java.io.Serializable

//@Entity
class GameState : Serializable {
    var isGameInProgress: Boolean = false
    var handCount: Int = 0

    var handState: HandState? = null

    var dealCount: Int = 0
    var currentDealer: ScoreBoardItem? = null

    var currentHandState: CurrentHandState? = null

    init {
        handState = HandState.BeginningOfHand
    }
}
