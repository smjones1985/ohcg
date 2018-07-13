package com.aprivate.sean.ohcg;

public class Calculate {
    public static int CalculateFirstHand(int playerCount){
       return  54 / playerCount;
    }

    public static int CalculateWithEstablishedcardsDealt(int playerCount, int handCount){
        int maxPossible = CalculateFirstHand(playerCount);
        int handsOneColumn = handCount % 10;
        if(handsOneColumn <= 4){
            return handsOneColumn;
        } else if(handsOneColumn >= maxPossible){
            return maxPossible;
        } else{
            return handsOneColumn;
        }

    }
}
