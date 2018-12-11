package com.aprivate.sean.ohcg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startGameButtonObj = (Button) findViewById(R.id.startGameButton);
        startGameButtonObj.setOnClickListener((view) -> {
            onStartGameClick(startGameButtonObj);
        });

        Button changePlayersButtonObj = (Button) findViewById(R.id.changePlayersButton);
        changePlayersButtonObj.setOnClickListener((view) -> {
            onAddPlayersClick(changePlayersButtonObj);
        });

        Button startEndHandButtonObj = (Button) findViewById(R.id.startEndHandButton);
        startEndHandButtonObj.setOnClickListener((view) -> {
            onStartHandClick(startEndHandButtonObj);
        });

        Button editHandButtonObj = (Button) findViewById(R.id.editHandButton);
        editHandButtonObj.setOnClickListener((view) -> {
            onEditHandClick(editHandButtonObj);
        });
        gameState = new GameState();

        gameState.setRecordAdapter(new ScoreBoardItemAdapter(this, new ArrayList<ScoreBoardItem>()));
        final ListView recordsView = (ListView) findViewById(R.id.scoreBoardList);
        recordsView.setAdapter(gameState.getRecordAdapter());
        recordsView.setItemsCanFocus(true);
        recordsView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, EditPlayer.class);
            intent.putExtra("player", i);
            startActivityForResult(intent, 1);
        });
        gameState.setCurrentDealer(-1);
    }

    private void onEditHandClick(Button editHandButtonObj) {
        gameState.setEditOfHand(true);
        //whether starting or ending the hand, want to go ahead and use record bids and just alter behavior based on flow.
        Intent intent = new Intent(this, RecordBids.class);
        intent.putExtra("gameState", gameState);
        startActivityForResult(intent, Global.EDIT_HAND);
    }

    private void onStartHandClick(Button startEndHandButtonObj) {
        int activityId;
        if (gameState.isHandInProgress()) {
            activityId = Global.END_HAND;
        }
        else {
            gameState.setHandCount(gameState.getHandCount() + 1);
            activityId = Global.START_HAND;
        }
        //whether starting or ending the hand, want to go ahead and use record bids and just alter behavior based on flow.
        Intent intent = new Intent(this, RecordBids.class);
        intent.putExtra("gameState", gameState);
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

        if(!gameState.isGameInProgress()) {
            gameState.setGameInProgress(true);
            int recordCount = gameState.getRecordAdapter().getCount();
            if (recordCount < 2) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("You must have at least two players");
                dlgAlert.setTitle("Error");
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } else {
                startGameButtonObj.setText(R.string.endGameStr);
                onStartHandClick(findViewById(R.id.startEndHandButton));
            }
        }else{
            //calculate and display winner
            //store result locally
            startGameButtonObj.setText(R.string.startGameStr);
        }
    }

    public void onAddPlayersClick(View button){
        Intent intent = new Intent(this, NewPlayers.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        Serializable retrievedState = extras.getSerializable("gameState");

        if(retrievedState != null){
            gameState = (GameState)retrievedState;
        }
        Button startEndHandButton = findViewById(R.id.startEndHandButton);
        Button editHandButton = findViewById(R.id.editHandButton);

        switch(requestCode) {
            case (Global.START_HAND) : {
                if (resultCode == Activity.RESULT_OK) {
                    gameState.setHandInProgress(true);
                    startEndHandButton.setText(R.string.endHandStr);
                    editHandButton.setVisibility(View.VISIBLE);
                }
                break;
            }
            case (Global.END_HAND) :{
                startEndHandButton.setText(R.string.beginHandStr);
                gameState.setHandInProgress(false);
                editHandButton.setVisibility(View.INVISIBLE);
                break;
            }
        }

        Runnable uiElementsUpdate = () -> {
            TextView handCountScreenObj = (TextView) findViewById(R.id.handCount);
            handCountScreenObj.setText(String.valueOf(gameState.getHandCount()));

            TextView dealCountScreenObj = (TextView) findViewById(R.id.dealCountTextForMain);
            dealCountScreenObj.setText(String.valueOf(gameState.getDealCount()));

        };
        uiElementsUpdate.run();

    }

}
