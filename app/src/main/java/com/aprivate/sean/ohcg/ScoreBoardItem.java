package com.aprivate.sean.ohcg;

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
    private String boardRank;
}
