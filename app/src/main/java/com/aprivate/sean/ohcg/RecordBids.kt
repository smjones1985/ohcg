package com.aprivate.sean.ohcg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import java.util.*

class RecordBids : AppCompatActivity() {

    private var dealCountObjectForRecordBids: TextView? = null
    private var playerNameField: TextView? = null
    private var playerBid: EditText? = null
    private var navigation: BottomNavigationView? = null
    private var tricksOutLabel: TextView? = null
    private var tricksOutField: TextView? = null
    private var bidLabel: TextView? = null
    private var dealerBox: CheckBox? = null

    private var gameState: GameState? = null
    private var currentHandState: CurrentHandState? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val currentPlayer = currentHandState!!.playerOrderMapToIndex?.get(currentHandState!!.currentOrderNumber)
        if (gameState!!.handState != HandState.EndOfHand) {
            recordBid(currentPlayer, playerBid!!.text.toString())
        } else {
            recordTricks(currentPlayer, playerBid!!.text.toString())
        }

        if (dealerBox!!.isChecked) {
            gameState!!.currentDealer = currentPlayer
        }

        var currentOrderNumberIndex: Int
        var playerItem: ScoreBoardItem?
        do {
            currentOrderNumberIndex = sortedOrderList!!.indexOf(currentHandState!!.currentOrderNumber)
            when (item.itemId) {
                R.id.navigation_dashboard_finish -> saveAndExit()
                R.id.navigation_dashboard_previous -> if (currentOrderNumberIndex - 1 < 0) {
                    currentOrderNumberIndex = sortedOrderList!!.size - 1
                } else {
                    currentOrderNumberIndex--
                }
                R.id.navigation_dashboard_next -> if (currentOrderNumberIndex + 1 >= sortedOrderList!!.size) {
                    currentOrderNumberIndex = 0
                } else {
                    currentOrderNumberIndex++
                }
            }
            val indexOfCurrentPlayer = sortedOrderList!![currentOrderNumberIndex]
            currentHandState!!.currentOrderNumber = indexOfCurrentPlayer
            playerItem = currentHandState!!.playerOrderMapToIndex?.get(indexOfCurrentPlayer)


        } while (playerItem == null || !playerItem.isActiveInGame)

