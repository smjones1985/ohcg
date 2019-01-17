package com.aprivate.sean.ohcg

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView

class EditPlayer : AppCompatActivity() {

    private var player: ScoreBoardItem? = null
    private var currentOrder: TextView? = null
    private var currentScore: TextView? = null
    private var activeSwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_player)
        var extras: Bundle? = null
        if (savedInstanceState == null) {
            extras = intent.extras
        }
        val playerIndex = extras!!.getInt("player")

        player = Global.recordAdapter!!.getItem(playerIndex) as ScoreBoardItem

        val saveButton = findViewById<Button>(R.id.button_save_changes)
        saveButton.setOnClickListener { view -> onSaveChanges(saveButton) }

        activeSwitch = findViewById(R.id.switchActive)
        activeSwitch!!.isChecked = player!!.isActiveInGame
        currentOrder = findViewById(R.id.edit_order)
        currentScore = findViewById(R.id.edit_current_score)

        currentOrder!!.text = player!!.order.toString()
        currentScore!!.text = player!!.currentPoints.toString()

    }

    private fun onSaveChanges(saveButton: Button) {
        val activeStatus = activeSwitch!!.isChecked
        val order = Integer.valueOf(currentOrder!!.text.toString())
        Global.recordAdapter!!.updateOrderAndPoints(this!!.player!!, order, Integer.valueOf(currentScore!!.text.toString()), activeStatus)
    }

    override fun onBackPressed() {
        val output = Intent()
        setResult(Activity.RESULT_OK, output)
        finish()
    }


}
