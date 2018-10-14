package com.aprivate.sean.ohcg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean handStarted;
    private boolean gameStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startGameButtonObj = (Button) findViewById(R.id.startEndGameButton);
        startGameButtonObj.setOnClickListener((view) -> {
            onStartGameClick(startGameButtonObj);
        });

        Button changePlayersButtonObj = (Button) findViewById(R.id.changePlayersButton);
        changePlayersButtonObj.setOnClickListener((view) -> {
            onAddplayersClick(changePlayersButtonObj);
        });

        Button startEndHandButtonObj = (Button) findViewById(R.id.startEndHandButton);
        startEndHandButtonObj.setOnClickListener((view) -> {
            onStartHandClick(startEndHandButtonObj);
        });

        Global.setRecordAdapter(new ScoreBoardItemAdapter(this, new ArrayList<ScoreBoardItem>()));
        final ListView recordsView = (ListView) findViewById(R.id.scoreBoardList);
        recordsView.setAdapter(Global.getRecordAdapter());

    }

    private void onStartHandClick(Button startEndHandButtonObj) {
        String handCount = ((TextView) findViewById(R.id.handCount)).getText().toString();
        int activityId = 0;
        if(!handStarted){
            handStarted = true;
            startEndHandButtonObj.setText(R.string.endHandStr);
            activityId = Global.END_HAND;

        }else {
            handStarted = false;
            startEndHandButtonObj.setText(R.string.beginHandStr);
            activityId = Global.START_HAND;

        }
        //whether starting or ending the hand, want to go ahead and use record bids and just alter behavior based on flow.
        Intent intent = new Intent(this, RecordBids.class);
        intent.putExtra("handCount", handCount);
        intent.putExtra("newGame", gameStarted);
        intent.putExtra("endHand", handStarted);
        startActivityForResult(intent, activityId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartGameClick(View button){
        Button startGameButtonObj = (Button) button;
        if(!startGameButtonObj.getText().toString().equalsIgnoreCase(getString(R.string.endGameStr))) {
            int recordCount = Global.getRecordAdapter().getCount();
            if (recordCount < 2) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("You must have at least two players");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } else {
                gameStarted = true;
                startGameButtonObj.setText(R.string.endGameStr);
                onStartHandClick((Button) findViewById(R.id.startEndHandButton));
            }
        }else{
            gameStarted = false;
            //calculate and display winner
            //store result locally
            startGameButtonObj.setText(R.string.startGameStr);
        }
    }

    public void onAddplayersClick(View button){
        if(!handStarted) {
            Intent intent = new Intent(this, NewPlayers.class);
            startActivity(intent);
        }else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You cannot add new players in the middle of a hand.");
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        switch(requestCode) {
            case (Global.START_HAND) : {
                if (resultCode == Activity.RESULT_OK) {
                    int handCount = extras.getInt("handCount");
                    TextView handCountScreenObj = (TextView) findViewById(R.id.handCount);
                    handCountScreenObj.setText(String.valueOf(handCount));

                    int dealCount = extras.getInt("dealCount");
                    TextView dealCountScreenObj = (TextView) findViewById(R.id.dealCountText);
                    dealCountScreenObj.setText(String.valueOf(dealCount));
                }
                break;
            }
            case (Global.END_HAND) :{
                if(resultCode == Activity.RESULT_OK){

                }
            }
        }
    }

}
