package com.aprivate.sean.ohcg;

public class Calculate {
    public static int calculateFirstHand(int playerCount){
       int firstHand = 10;
       if(54 / playerCount < 10){
           firstHand = 54 / playerCount;
       }
       return firstHand;
    }

    public static int calculateWithEstablishedCardsDealt(int playerCount, int handCount){
        int maxPossible = calculateFirstHand(playerCount);
        int handsOneColumn = handCount % 10;
        if(handsOneColumn == 0){
            handsOneColumn = 1;
        }
        return handsOneColumn > maxPossible ? maxPossible : handsOneColumn;
    }

    public static int calculatePointsEarned(int bid, int tricksTaken){
        if(bid != tricksTaken){
            return tricksTaken - 5;
        }
        return tricksTaken + 10;
    }



}
