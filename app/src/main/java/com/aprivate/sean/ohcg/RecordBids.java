package com.aprivate.sean.ohcg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RecordBids extends AppCompatActivity {

    private TextView dealCountObjectForRecordBids;
    private TextView playerNameField;
    private EditText playerBid;
    private BottomNavigationView navigation;
    private TextView tricksOutLabel;
    private TextView tricksOutField;
    private TextView bidLabel;
    private CheckBox dealerBox;

    private int currentIndex = 0;
    private int maxIndex = 1;
    private int handCount = 0;
    private int cardsToDealForNewHand;
    private int dealerId = -1;
    private boolean isEndOfHand;
    private int totalBidsForCurrentHand;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ScoreBoardItem currentPlayer = (ScoreBoardItem) Global.getRecordAdapter().getItem(currentIndex);
            if(!isEndOfHand) {
                recordBid(currentPlayer, String.valueOf(playerBid.getText()));
            }else{
                recordTricks(currentPlayer, String.valueOf(playerBid.getText()));
            }

            if(dealerBox.isChecked()){
                dealerId = currentPlayer.getIdNumber();
            }

            switch (item.getItemId()) {
                case R.id.navigation_dashboard_finish:
                    onBackPressed();
                    break;
                case R.id.navigation_dashboard_previous:
                    if(currentIndex - 1 < 0){
                        currentIndex = maxIndex;
                    }else{
                        currentIndex--;
                    }
                    break;
                case R.id.navigation_dashboard_next:
                    if(currentIndex + 1 > maxIndex){
                        currentIndex = 0;
                    }else{
                        currentIndex++;
                    }
                    break;
            }

            ScoreBoardItem playerItem = (ScoreBoardItem) Global.getRecordAdapter().getItem(currentIndex);
            if(!isEndOfHand) {
                playerBid.setText(playerItem.getCurrentBid());
            }else{
                playerBid.setText(String.valueOf(playerItem.getCurrentTricksTaken()));
            }
            playerNameField.setText(playerItem.getPlayerName());
            dealerBox.setChecked(playerItem.getIdNumber() == dealerId);
            return true;
        }
    };
    private List<ScoreBoardItem> players;
    private HashMap<Integer, RecordedHand> recordedHands;

    private void recordTricks(ScoreBoardItem currentPlayer, String tricksTaken) {
        if(recordedHands == null) {
            recordedHands = new HashMap<>();
        }
        RecordedHand recordedHand = new RecordedHand();
        recordedHand.setBid(Integer.valueOf(currentPlayer.getCurrentBid()));
        recordedHand.setHand(handCount);
        recordedHand.setTricksTaken(Integer.valueOf(tricksTaken));
        recordedHands.put(currentPlayer.getIdNumber(), recordedHand);
        currentPlayer.setCurrentTricksTaken(recordedHand.getTricksTaken());
        tricksOutField.setText(String.valueOf( getTotalTricksTaken()));
        Global.getRecordAdapter().update(currentPlayer);
    }

    private int getTotalTricksTaken() {
        int totalTricksTaken = 0;
        for (ScoreBoardItem player : players) {
            totalTricksTaken += player.getCurrentTricksTaken();
        }

        return totalTricksTaken;
    }


    @Override
    public void onBackPressed(){
        if(!isEndOfHand) {
            if (dealerId < 0) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("A dealer must be set");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return;

            } else if (totalBidsForCurrentHand == cardsToDealForNewHand) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                ScoreBoardItem dealer = (ScoreBoardItem) Global.getRecordAdapter().getItem(dealerId);
                dlgAlert.setMessage(String.format("%s cannot bid %s. Please, change bid.", dealer.getPlayerName(), dealer.getCurrentBid()));
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return;

            }
        } else{

            int totalTricksTaken = players.stream().mapToInt(ScoreBoardItem::getCurrentTricksTaken).sum();
            if(cardsToDealForNewHand != totalTricksTaken){
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Tricks taken cannot equal the number of tricks bid");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return;
            }

            for (ScoreBoardItem player: players
                 ) {

                boolean result = recordTricksFinal(player);
                if(!result){
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setMessage("Error recording tricks for " + player.getPlayerName());
                    dlgAlert.setTitle("Error");
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    return;
                }
            }
        }

        Intent output = new Intent();
        output.putExtra("dealCount", cardsToDealForNewHand);
        output.putExtra("handCount", handCount);
        output.putExtra("currentDealer", dealerId);
        setResult(RESULT_OK, output);
        finish();

    }



    private boolean recordTricksFinal(ScoreBoardItem currentPlayer) {
        RecordedHand recordedHand = recordedHands.getOrDefault(currentPlayer.getIdNumber(), null);
        if(recordedHand == null){
            return false;
        }
        int currentPoints = currentPlayer.getCurrentPoints();
        int pointChange = Calculate.calculatePointsEarned(recordedHand.getBid(), recordedHand.getTricksTaken());
        currentPoints += pointChange;
        currentPlayer.setCurrentPoints(currentPoints);
        recordedHand.setPointsAtEndOfHand(currentPoints);
        currentPlayer.recordHand(recordedHand);
        Global.getRecordAdapter().update(currentPlayer);
        return true;
    }

    private void recordBid(ScoreBoardItem currentPlayer, String bid) {
        currentPlayer.setCurrentBid(bid);
        totalBidsForCurrentHand = getCurrentBids();
        tricksOutField.setText(String.valueOf(totalBidsForCurrentHand));
        Global.getRecordAdapter().update(currentPlayer);
    }

    private int getCurrentBids() {
        int currentBids = 0;
        for (ScoreBoardItem player : players) {
            currentBids += Integer.parseInt(player.getCurrentBid());
        }
        return currentBids;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_bids);
        initializeScreenObjects();
        loadPlayersAndGetExtras(savedInstanceState);

        if(!isEndOfHand){
            players.stream().forEach(x -> x.setCurrentBid("0"));
        }else{

            players.stream().forEach(x -> {
                    x.setCurrentTricksTaken(Integer.parseInt(x.getCurrentBid()));
                    recordTricks(x, x.getCurrentBid());
            });
            totalBidsForCurrentHand = getCurrentBids();
        }
        ScoreBoardItem playerItem = players.get(currentIndex);
        updateLabels();
        populateLayoutFields(playerItem);
    }

    private void initializeScreenObjects() {
        dealCountObjectForRecordBids =  findViewById(R.id.recordBidsDealCountTextView);
        navigation = findViewById(R.id.navigation);
        playerBid = findViewById(R.id.bidInputField);
        playerNameField = findViewById(R.id.recordBidPlayerDisplay);
        tricksOutLabel = findViewById(R.id.tricksOutLabel);
        bidLabel = findViewById(R.id.bidLabel);
        tricksOutField = findViewById(R.id.tricksOutValueText);
        dealerBox = findViewById(R.id.dealerCheckBox);
    }

    private void updateLabels() {
        String title = isEndOfHand ? "End of Hand" : "Record Bids";
        String bidLabelContent = isEndOfHand ? "Tricks:" : "Bid:";
        if(isEndOfHand){
            tricksOutLabel.setText("Tricks Taken:");
        } else{
            tricksOutLabel.setText("Tricks Out:");
        }
        this.setTitle(title);
        bidLabel.setText(bidLabelContent);


    }

    private void populateLayoutFields(ScoreBoardItem playerItem) {
        dealCountObjectForRecordBids.setText(String.valueOf(cardsToDealForNewHand));
        playerNameField.setText(playerItem.getPlayerName());
        playerBid.setText(String.valueOf(playerItem.getCurrentBid()));
        dealerBox.setChecked(playerItem.getIdNumber() == dealerId);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadPlayersAndGetExtras(Bundle savedInstanceState) {
        maxIndex = Global.getRecordAdapter().getCount() - 1;
        players = Global.getRecordAdapter().getScoreBoardItems();
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        isEndOfHand = extras.getBoolean(getString(R.string.extraKey_isHandInProgress));
        handCount = extras.getInt("handCount");
        cardsToDealForNewHand = Calculate.calculateWithEstablishedCardsDealt(players.size(), handCount);

        if(isEndOfHand) {
            dealerId = extras.getInt("currentDealer");
        }
    }


}
