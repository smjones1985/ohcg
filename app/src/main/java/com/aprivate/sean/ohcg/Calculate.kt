package com.aprivate.sean.ohcg

object Calculate {
    fun calculateFirstHand(playerCount: Int): Int {
        var firstHand = 10
        if (54 / playerCount < 10) {
            firstHand = 54 / playerCount
        }
        return firstHand
    }

    fun calculateWithEstablishedCardsDealt(playerCount: Int, handCount: Int): Int {
        val maxPossible = calculateFirstHand(playerCount)
        var handsOneColumn = handCount % 10
        val movingDown = handCount / 10 % 2 == 0

        if (movingDown) {
            handsOneColumn = 10 - (handsOneColumn - 1)
        } else {
            if (handsOneColumn == 0 || handsOneColumn == 1) {
                return 1
            }
        }

        return if (handsOneColumn > maxPossible) maxPossible else handsOneColumn
    }

    fun calculatePointsEarned(bid: Int, tricksTaken: Int): Int {
        return if (bid != tricksTaken) {
            tricksTaken - 5
        } else tricksTaken + 10
    }


}
