package com.aprivate.sean.ohcg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class RecordBids extends AppCompatActivity {

    private TextView mTextMessage;
    private int currentIndex = 0;
    private int maxIndex = 1;
    private int cardsDealt = 0;
    private int totalBidsForCurrentHand = 0;

    private List<ScoreBoardItem> scoreBoardItems;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ScoreBoardItem currentPlayer = (ScoreBoardItem) Global.getRecordAdapter().getItem(currentIndex);
            EditText playerBid = (EditText) findViewById(R.id.bidInputField);
//            boolean isDealer = ((CheckBox) findViewById(R.id.dealerCheckBox)).isChecked();
//            if(isDealer){
//                playerItem.setDealerStatus(true);
//            }
//            int bidAttempt = Integer.valueOf(String.valueOf(playerBid.getText()));
//
//            if(totalBidsForCurrentHand + bidAttempt == cardsDealt &&
//                    recordBid());
            recordBid(currentPlayer, String.valueOf(playerBid));

            switch (item.getItemId()) {
                case R.id.navigation_dashboard_finish:

                    return true;
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
            return true;
        }
    };

    private void recordBid(ScoreBoardItem currentPlayer, String bid) {
        currentPlayer.setCurrentBid(bid);
        Global.getRecordAdapter().update(currentPlayer, currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_bids);
        maxIndex = Global.getRecordAdapter().getCount() - 1;
        TextView dealCountLabelObject =  (TextView) findViewById(R.id.dealCountLabel);
        if(dealCountLabelObject.getText() != null && !Global.isNewGame()) {
            cardsDealt = Integer.valueOf(String.valueOf(dealCountLabelObject));
        }else{
            cardsDealt = 0;
        }
        List<ScoreBoardItem> players = Global.getRecordAdapter().getScoreBoardItems();
        players.stream().forEach(x -> x.setCurrentBid("0"));

        ScoreBoardItem playerItem = players.get(currentIndex);
        TextView playerNameField = (TextView) findViewById(R.id.recordBidPlayerDisplay);

        playerNameField.setText(playerItem.getPlayerName());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
