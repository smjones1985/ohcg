package com.aprivate.sean.ohcg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
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

    private int currentOrderNumber = 1;
    private int maxIndex = 1;
    private int handCount = 0;
    private int cardsToDealForNewHand;
    private int dealerId = -1;
    private boolean isEndOfHand;
    private boolean isEditHand;
    private int totalBidsForCurrentHand;

    HashMap<Integer, ScoreBoardItem> playerOrderMapToIndex;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ScoreBoardItem currentPlayer = playerOrderMapToIndex.get(currentOrderNumber);
            if(!isEndOfHand || isEditHand) {
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
                    if(currentOrderNumber - 1 < 1){
                        currentOrderNumber = maxIndex;
                    }else{
                        currentOrderNumber--;
                    }
                    break;
                case R.id.navigation_dashboard_next:
                    if(currentOrderNumber + 1 > maxIndex){
                        currentOrderNumber = 1;
                    }else{
                        currentOrderNumber++;
                    }
                    break;
            }

            ScoreBoardItem playerItem = playerOrderMapToIndex.get(currentOrderNumber);
            if(!isEndOfHand || isEditHand) {
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
    private HashMap<String, Button> buttonHashMap;



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
        if(!isEndOfHand || isEditHand) {
            if (dealerId < 0) {
                showAlert("A dealer must be set", "Error");
                return;

            } else if (totalBidsForCurrentHand == cardsToDealForNewHand) {
                ScoreBoardItem dealer = (ScoreBoardItem) Global.getRecordAdapter().getItem(dealerId);
                showAlert(String.format("%s cannot bid %s. Please, change bid.", dealer.getPlayerName(), dealer.getCurrentBid()), "Error");
                return;

            }
        } else{

            int totalTricksTaken = players.stream().mapToInt(ScoreBoardItem::getCurrentTricksTaken).sum();
            if(cardsToDealForNewHand != totalTricksTaken){
                showAlert("Tricks taken cannot equal the number of tricks bid", "Error");
                return;
            }

            for (ScoreBoardItem player: players
                 ) {

                boolean result = recordTricksFinal(player);
                if(!result){
                    showAlert("Error recording tricks for " + player.getPlayerName(), "Error");
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

    private void showAlert(String message, String title) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
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

        players.stream().forEach(x -> {
            if(!isEndOfHand && !isEditHand) {
                x.setCurrentBid("0");
            } else if(!isEditHand){
                x.setCurrentTricksTaken(Integer.parseInt(x.getCurrentBid()));
                recordTricks(x, x.getCurrentBid());
            }

            if(playerOrderMapToIndex == null){
                playerOrderMapToIndex = new HashMap<>();
            }
            playerOrderMapToIndex.put(x.getOrder(), x);
        });

        totalBidsForCurrentHand = getCurrentBids();

        ScoreBoardItem playerItem = getFirstPlayerToBid();
        updateLabels();
        populateLayoutFields(playerItem);
    }

    private ScoreBoardItem getFirstPlayerToBid() {
        ScoreBoardItem dealer = dealerId > 0 ? playerOrderMapToIndex.get(dealerId) : null;
        if(dealer == null || dealer.getOrder() + 1 > players.size()) {
            return playerOrderMapToIndex.get(1);
        }
        return playerOrderMapToIndex.get(dealerId + 1);
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
        if(buttonHashMap == null){
            buttonHashMap = new HashMap<>();
        }
        buttonHashMap.put("0", findViewById(R.id.button0));
        buttonHashMap.put("1", findViewById(R.id.button1));
        buttonHashMap.put("2", findViewById(R.id.button2));
        buttonHashMap.put("3", findViewById(R.id.button3));
        buttonHashMap.put("4", findViewById(R.id.button4));
        buttonHashMap.put("5", findViewById(R.id.button5));
        buttonHashMap.put("6", findViewById(R.id.button6));
        buttonHashMap.put("7", findViewById(R.id.button7));
        buttonHashMap.put("8", findViewById(R.id.button8));
        buttonHashMap.put("9", findViewById(R.id.button9));
        buttonHashMap.put("c", findViewById(R.id.buttonC));

        for (Button button : buttonHashMap.values()) {
            button.setOnClickListener((view) -> {
                onNumberButtonClick(button);
            });

        }
    }

    private void onNumberButtonClick(Button button) {
        if(button.getText().toString().equalsIgnoreCase("C")){
            playerBid.setText("0");
        }else{
            playerBid.setText(button.getText().toString());
        }

    }

    private void updateLabels() {
        String title = isEndOfHand && !isEditHand? "End of Hand" : "Record Bids";
        String bidLabelContent = isEndOfHand && !isEditHand? "Tricks:" : "Bid:";
        if(isEndOfHand && !isEditHand){
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
        maxIndex = Global.getRecordAdapter().getCount();
        players = Global.getRecordAdapter().getScoreBoardItems();
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        isEndOfHand = extras.getBoolean(getString(R.string.extraKey_isHandInProgress));
        isEditHand = extras.getBoolean(getString(R.string.extraKey_editHand));
        handCount = extras.getInt("handCount");
        cardsToDealForNewHand = Calculate.calculateWithEstablishedCardsDealt(players.size(), handCount);
        if(cardsToDealForNewHand * players.size() == 54){
            showAlert("Jokers are in! Deal them all!", "Notification");
        }else{
            showAlert("Deal " + cardsToDealForNewHand, "Notification");
        }
        if(isEndOfHand) {
            dealerId = extras.getInt("currentDealer");
        }
    }


}
