package com.aprivate.sean.ohcg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.aprivate.sean.ohcg.dataBase.DbWorkerThread
import com.aprivate.sean.ohcg.dataBase.GameStateDb
import com.aprivate.sean.ohcg.dataBase.GameStateEntity
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var gameState: GameState = GameState()
    private var gsDb: GameStateDb? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
//    private val mUiHandler = Handler()


    private var previousGames: List<GameStateEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val startGameButtonObj = findViewById<View>(R.id.startGameButton) as Button
        startGameButtonObj.setOnClickListener { view -> onStartGameClick(startGameButtonObj) }

        val changePlayersButtonObj = findViewById<View>(R.id.changePlayersButton) as Button
        changePlayersButtonObj.setOnClickListener { view -> onAddPlayersClick(changePlayersButtonObj) }

        val startEndHandButtonObj = findViewById<View>(R.id.startEndHandButton) as Button
        startEndHandButtonObj.setOnClickListener { view -> onStartHandClick(startEndHandButtonObj) }

        val editHandButtonObj = findViewById<View>(R.id.editHandButton) as Button
        editHandButtonObj.setOnClickListener { view -> onEditHandClick(editHandButtonObj) }

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        Global.recordAdapter = ScoreBoardItemAdapter(this, ArrayList())
        val recordsView = findViewById<View>(R.id.scoreBoardList) as ListView
        recordsView.adapter = Global.recordAdapter
        recordsView.itemsCanFocus = true
        recordsView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, EditPlayer::class.java)
            intent.putExtra("player", i)
            startActivityForResult(intent, Global.EDIT_ORDER)
        }

        gsDb = GameStateDb.getInstance(applicationContext)
        insertGameStateInDb(Utility().convertToEntity(gameState))
        fetchGameStatesFromDb()

    }

    private fun fetchGameStatesFromDb() {
        val task = Runnable {
            previousGames =
                    gsDb?.gameStateDao()?.getAll()!!
        }
        mDbWorkerThread.postTask(task)
    }

    private fun insertGameStateInDb(gameState: GameStateEntity) {
        val task = Runnable { gsDb?.gameStateDao()?.insertAll(gameState) }
        mDbWorkerThread.postTask(task)
    }


    private fun onEditHandClick(editHandButtonObj: Button) {
        gameState!!.handState = HandState.EditHand
        //whether starting or ending the hand, want to go ahead and use record bids and just alter behavior based on flow.
        val intent = Intent(this, RecordBids::class.java)
        intent.putExtra("gameState", gameState)
        startActivityForResult(intent, Global.EDIT_HAND)
    }

    private fun onStartHandClick(startEndHandButtonObj: Button) {
        val activityId: Int
        if (gameState!!.handState != HandState.BeginningOfHand) {
            activityId = Global.END_HAND
        } else {
            gameState!!.handCount = gameState!!.handCount + 1
            activityId = Global.START_HAND
        }
        //whether starting or ending the hand, want to go ahead and use record bids and just alter behavior based on flow.
        val intent = Intent(this, RecordBids::class.java)
        intent.putExtra("gameState", gameState)
        startActivityForResult(intent, activityId)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun onStartGameClick(button: View) {
        val startGameButtonObj = button as Button

        if (!gameState!!.isGameInProgress) {
            gameState!!.isGameInProgress = true
            val recordCount = Global.recordAdapter!!.count
            if (recordCount < 2) {
                val dlgAlert = AlertDialog.Builder(this)
                dlgAlert.setMessage("You must have at least two players")
                dlgAlert.setTitle("Error")
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()
            } else {
                startGameButtonObj.setText(R.string.endGameStr)
                onStartHandClick(findViewById(R.id.startEndHandButton))
            }
        } else {
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage("Are you sure you want to end the game?")
            dlgAlert.setTitle("Confirmation")
            dlgAlert.setCancelable(true)
            dlgAlert.setPositiveButton("Yes") { dialogInterface, i ->
                startGameButtonObj.setText(R.string.startGameStr)
                gameState!!.isGameInProgress = false
            }
            dlgAlert.setNegativeButton("No") { dialogInterface, i -> }
            dlgAlert.create().show()
            //calculate and display winner
            //store result locally

        }
    }

    fun onAddPlayersClick(button: View) {
        val intent = Intent(this, NewPlayers::class.java)
        intent.putExtra("gameState", gameState)
        startActivity(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val extras = data.extras
        if (requestCode == Global.EDIT_ORDER) {
            return
        }
        val retrievedState = extras!!.getSerializable("gameState")

        if (retrievedState != null) {
            gameState = retrievedState as GameState
        }
        val startEndHandButton = findViewById<Button>(R.id.startEndHandButton)
        val editHandButton = findViewById<Button>(R.id.editHandButton)
        val overUnderLabel = findViewById<TextView>(R.id.labelUnderOrOver)

        when (requestCode) {
            Global.EDIT_HAND, Global.START_HAND -> {
                if (resultCode == Activity.RESULT_OK) {
                    gameState!!.handState = HandState.EndOfHand
                    startEndHandButton.setText(R.string.endHandStr)
                    editHandButton.visibility = View.VISIBLE
                    val amountOverUnder = gameState!!.dealCount - gameState!!.currentHandState!!.totalBidsForCurrentHand
                    val overUnderText: String
                    if (amountOverUnder > 0) {
                        overUnderText = amountOverUnder.toString() + " under"
                    } else {
                        overUnderText = (amountOverUnder * -1).toString() + " over"
                    }
                    overUnderLabel.text = overUnderText
                }
            }
            Global.END_HAND -> {
                if (resultCode == Activity.RESULT_OK) {
                    startEndHandButton.setText(R.string.beginHandStr)
                    gameState!!.handState = HandState.BeginningOfHand
                    editHandButton.visibility = View.INVISIBLE
                    overUnderLabel.text = ""
                }
            }
        }

        val uiElementsUpdate = {
            val handCountScreenObj = findViewById<View>(R.id.handCount) as TextView
            handCountScreenObj.text = gameState!!.handCount.toString()

            val dealCountScreenObj = findViewById<View>(R.id.dealCountTextForMain) as TextView
            dealCountScreenObj.text = gameState!!.dealCount.toString()

        }
        uiElementsUpdate.run {  }

    }

}
