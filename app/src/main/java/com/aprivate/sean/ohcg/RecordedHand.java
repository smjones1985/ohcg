package com.aprivate.sean.ohcg;

public class RecordedHand {
    private int bid;

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    private int hand;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getTricksTaken() {
        return tricksTaken;
    }

    public void setTricksTaken(int tricksTaken) {
        this.tricksTaken = tricksTaken;
    }

    public int getPointsAtEndOfHand() {
        return pointsAtEndOfHand;
    }

    public void setPointsAtEndOfHand(int pointsAtEndOfHand) {
        this.pointsAtEndOfHand = pointsAtEndOfHand;
    }

    private int tricksTaken;
    private int pointsAtEndOfHand;
}
