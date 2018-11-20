package com.aprivate.sean.ohcg;

import java.util.HashMap;
import java.util.Random;

public class ScoreBoardItem {
    private static Random random = new Random();

    public ScoreBoardItem(){
        setIdNumber(random.nextInt(Integer.MAX_VALUE));
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    private int idNumber;


    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(String currentBid) {
        this.currentBid = currentBid;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getBoardRank() {
        return boardRank;
    }

    public void setBoardRank(int boardRank) {
        this.boardRank = boardRank;
    }

    private String currentBid;
    private int currentPoints;

    public int getCurrentTricksTaken() {
        return currentTricksTaken;
    }

    public void setCurrentTricksTaken(int currentTricksTaken) {
        this.currentTricksTaken = currentTricksTaken;
    }

    private int currentTricksTaken;
    private int boardRank;

    public HashMap<Integer, RecordedHand> getRecordedHands() {
        if (recordedHands == null) {
            return new HashMap<>();
        }
        return recordedHands;
    }

    private HashMap<Integer, RecordedHand> recordedHands;

    public void recordHand(RecordedHand record) {
        if (recordedHands == null) {
            recordedHands = new HashMap<>();
        }
        recordedHands.put(record.getHand(), record);
    }

    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
