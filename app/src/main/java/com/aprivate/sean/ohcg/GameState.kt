package com.aprivate.sean.ohcg

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.util.*

class GameState : Serializable {
    @PrimaryKey
    var uid: UUID

    var isGameInProgress: Boolean = false

    var handCount: Int = 0

    var handState: HandState? = null

    var dealCount: Int = 0

    var currentDealer: ScoreBoardItem? = null

    var currentHandState: CurrentHandState? = null

    var players: List<ScoreBoardItem> = emptyList()

    var dateStarted: LocalDate = LocalDate.now()

    init {
        handState = HandState.BeginningOfHand
        uid = UUID.randomUUID()
    }
}
