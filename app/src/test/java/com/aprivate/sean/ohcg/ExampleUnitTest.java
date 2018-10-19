package com.aprivate.sean.ohcg;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void calculateCards_lowerPlayerLowHandCount() {
        int result = Calculate.calculateWithEstablishedCardsDealt(2, 3);
        assertEquals(result, 3);
    }

    @Test
    public void calculateCards_highPlayerLowHand() {
        int result = Calculate.calculateWithEstablishedCardsDealt(9, 3);
        assertEquals(result, 3);
    }

    @Test
    public void calculateCards_highPlayerHighHandCount() {
        int result = Calculate.calculateWithEstablishedCardsDealt(9, 18);
        assertEquals(result, 6);
    }

    @Test
    public void calculateCards_lowPlayerHighHand() {
        int result = Calculate.calculateWithEstablishedCardsDealt(3, 18);
        assertEquals(result, 8);
    }

    @Test
    public void calculateCards_whenHandCountEleven() {
        int result = Calculate.calculateWithEstablishedCardsDealt(3, 11);
        assertEquals(result, 1);
    }

    @Test
    public void calculateCards_whenHandCountTen() {
        int result = Calculate.calculateWithEstablishedCardsDealt(3, 10);
        assertEquals(result, 1);
    }
}