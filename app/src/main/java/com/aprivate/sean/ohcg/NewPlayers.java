package com.aprivate.sean.ohcg;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    private void onFinished(Button finishedAddingButtonObj) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void addPlayer(Button addPlayerButtonObj) {
        EditText playerNameTextBox = (EditText) findViewById(R.id.editPlayerName);
        String name = playerNameTextBox.getText().toString();
        if(name != null && !name.equalsIgnoreCase("") && !Global.getRecordAdapter().getScoreBoardItems().stream().anyMatch(x -> x.getPlayerName().equalsIgnoreCase(name))) {
            ScoreBoardItem newPlayer = new ScoreBoardItem();
            newPlayer.setCurrentBid("0");
            newPlayer.setBoardRank(0);
            newPlayer.setCurrentPoints(0);
            newPlayer.setPlayerName(name);
            newPlayer.setOrder(Global.getRecordAdapter().getCount() + 1);
            Global.getRecordAdapter().add(newPlayer);
            TextView playerAddedLabelObj = (TextView) findViewById(R.id.playerAddedLabel);
            playerAddedLabelObj.setText(name + " has been added!");
            playerAddedLabelObj.setVisibility(View.VISIBLE);
            playerNameTextBox.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(playerNameTextBox.getWindowToken(), 0);
        }else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Invalid player name. Please, try again.");
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

}
