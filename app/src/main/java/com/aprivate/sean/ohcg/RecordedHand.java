package com.aprivate.sean.ohcg;

import java.io.Serializable;
import java.util.Random;

public class RecordedHand implements Serializable {

    private static Random random = new Random();

    public RecordedHand(){
        setIdNumber(random.nextInt(Integer.MAX_VALUE));
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    private int idNumber;

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
