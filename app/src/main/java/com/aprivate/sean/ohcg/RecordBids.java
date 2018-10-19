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
    private int dealerIndex = -1;
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
                dealerIndex = currentIndex;
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
                playerBid.setText(playerItem.getCurrentTricksTaken());
            }
            playerNameField.setText(playerItem.getPlayerName());
            dealerBox.setChecked(currentIndex == dealerIndex);
            return true;
        }
    };
    private List<ScoreBoardItem> players;

    private void recordTricks(ScoreBoardItem currentPlayer, String tricksTaken) {
        RecordedHand recordedHand = new RecordedHand();
        recordedHand.setBid(Integer.valueOf(currentPlayer.getCurrentBid()));
        recordedHand.setHand(handCount);
        recordedHand.setTricksTaken(Integer.valueOf(tricksTaken));
        currentPlayer.setCurrentTricksTaken(recordedHand.getTricksTaken());
        int currentPoints = Integer.parseInt(currentPlayer.getCurrentPoints());
        int pointChange = Calculate.calculatePointsEarned(recordedHand.getBid(), recordedHand.getTricksTaken());
        currentPoints += pointChange;
        currentPlayer.setCurrentPoints(String.valueOf(currentPoints));
        recordedHand.setPointsAtEndOfHand(currentPoints);
        currentPlayer.getRecordedHands().add(recordedHand);
        Global.getRecordAdapter().update(currentPlayer, currentIndex);
    }


    @Override
    public void onBackPressed(){
        if(!isEndOfHand) {
            if (dealerIndex < 0) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("A dealer must be set");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } else if (totalBidsForCurrentHand == cardsToDealForNewHand) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                ScoreBoardItem dealer = (ScoreBoardItem) Global.getRecordAdapter().getItem(dealerIndex);
                dlgAlert.setMessage(String.format("%s cannot bid %s. Please, change bid.", dealer.getPlayerName(), dealer.getCurrentBid()));
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        } else{
            int totalTricksTaken = players.stream().mapToInt(ScoreBoardItem::getCurrentTricksTaken).sum();
            if(totalBidsForCurrentHand == totalTricksTaken){
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Tricks taken cannot equal the number of tricks bid");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        }

        Intent output = new Intent();
        output.putExtra("dealCount", cardsToDealForNewHand);
        output.putExtra("handCount", handCount);
        setResult(RESULT_OK, output);
        finish();

    }

    private void recordBid(ScoreBoardItem currentPlayer, String bid) {
        currentPlayer.setCurrentBid(bid);
        totalBidsForCurrentHand += Integer.valueOf(bid);
        tricksOutField.setText(String.valueOf(totalBidsForCurrentHand));
        Global.getRecordAdapter().update(currentPlayer, currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_bids);
        initializeScreenObjects();

        players = loadPlayersAndGetExtras(savedInstanceState);
        if(!isEndOfHand){
            players.stream().forEach(x -> x.setCurrentBid("0"));
        }else{
            players.stream().forEach(x ->
            {
                x.setCurrentTricksTaken(0);
                totalBidsForCurrentHand += Integer.parseInt(x.getCurrentBid());
            });


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
            tricksOutField.setActivated(false);
            tricksOutLabel.setActivated(false);
        } else{
            tricksOutField.setActivated(true);
            tricksOutLabel.setActivated(true);
        }
        this.setTitle(title);
        bidLabel.setText(bidLabelContent);


    }

    private void populateLayoutFields(ScoreBoardItem playerItem) {
        dealCountObjectForRecordBids.setText(String.valueOf(cardsToDealForNewHand));
        playerNameField.setText(playerItem.getPlayerName());
        playerBid.setText(String.valueOf(playerItem.getCurrentBid()));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private List<ScoreBoardItem> loadPlayersAndGetExtras(Bundle savedInstanceState) {
        maxIndex = Global.getRecordAdapter().getCount() - 1;
        List<ScoreBoardItem> players = Global.getRecordAdapter().getScoreBoardItems();
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        isEndOfHand = extras.getBoolean("isEndOfHand");
        if(!isEndOfHand) {
            if (!extras.getBoolean("newGame")) {
                handCount = Integer.valueOf(extras.getString("handCount")) + 1;
                cardsToDealForNewHand = Calculate.calculateWithEstablishedCardsDealt(maxIndex + 1, handCount);

            } else {
                handCount = 1;
                cardsToDealForNewHand = Calculate.calculateFirstHand(maxIndex + 1);
            }
        }else{
            handCount = Integer.valueOf(extras.getString("handCount"));
        }


        return players;
    }


}
