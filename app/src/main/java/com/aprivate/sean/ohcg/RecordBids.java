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

    private TextView mTextMessage;
    private int currentIndex = 0;
    private int maxIndex = 1;
    private int handCount = 0;
    private int totalBidsForCurrentHand = 0;
    private int cardsToDealForNewHand;
    private int dealerIndex = -1;
    private boolean endOfHand;

    private List<ScoreBoardItem> scoreBoardItems;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ScoreBoardItem currentPlayer = (ScoreBoardItem) Global.getRecordAdapter().getItem(currentIndex);
            EditText playerBid = (EditText) findViewById(R.id.bidInputField);
            recordBid(currentPlayer, String.valueOf(playerBid.getText()));
            CheckBox dealerBox = (CheckBox) findViewById(R.id.dealerCheckBox);
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
            TextView playerNameField = (TextView) findViewById(R.id.recordBidPlayerDisplay);
            playerBid.setText(playerItem.getCurrentBid());
            playerNameField.setText(playerItem.getPlayerName());
            if(currentIndex == dealerIndex){
                dealerBox.setChecked(true);
            }else{
                dealerBox.setChecked(false);
            }
            return true;
        }
    };

    @Override
    public void onBackPressed(){
        if(dealerIndex <  0){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("A dealer must be set");
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else if(totalBidsForCurrentHand == cardsToDealForNewHand){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            ScoreBoardItem dealer = (ScoreBoardItem)Global.getRecordAdapter().getItem(dealerIndex);
            dlgAlert.setMessage(String.format("%s cannot bid %s. Please, change bid.", dealer.getPlayerName(), dealer.getCurrentBid()));
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else {
//            String overUnderStatus = "Under";
//            if(totalBidsForCurrentHand > cardsToDealForNewHand){
//                overUnderStatus = "Over";
//            }
//            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//            dlgAlert.setMessage(String.format("The current hand is an %s", overUnderStatus));
//            dlgAlert.setTitle("Information");
//            dlgAlert.setCancelable(true);
//            dlgAlert.create().show();

            Intent output = new Intent();
            output.putExtra("dealCount", cardsToDealForNewHand);
            output.putExtra("handCount", handCount);
            setResult(RESULT_OK, output);
            finish();
        }
    }

    private void recordBid(ScoreBoardItem currentPlayer, String bid) {
        currentPlayer.setCurrentBid(bid);
        totalBidsForCurrentHand += Integer.valueOf(bid);
        TextView tricksOut = (TextView) findViewById(R.id.tricksOutValueText);
        tricksOut.setText(String.valueOf(totalBidsForCurrentHand));
        Global.getRecordAdapter().update(currentPlayer, currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_bids);

        List<ScoreBoardItem> players = loadPlayersAndGetExtras(savedInstanceState);
        players.stream().forEach(x -> x.setCurrentBid("0"));
        ScoreBoardItem playerItem = players.get(currentIndex);
        populateLayoutFields(playerItem);
    }

    private void populateLayoutFields(ScoreBoardItem playerItem) {
        TextView dealCountObjectForRecordBids =  (TextView) findViewById(R.id.recordBidsDealCountTextView);
        dealCountObjectForRecordBids.setText(String.valueOf(cardsToDealForNewHand));
        TextView playerNameField = (TextView) findViewById(R.id.recordBidPlayerDisplay);
        playerNameField.setText(playerItem.getPlayerName());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private List<ScoreBoardItem> loadPlayersAndGetExtras(Bundle savedInstanceState) {
        maxIndex = Global.getRecordAdapter().getCount() - 1;
        List<ScoreBoardItem> players = Global.getRecordAdapter().getScoreBoardItems();
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        endOfHand = extras.getBoolean("endOfHand");
        if(!endOfHand) {
            if (!extras.getBoolean("newGame")) {
                handCount = Integer.valueOf(extras.getString("handCount")) + 1;
                cardsToDealForNewHand = Calculate.CalculateWithEstablishedCardsDealt(maxIndex + 1, handCount);

            } else {
                handCount = 1;
                cardsToDealForNewHand = Calculate.CalculateFirstHand(maxIndex + 1);
            }
        }
        return players;
    }


}
