package com.aprivate.sean.ohcg

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class NewPlayers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_players)
        val addPlayerButtonObj = findViewById<View>(R.id.addPlayer) as Button
        addPlayerButtonObj.setOnClickListener { view -> addPlayer(addPlayerButtonObj) }

        val finishedAddingButtonObj = findViewById<View>(R.id.finishedAddingButton) as Button
        finishedAddingButtonObj.setOnClickListener { view -> onFinished(finishedAddingButtonObj) }
    }

    private fun onFinished(finishedAddingButtonObj: Button) {
        onBackPressed()
    }

    private fun addPlayer(addPlayerButtonObj: Button) {
        val playerNameTextBox = findViewById<View>(R.id.editPlayerName) as EditText
        val name = playerNameTextBox.text.toString()
        if (name != null && !name.equals("", ignoreCase = true) && !Global.recordAdapter!!.scoreBoardItems.stream().anyMatch { x -> x.playerName!!.equals(name, ignoreCase = true) }) {
            val newPlayer = ScoreBoardItem()
            newPlayer.currentBid = "0"
            newPlayer.boardRank = 0
            newPlayer.currentPoints = 0
            newPlayer.playerName = name
            newPlayer.order = Global.recordAdapter!!.count + 1
            Global.recordAdapter!!.add(newPlayer)
            val playerAddedLabelObj = findViewById<View>(R.id.playerAddedLabel) as TextView
            playerAddedLabelObj.text = "$name has been added!"
            playerAddedLabelObj.visibility = View.VISIBLE
            playerNameTextBox.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(playerNameTextBox.windowToken, 0)
        } else {
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage("Invalid player name. Please, try again.")
            dlgAlert.setTitle("Error")
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()
        }
    }

}
