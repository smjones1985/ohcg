package com.aprivate.sean.ohcg;

import java.io.Serializable;

public class GameState implements Serializable {
    private boolean gameInProgress;
    private int handCount;

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

    public boolean isEditOfHand() {
        return editOfHand;
    }

    public void setEditOfHand(boolean editOfHand) {
        this.editOfHand = editOfHand;
    }

    private boolean editOfHand;

    public boolean isHandInProgress() {
        return isHandInProgress;
    }

    public void setHandInProgress(boolean handInProgress) {
        isHandInProgress = handInProgress;
    }

    private boolean isHandInProgress;
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

    public ScoreBoardItemAdapter getRecordAdapter() {
        return recordAdapter;
    }

    public void setRecordAdapter(ScoreBoardItemAdapter adapter) {
        recordAdapter = adapter;
    }

    private ScoreBoardItemAdapter recordAdapter;


}
