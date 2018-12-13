package com.aprivate.sean.ohcg;

import java.io.Serializable;

public class GameState implements Serializable {

    public GameState(){
        setHandState(HandState.BeginningOfHand);
    }
    private boolean gameInProgress;
    private int handCount;

    public HandState getHandState() {
        return handState;
    }

    public void setHandState(HandState handState) {
        this.handState = handState;
    }

    private HandState handState;

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }


    public int getHandCount() {
        return handCount;
    }

    public void setHandCount(int handCount) {
        this.handCount = handCount;
    }

    public int getDealCount() {
        return dealCount;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    public int getCurrentDealer() {
        return currentDealer;
    }

    public void setCurrentDealer(int currentDealer) {
        this.currentDealer = currentDealer;
    }

    private int dealCount;
    private int currentDealer;

    public CurrentHandState getCurrentHandState() {
        return currentHandState;
    }

    public void setCurrentHandState(CurrentHandState currentHandState) {
        this.currentHandState = currentHandState;
    }

    private CurrentHandState currentHandState;
}
