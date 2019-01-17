package com.aprivate.sean.ohcg

import junit.framework.Assert

import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList
import java.util.Collections

import org.junit.Assert.*


class ExampleUnitTest {

    private val expectedCardCountForEachStandardHand = intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)


    @Test
    fun calculateStandardPattern() {
        var handCount = 1
        for (hand in expectedCardCountForEachStandardHand) {

            val result = Calculate.calculateWithEstablishedCardsDealt(5, handCount)
            assertEquals(result.toLong(), expectedCardCountForEachStandardHand[handCount - 1].toLong())
            handCount++

        }
    }

    @Test
    fun calculateCards_lowerPlayerLowHandCount() {
        testCalculate(3, 3)
    }

    @Test
    fun calculateCards_firstHand() {
        val result = Calculate.calculateWithEstablishedCardsDealt(3, 1)
        assertEquals(result.toLong(), 10)
    }

    @Test
    fun calculateCards_highPlayerLowHand() {
        val result = Calculate.calculateWithEstablishedCardsDealt(9, 2)
        assertEquals(result.toLong(), 6)
    }

    @Test
    fun calculateCards_highPlayerHighHandCount() {
        val result = Calculate.calculateWithEstablishedCardsDealt(9, 18)
        assertEquals(result.toLong(), 6)
    }

    @Test
    fun calculateCards_lowPlayerHighHand() {
        testCalculate(3, 18)

    }

    @Test
    @Throws(Exception::class)
    fun serializeTest() {
        ObjectOutputStream(ByteArrayOutputStream()).writeObject(GameState())
    }

    @Test
    fun calculateCards_whenHandCountEleven() {
        testCalculate(3, 11)
    }

    @Test
    fun calculateCards_whenHandCountTen() {
        testCalculate(3, 10)
    }

    private fun testCalculate(playerCount: Int, handCount: Int) {
        val result = Calculate.calculateWithEstablishedCardsDealt(playerCount, handCount)
        assertEquals(expectedCardCountForEachStandardHand[handCount - 1].toLong(), result.toLong())
    }

    @Test
    fun RankTesting() {
        val player1 = ScoreBoardItem()
        val player2 = ScoreBoardItem()
        val player3 = ScoreBoardItem()

        player1.currentPoints = 10
        player2.currentPoints = 30
        player3.currentPoints = 20

        val players = ArrayList<ScoreBoardItem>()
        players.add(player1)
        players.add(player2)
        players.add(player3)

        establishRankings(players)

        for (player in players) {
            if (player.currentPoints == 30) {
                Assert.assertEquals(1, player.boardRank)
            } else if (player.currentPoints == 20) {
                assert(player.boardRank == 2)
            }
        }
    }

    fun establishRankings(players: List<ScoreBoardItem>) {
        Collections.sort(players) { player1, player2 -> if (player1.currentPoints < player2.currentPoints) 1 else if (player1.currentPoints == player2.currentPoints) 0 else -1 }
        var rank = 1
        for (player in players) {
            player.boardRank = rank++
        }
    }

    //    @Test
    //    public void ReorderTesting(){
    //        List<ScoreBoardItem> players = new ArrayList<>();
    //        for (int i = 0; i < 6; i++) {
    //            ScoreBoardItem player = new ScoreBoardItem();
    //            player.setOrder(i + 1);
    //            players.add(player);
    //        }
    //        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
    //        adapter.updateOrderAndPoints(players.get(2), 1, 10);
    //        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
    //        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
    //        int previousOrder = 0;
    //        for (int i = 0; i < items.size(); i++) {
    //            int orderNumber = items.get(i).getOrder();
    //            if(i == 0){
    //                assert orderNumber == 1 && items.get(i).getCurrentPoints() == 10;
    //            }
    //
    //            assert orderNumber != previousOrder;
    //            previousOrder = orderNumber;
    //        }
    //    }

    //    @Test
    //    public void ReorderTestin2(){
    //        List<ScoreBoardItem> players = new ArrayList<>();
    //        for (int i = 0; i < 6; i++) {
    //            ScoreBoardItem player = new ScoreBoardItem();
    //            player.setOrder(i + 1);
    //            players.add(player);
    //        }
    //        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
    //        adapter.updateOrderAndPoints(players.get(2), 5, 10);
    //        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
    //        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
    //        int previousOrder = 0;
    //        for (int i = 0; i < items.size(); i++) {
    //            int orderNumber = items.get(i).getOrder();
    //            if(i == 4){
    //                assert orderNumber == 5 && items.get(i).getCurrentPoints() == 10;
    //            }
    //
    //            assert orderNumber != previousOrder;
    //            previousOrder = orderNumber;
    //        }
    //    }
    //
    //    @Test
    //    public void ReorderTestin3(){
    //        List<ScoreBoardItem> players = new ArrayList<>();
    //        for (int i = 0; i < 6; i++) {
    //            ScoreBoardItem player = new ScoreBoardItem();
    //            player.setOrder(i + 1);
    //            players.add(player);
    //        }
    //        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
    //        adapter.updateOrderAndPoints(players.get(0), 6, 10);
    //        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
    //        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
    //        int previousOrder = 0;
    //        for (int i = 0; i < items.size(); i++) {
    //            int orderNumber = items.get(i).getOrder();
    //            if(i == 5){
    //                assert orderNumber == 6 && items.get(i).getCurrentPoints() == 10;
    //            }
    //
    //            assert orderNumber != previousOrder;
    //            previousOrder = orderNumber;
    //        }
    //    }
    //
    //    @Test
    //    public void ReorderTestin4(){
    //        List<ScoreBoardItem> players = new ArrayList<>();
    //        for (int i = 0; i < 6; i++) {
    //            ScoreBoardItem player = new ScoreBoardItem();
    //            player.setOrder(i + 1);
    //            players.add(player);
    //        }
    //        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
    //        adapter.updateOrderAndPoints(players.get(5), 1, 10);
    //        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
    //        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
    //        int previousOrder = 0;
    //        for (int i = 0; i < items.size(); i++) {
    //            int orderNumber = items.get(i).getOrder();
    //            if(i == 0){
    //                assert orderNumber == 1 && items.get(i).getCurrentPoints() == 10;
    //            }
    //
    //            assert orderNumber != previousOrder;
    //            previousOrder = orderNumber;
    //        }
    //    }
}