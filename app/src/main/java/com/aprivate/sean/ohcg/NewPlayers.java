package com.aprivate.sean.ohcg;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NewPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_players);

        Button addPlayerButtonObj = (Button) findViewById(R.id.addPlayer);
        addPlayerButtonObj.setOnClickListener((view) -> {
            addPlayer(addPlayerButtonObj);
        });

        Button finishedAddingButtonObj = (Button) findViewById(R.id.finishedAddingButton);
        finishedAddingButtonObj.setOnClickListener((view) -> {
            onFinished(finishedAddingButtonObj);
        });

    }

    @Override
    public void onBackPressed() {
        //TODO calculate cards to deal
        int playerCount = Global.getRecordAdapter().getCount();

        TextView dealCountLabelObject =  (TextView) findViewById(R.id.dealCountText);
        TextView handCountLabelObject =  (TextView) findViewById(R.id.handCount);
        int cardsToDeal = 10;

        if(dealCountLabelObject != null && dealCountLabelObject.getText() != null) {
            cardsToDeal = Integer.valueOf(String.valueOf(dealCountLabelObject));
        }



        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    private void onFinished(Button finishedAddingButtonObj) {
        onBackPressed();
    }

    private void addPlayer(Button addPlayerButtonObj) {
        EditText playerNameTextBox = (EditText) findViewById(R.id.editPlayerName);
        String name = playerNameTextBox.getText().toString();
        ScoreBoardItem newPlayer = new ScoreBoardItem();
        newPlayer.setCurrentBid("0");
        newPlayer.setBoardRank("0");
        newPlayer.setCurrentPoints("0");
        newPlayer.setPlayerName(name);
        Global.getRecordAdapter().add(newPlayer);
        TextView playerAddedLabelObj = (TextView) findViewById(R.id.playerAddedLabel);
        playerAddedLabelObj.setText(name + " has been added!");
        playerAddedLabelObj.setVisibility(View.VISIBLE);
        playerNameTextBox.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(playerNameTextBox.getWindowToken(), 0);
    }

}
