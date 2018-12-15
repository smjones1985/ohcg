package com.aprivate.sean.ohcg;

import java.io.Serializable;
import java.util.HashMap;

public class CurrentHandState implements Serializable {

    HashMap<Integer, ScoreBoardItem> playerOrderMapToIndex;

    public HashMap<Integer, ScoreBoardItem> getPlayerOrderMapToIndex() {
        return playerOrderMapToIndex;
    }

    public void setPlayerOrderMapToIndex(HashMap<Integer, ScoreBoardItem> playerOrderMapToIndex) {
        this.playerOrderMapToIndex = playerOrderMapToIndex;
    }
    private int currentOrderNumber = 1;

    public int getTotalBidsForCurrentHand() {
        return totalBidsForCurrentHand;
    }

    public void setTotalBidsForCurrentHand(int totalBidsForCurrentHand) {
        this.totalBidsForCurrentHand = totalBidsForCurrentHand;
    }

    private int totalBidsForCurrentHand;

    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    public void setCurrentOrderNumber(int currentOrderNumber) {
        this.currentOrderNumber = currentOrderNumber;
    }
}
