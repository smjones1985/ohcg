package com.aprivate.sean.ohcg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EditPlayer extends AppCompatActivity {

    private ScoreBoardItem player;
    private TextView currentOrder;
    private TextView currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        Bundle extras = null;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
        }
        int playerIndex = extras.getInt("player");

        player = (ScoreBoardItem) Global.getRecordAdapter().getItem(playerIndex);

        Button saveButton = findViewById(R.id.button_save_changes);
        saveButton.setOnClickListener((view) -> {
            onSaveChanges(saveButton);
        });


        currentOrder = findViewById(R.id.edit_order);
        currentScore = findViewById(R.id.edit_current_score);

        currentOrder.setText(String.valueOf(player.getOrder()));
        currentScore.setText(String.valueOf(player.getCurrentPoints()));

    }

    private void onSaveChanges(Button saveButton) {
        int order = Integer.valueOf(currentOrder.getText().toString());
        Global.getRecordAdapter().updateOrderAndPoints(player, order, Integer.valueOf(currentScore.getText().toString()));
    }

    @Override
    public void onBackPressed() {
        Intent output = new Intent();
        setResult(RESULT_OK, output);
        finish();
    }
}
