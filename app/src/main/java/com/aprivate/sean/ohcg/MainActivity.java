package com.aprivate.sean.ohcg;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        Button startGameButtonObj = (Button) findViewById(R.id.startEndGameButton);
        if(String.valueOf(startEndHandButtonObj.getText()).equalsIgnoreCase(String.valueOf(R.string.endGameStr))){
            if(String.valueOf(startEndHandButtonObj.getText()).equalsIgnoreCase(String.valueOf(R.string.endHandStr))){
                startEndHandButtonObj.setText(R.string.beginHandStr);
            }else {
                startEndHandButtonObj.setText(R.string.endHandStr);
            }
        }else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You cannot begin the hand without starting the game");
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
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
        int recordCount = Global.getRecordAdapter().getCount();
        if(recordCount < 2){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You must have at least two players");
            dlgAlert.setTitle("Error");
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }else{
            Button startGameButtonObj = (Button) findViewById(button.getId());
            Global.setNewGame(true);
            Intent intent = new Intent(this, RecordBids.class);
            startActivity(intent);
            startGameButtonObj.setText(R.string.endGameStr);
        }


    }

    public void onAddplayersClick(View button){
        Intent intent = new Intent(this, NewPlayers.class);
        startActivity(intent);

    }
}
