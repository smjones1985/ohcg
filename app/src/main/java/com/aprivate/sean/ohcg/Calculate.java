package com.aprivate.sean.ohcg;

public class Calculate {
    public static int CalculateFirstHand(int playerCount){
       int firstHand = 10;
       if(54 / playerCount < 10){
           firstHand = 54 / playerCount;
       }
       return firstHand;
    }

    public static int CalculateWithEstablishedCardsDealt(int playerCount, int handCount){
        int maxPossible = CalculateFirstHand(playerCount);
        int handsOneColumn = handCount % 10;
        if(handsOneColumn == 0){
            handsOneColumn = 1;
        }
        return handsOneColumn > maxPossible ? maxPossible : handsOneColumn;
    }


}
