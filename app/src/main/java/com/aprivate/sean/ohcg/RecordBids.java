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

    private GameState gameState;
    private CurrentHandState currentHandState;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ScoreBoardItem currentPlayer = currentHandState.getPlayerOrderMapToIndex().get(currentHandState.getCurrentOrderNumber());
            if(gameState.getHandState() != HandState.EndOfHand) {
                recordBid(currentPlayer, String.valueOf(playerBid.getText()));
            }else{
                recordTricks(currentPlayer, String.valueOf(playerBid.getText()));
            }

            if(dealerBox.isChecked()){
                gameState.setCurrentDealer(currentPlayer.getIdNumber());
            }


            int currentOrderNumber = currentHandState.getCurrentOrderNumber();
            switch (item.getItemId()) {
                case R.id.navigation_dashboard_finish:
                    onBackPressed();
                    break;
                case R.id.navigation_dashboard_previous:
                    if(currentOrderNumber - 1 < 1){
                        currentOrderNumber = Global.getRecordAdapter().getCount();
                    }else{
                        currentOrderNumber--;
                    }
                    break;
                case R.id.navigation_dashboard_next:
                    if(currentOrderNumber + 1 > Global.getRecordAdapter().getCount()){
                        currentOrderNumber = 1;
                    }else{
                        currentOrderNumber++;
                    }
                    break;
            }

            currentHandState.setCurrentOrderNumber(currentOrderNumber);
            ScoreBoardItem playerItem = currentHandState.getPlayerOrderMapToIndex().get(currentOrderNumber);
            if(gameState.getHandState() != HandState.EndOfHand) {
                playerBid.setText(playerItem.getCurrentBid());
            }else{
                playerBid.setText(String.valueOf(playerItem.getCurrentTricksTaken()));
            }
            playerNameField.setText(playerItem.getPlayerName());
            dealerBox.setChecked(playerItem.getIdNumber() == gameState.getCurrentDealer());
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
        recordedHand.setHand(gameState.getHandCount());
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
        if(gameState.getHandState() != HandState.EndOfHand) {
            if (gameState.getCurrentDealer() < 0) {
                showAlert("A dealer must be set", "Error");
                return;

            } else if (currentHandState.getTotalBidsForCurrentHand() == gameState.getDealCount()) {
                ScoreBoardItem dealer = Global.getRecordAdapter().getById(gameState.getCurrentDealer());
                showAlert(String.format("%s cannot bid %s. Please, change bid.", dealer.getPlayerName(), dealer.getCurrentBid()), "Error");
                return;

            }
        } else{

            int totalTricksTaken = players.stream().mapToInt(ScoreBoardItem::getCurrentTricksTaken).sum();
            if(gameState.getDealCount() != totalTricksTaken){
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
        gameState.setCurrentHandState(currentHandState);
        Intent output = new Intent();
        output.putExtra("gameState", gameState);
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
        currentHandState.setTotalBidsForCurrentHand(getCurrentBids());
        tricksOutField.setText(String.valueOf(currentHandState.getTotalBidsForCurrentHand()));
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
        currentHandState = new CurrentHandState();
        initializeScreenObjects();
        loadPlayersAndGetExtras(savedInstanceState);

        players.stream().forEach(x -> {
            if(gameState.getHandState() == HandState.BeginningOfHand) {
                x.setCurrentBid("0");
            } else {
                x.setCurrentTricksTaken(Integer.parseInt(x.getCurrentBid()));
                recordTricks(x, x.getCurrentBid());
            }


            if(currentHandState.playerOrderMapToIndex == null){
                currentHandState.playerOrderMapToIndex = new HashMap<>();
            }
            currentHandState.playerOrderMapToIndex.put(x.getOrder(), x);
        });

        currentHandState.setTotalBidsForCurrentHand(getCurrentBids());

        ScoreBoardItem playerItem = getFirstPlayerToBid();
        updateLabels();
        populateLayoutFields(playerItem);
    }

    private ScoreBoardItem getFirstPlayerToBid() {
        int firstToBidIndex = 1;
        ScoreBoardItem dealer = gameState.getCurrentDealer() > 0 ? currentHandState.playerOrderMapToIndex.get(gameState.getCurrentDealer()) : null;
        if(dealer != null) {
            if (dealer.getOrder() + 1 <= players.size()){
                firstToBidIndex = dealer.getOrder() + 1;
            }
        }
        return currentHandState.playerOrderMapToIndex.get(firstToBidIndex);
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
        String title = gameState.getHandState() == HandState.EndOfHand ? "End of Hand" : "Record Bids";
        String bidLabelContent = gameState.getHandState() == HandState.EndOfHand ? "Tricks:" : "Bid:";
        if(gameState.getHandState() == HandState.EndOfHand ){
            tricksOutLabel.setText("Tricks Taken:");
        } else{
            tricksOutLabel.setText("Tricks Out:");
        }
        this.setTitle(title);
        bidLabel.setText(bidLabelContent);


    }

    private void populateLayoutFields(ScoreBoardItem playerItem) {
        dealCountObjectForRecordBids.setText(String.valueOf(gameState.getDealCount()));
        playerNameField.setText(playerItem.getPlayerName());
        playerBid.setText(String.valueOf(playerItem.getCurrentBid()));
        dealerBox.setChecked(playerItem.getIdNumber() == gameState.getCurrentDealer());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadPlayersAndGetExtras(Bundle savedInstanceState) {
        players = Global.getRecordAdapter().getScoreBoardItems();
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        gameState = (GameState) extras.getSerializable("gameState");

        gameState.setDealCount(Calculate.calculateWithEstablishedCardsDealt(players.size(), gameState.getHandCount()));
        if(gameState.getDealCount() * players.size() == 54){
            showAlert("Jokers are in! Deal them all!", "Notification");
        }else{
            showAlert("Deal " + gameState.getDealCount(), "Notification");
        }
    }


}
