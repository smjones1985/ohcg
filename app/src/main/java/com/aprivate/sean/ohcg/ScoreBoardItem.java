package com.aprivate.sean.ohcg;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardItem {
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

    public String getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    public String getBoardRank() {
        return boardRank;
    }

    public void setBoardRank(String boardRank) {
        this.boardRank = boardRank;
    }

    private String currentBid;
    private String currentPoints;

    public int getCurrentTricksTaken() {
        return currentTricksTaken;
    }

    public void setCurrentTricksTaken(int currentTricksTaken) {
        this.currentTricksTaken = currentTricksTaken;
    }

    private int currentTricksTaken;
    private String boardRank;

    public List<RecordedHand> getRecordedHands() {
        if(recordedHands == null){
            return new ArrayList<>();
        }
        return recordedHands;
    }

    private List<RecordedHand> recordedHands;
}