        if (gameState!!.handState != HandState.EndOfHand) {
            playerBid!!.setText(playerItem.currentBid)
        } else {
            playerBid!!.setText(playerItem.currentTricksTaken.toString())
        }
        playerNameField!!.text = playerItem.playerName
        dealerBox!!.isChecked = playerItem === gameState!!.currentDealer
        true
    }
    private var players: List<ScoreBoardItem>? = null
    private var recordedHands: HashMap<UUID, RecordedHand>? = null
    private var buttonHashMap: HashMap<String, Button>? = null
    private var inActivePlayers: List<ScoreBoardItem>? = null
    private var sortedOrderList: MutableList<Int>? = null

    private val totalTricksTaken: Int
        get() {
            var totalTricksTaken = 0
            for (player in players!!) {
                totalTricksTaken += player.currentTricksTaken
            }

            return totalTricksTaken
        }

    private val currentBids: Int
        get() {
            var currentBids = 0
            for (player in players!!) {
                currentBids += Integer.parseInt(player.currentBid)
            }
            return currentBids
        }

    private val firstPlayerToBid: ScoreBoardItem?
        get() {
            val dealer = gameState!!.currentDealer
            val firstToBid: ScoreBoardItem?
            var firstToBidIndex = 0
            if (dealer != null) {
                firstToBidIndex = sortedOrderList!!.indexOf(dealer.order) + 1
                if (firstToBidIndex >= sortedOrderList!!.size) {
                    firstToBidIndex = 0
                }
            }
            firstToBid = currentHandState!!.playerOrderMapToIndex?.get(sortedOrderList!![firstToBidIndex])

            return firstToBid
        }


    private fun recordTricks(currentPlayer: ScoreBoardItem?, tricksTaken: String?) {
        if (recordedHands == null) {
            recordedHands = HashMap()
        }
        val recordedHand = RecordedHand()
        recordedHand.bid = Integer.valueOf(currentPlayer?.currentBid)
        recordedHand.hand = gameState!!.handCount
        recordedHand.tricksTaken = Integer.valueOf(tricksTaken)
        recordedHands!![currentPlayer!!.idNumber] = recordedHand
        currentPlayer.currentTricksTaken = recordedHand.tricksTaken
        tricksOutField!!.text = totalTricksTaken.toString()
        Global.recordAdapter!!.update(currentPlayer)
    }


    fun saveAndExit() {
        if (gameState!!.handState != HandState.EndOfHand) {
            if (gameState!!.currentDealer == null) {
                showAlert("A dealer must be set", "Error")
                return

            } else if (currentHandState!!.totalBidsForCurrentHand == currentHandState!!.dealCount) {
                val dealer = gameState!!.currentDealer
                showAlert(String.format("%s cannot bid %s. Please, change bid.", dealer!!.playerName, dealer.currentBid), "Error")
                return

            }
        } else {

            val totalTricksTaken = players!!.map{it.currentTricksTaken}.sum()
            if (currentHandState!!.dealCount != totalTricksTaken) {
                showAlert("Tricks taken cannot equal the number of tricks bid", "Error")
                return
            }

            for (player in players!!) {

                val result = recordTricksFinal(player)
                if (!result) {
                    showAlert("Error recording tricks for " + player.playerName!!, "Error")
                    return
                }
            }
        }
        gameState!!.currentHandState = currentHandState
        gameState!!.dealCount = currentHandState!!.dealCount
        val output = Intent()
        output.putExtra("gameState", gameState)
        setResult(Activity.RESULT_OK, output)
        finish()

    }

    override fun onBackPressed() {
        val output = Intent()
        output.putExtra("gameState", gameState)
        setResult(Activity.RESULT_CANCELED, output)
        finish()
    }

    private fun showAlert(message: String, title: String) {
        val dlgAlert = AlertDialog.Builder(this)
        dlgAlert.setMessage(message)
        dlgAlert.setTitle(title)
        dlgAlert.setCancelable(true)
        dlgAlert.create().show()
    }


    private fun recordTricksFinal(currentPlayer: ScoreBoardItem): Boolean {
        val recordedHand = (recordedHands as java.util.Map<Int, RecordedHand>).getOrDefault(currentPlayer.idNumber, null)
                ?: return false
        var currentPoints = currentPlayer.currentPoints
        if (currentPlayer.isActiveInGame) {
            val pointChange = Calculate.calculatePointsEarned(recordedHand.bid, recordedHand.tricksTaken)
            currentPoints += pointChange
        }
        currentPlayer.currentPoints = currentPoints
        recordedHand.pointsAtEndOfHand = currentPoints
        currentPlayer.recordHand(recordedHand)
        Global.recordAdapter!!.update(currentPlayer)
        return true
    }

    private fun recordBid(currentPlayer: ScoreBoardItem?, bid: String) {
        currentPlayer?.currentBid = bid
        currentHandState!!.totalBidsForCurrentHand = currentBids
        tricksOutField!!.text = currentHandState!!.totalBidsForCurrentHand.toString()
        Global.recordAdapter!!.update(currentPlayer!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_bids)
        currentHandState = CurrentHandState()
        initializeScreenObjects()
        loadPlayersAndGetExtras(savedInstanceState)
        sortedOrderList = ArrayList()
        players!!.stream().forEach { x ->
            if (gameState!!.handState == HandState.BeginningOfHand) {
                x.currentBid = "0"
            } else {
                x.currentTricksTaken = Integer.parseInt(x.currentBid)
                recordTricks(x, x.currentBid)
            }
            if (x.isActiveInGame) {
                sortedOrderList!!.add(x.order)
            }
            currentHandState!!.playerOrderMapToIndex?.set(x.order, x)
        }

        sortedOrderList!!.sort()

        currentHandState!!.totalBidsForCurrentHand = currentBids

        val playerItem = firstPlayerToBid
        currentHandState!!.currentOrderNumber = playerItem!!.order
        updateLabels()
        populateLayoutFields(playerItem)
    }

    private fun initializeScreenObjects() {
        dealCountObjectForRecordBids = findViewById(R.id.recordBidsDealCountTextView)
        navigation = findViewById(R.id.navigation)
        playerBid = findViewById(R.id.bidInputField)
        playerNameField = findViewById(R.id.recordBidPlayerDisplay)
        tricksOutLabel = findViewById(R.id.tricksOutLabel)
        bidLabel = findViewById(R.id.bidLabel)
        tricksOutField = findViewById(R.id.tricksOutValueText)
        dealerBox = findViewById(R.id.dealerCheckBox)
        if (buttonHashMap == null) {
            buttonHashMap = HashMap()
        }
        buttonHashMap!!["0"] = findViewById(R.id.button0)
        buttonHashMap!!["1"] = findViewById(R.id.button1)
        buttonHashMap!!["2"] = findViewById(R.id.button2)
        buttonHashMap!!["3"] = findViewById(R.id.button3)
        buttonHashMap!!["4"] = findViewById(R.id.button4)
        buttonHashMap!!["5"] = findViewById(R.id.button5)
        buttonHashMap!!["6"] = findViewById(R.id.button6)
        buttonHashMap!!["7"] = findViewById(R.id.button7)
        buttonHashMap!!["8"] = findViewById(R.id.button8)
        buttonHashMap!!["9"] = findViewById(R.id.button9)
        buttonHashMap!!["c"] = findViewById(R.id.buttonC)

        for (button in buttonHashMap!!.values) {
            button.setOnClickListener { view -> onNumberButtonClick(button) }

        }
    }

    private fun onNumberButtonClick(button: Button) {
        if (button.text.toString().equals("C", ignoreCase = true)) {
            playerBid!!.setText("0")
        } else {
            playerBid!!.setText(button.text.toString())
        }

    }

    private fun updateLabels() {
        val title = if (gameState!!.handState == HandState.EndOfHand) "End of Hand" else "Record Bids"
        val bidLabelContent = if (gameState!!.handState == HandState.EndOfHand) "Tricks:" else "Bid:"
        if (gameState!!.handState == HandState.EndOfHand) {
            tricksOutLabel!!.text = "Tricks Taken:"
        } else {
            tricksOutLabel!!.text = "Tricks Out:"
        }
        this.title = title
        bidLabel!!.text = bidLabelContent


    }

    private fun populateLayoutFields(playerItem: ScoreBoardItem) {
        dealCountObjectForRecordBids!!.text = currentHandState!!.dealCount.toString()
        playerNameField!!.text = playerItem.playerName
        playerBid!!.setText(playerItem.currentBid.toString())
        dealerBox!!.isChecked = playerItem === gameState!!.currentDealer
        navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun loadPlayersAndGetExtras(savedInstanceState: Bundle?) {
        players = Global.recordAdapter!!.scoreBoardItems
        inActivePlayers = players!!.filter { x -> !x.isActiveInGame }
        var extras: Bundle? = null
        if (savedInstanceState == null) {
            extras = intent.extras
        }
        gameState = extras!!.getSerializable("gameState") as GameState
        currentHandState!!.dealCount = Calculate.calculateWithEstablishedCardsDealt(players!!.size - inActivePlayers!!.size, gameState!!.handCount)
        if (gameState!!.handState != HandState.EndOfHand) {
            if (currentHandState!!.dealCount * (players!!.size - inActivePlayers!!.size) == 54) {
                showAlert("Jokers are in! Deal them all!", "Notification")
            } else {
                showAlert("Deal " + currentHandState!!.dealCount, "Notification")
            }
        }
    }


}
