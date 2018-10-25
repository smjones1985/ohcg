package com.aprivate.sean.ohcg;

import org.junit.Test;

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
}