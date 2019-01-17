package com.aprivate.sean.ohcg

import java.io.Serializable
import java.util.HashMap
import java.util.Random

class ScoreBoardItem : Serializable {

    var idNumber: Int = 0


    var playerName: String? = null

    var currentBid: String? = null
    var currentPoints: Int = 0

    var currentTricksTaken: Int = 0
    var boardRank: Int = 0

    private var recordedHands: HashMap<Int, RecordedHand>? = null

    var order: Int = 0

    var isActiveInGame: Boolean = false

    init {
        idNumber = random.nextInt(Integer.MAX_VALUE)
        isActiveInGame = true
    }

    fun getRecordedHands(): HashMap<Int, RecordedHand> {
        return if (recordedHands == null) {
            HashMap()
        } else recordedHands!!
    }

    fun recordHand(record: RecordedHand) {
        if (recordedHands == null) {
            recordedHands = HashMap()
        }
        recordedHands!![record.hand] = record
    }

    companion object {
        private val random = Random()
    }
}
