package com.aprivate.sean.ohcg;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


public class ExampleUnitTest {

    private int[] expectedCardCountForEachStandardHand = {10, 9,
            8,
            7,
            6,
            5,
            4,
            3,
            2,
            1,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10};


    @Test
    public void calculateStandardPattern(){
        int handCount = 1;
        for (int hand : expectedCardCountForEachStandardHand
             ) {

            int result = Calculate.calculateWithEstablishedCardsDealt(5, handCount);
            assertEquals(result, expectedCardCountForEachStandardHand[handCount - 1]);
            handCount++;

        }
    }

    @Test
    public void calculateCards_lowerPlayerLowHandCount() {
        testCalculate(3, 3);
    }

    @Test
    public void calculateCards_firstHand() {
        int result = Calculate.calculateWithEstablishedCardsDealt(3, 1);
        assertEquals(result, 10);
    }

    @Test
    public void calculateCards_highPlayerLowHand() {
        int result = Calculate.calculateWithEstablishedCardsDealt(9, 2);
        assertEquals(result, 6);
    }

    @Test
    public void calculateCards_highPlayerHighHandCount() {
        int result = Calculate.calculateWithEstablishedCardsDealt(9, 18);
        assertEquals(result, 6);
    }

    @Test
    public void calculateCards_lowPlayerHighHand() {
        testCalculate(3,18);

    }

    @Test
    public void calculateCards_whenHandCountEleven() {
        testCalculate(3,11);
    }

    @Test
    public void calculateCards_whenHandCountTen() {
        testCalculate(3,10);
    }

    private void testCalculate(int playerCount, int handCount){
        int result = Calculate.calculateWithEstablishedCardsDealt(playerCount, handCount);
        assertEquals(expectedCardCountForEachStandardHand[handCount - 1], result);
    }

    @Test
    public void RankTesting(){
        ScoreBoardItem player1 = new ScoreBoardItem();
        ScoreBoardItem player2 = new ScoreBoardItem();
        ScoreBoardItem player3 = new ScoreBoardItem();

        player1.setCurrentPoints(10);
        player2.setCurrentPoints(30);
        player3.setCurrentPoints(20);

        List<ScoreBoardItem> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        establishRankings(players);

        for (ScoreBoardItem player: players
             ) {
            if(player.getCurrentPoints() == 30){
                Assert.assertEquals(1, player.getBoardRank());
            } else if(player.getCurrentPoints() == 20){
                assert player.getBoardRank() == 2;
            }
        }
    }

    public void establishRankings(List<ScoreBoardItem> players) {
        Collections.sort(players, (player1, player2) -> player1.getCurrentPoints() < player2.getCurrentPoints() ? 1 : player1.getCurrentPoints() == player2.getCurrentPoints() ? 0 : -1);
        int rank = 1;
        for (ScoreBoardItem player : players){
            player.setBoardRank(rank++);
        }
    }

    @Test
    public void ReorderTesting(){
        List<ScoreBoardItem> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ScoreBoardItem player = new ScoreBoardItem();
            player.setOrder(i + 1);
            players.add(player);
        }
        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
        adapter.updateOrderAndPoints(players.get(2), 1, 10);
        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
        int previousOrder = 0;
        for (int i = 0; i < items.size(); i++) {
            int orderNumber = items.get(i).getOrder();
            if(i == 0){
                assert orderNumber == 1 && items.get(i).getCurrentPoints() == 10;
            }

            assert orderNumber != previousOrder;
            previousOrder = orderNumber;
        }
    }

    @Test
    public void ReorderTestin2(){
        List<ScoreBoardItem> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ScoreBoardItem player = new ScoreBoardItem();
            player.setOrder(i + 1);
            players.add(player);
        }
        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
        adapter.updateOrderAndPoints(players.get(2), 5, 10);
        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
        int previousOrder = 0;
        for (int i = 0; i < items.size(); i++) {
            int orderNumber = items.get(i).getOrder();
            if(i == 4){
                assert orderNumber == 5 && items.get(i).getCurrentPoints() == 10;
            }

            assert orderNumber != previousOrder;
            previousOrder = orderNumber;
        }
    }

    @Test
    public void ReorderTestin3(){
        List<ScoreBoardItem> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ScoreBoardItem player = new ScoreBoardItem();
            player.setOrder(i + 1);
            players.add(player);
        }
        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
        adapter.updateOrderAndPoints(players.get(0), 6, 10);
        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
        int previousOrder = 0;
        for (int i = 0; i < items.size(); i++) {
            int orderNumber = items.get(i).getOrder();
            if(i == 5){
                assert orderNumber == 6 && items.get(i).getCurrentPoints() == 10;
            }

            assert orderNumber != previousOrder;
            previousOrder = orderNumber;
        }
    }

    @Test
    public void ReorderTestin4(){
        List<ScoreBoardItem> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ScoreBoardItem player = new ScoreBoardItem();
            player.setOrder(i + 1);
            players.add(player);
        }
        ScoreBoardItemAdapter adapter = new ScoreBoardItemAdapter(null, players);
        adapter.updateOrderAndPoints(players.get(5), 1, 10);
        List<ScoreBoardItem> items = adapter.getScoreBoardItems();
        Collections.sort(items, (player1, player2) -> player1.getOrder() < player2.getOrder() ? -1 : player1.getOrder() == player2.getOrder() ? 0 : 1);
        int previousOrder = 0;
        for (int i = 0; i < items.size(); i++) {
            int orderNumber = items.get(i).getOrder();
            if(i == 0){
                assert orderNumber == 1 && items.get(i).getCurrentPoints() == 10;
            }

            assert orderNumber != previousOrder;
            previousOrder = orderNumber;
        }
    }
}