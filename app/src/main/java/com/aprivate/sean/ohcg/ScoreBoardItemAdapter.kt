package com.aprivate.sean.ohcg

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.util.Collections

class ScoreBoardItemAdapter(private val scoreBoardContext: Context, val scoreBoardItems: MutableList<ScoreBoardItem>) : BaseAdapter() {

    fun add(record: ScoreBoardItem) {
        scoreBoardItems.add(record)
        notifyDataSetChanged()
    }

    fun establishRankings(players: List<ScoreBoardItem>) {
        Collections.sort(players) { player1, player2 -> if (player1.currentPoints < player2.currentPoints) 1 else if (player1.currentPoints == player2.currentPoints) 0 else -1 }
        var rank = 1
        for (player in players) {
            player.boardRank = rank++
        }
    }

    fun update(updateRecord: ScoreBoardItem) {
        for (i in scoreBoardItems.indices) {
            if (scoreBoardItems[i].idNumber == updateRecord.idNumber) {
                scoreBoardItems[i] = updateRecord
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return scoreBoardItems.size
    }

    override fun getItem(i: Int): Any {
        return scoreBoardItems[i]
    }

//    fun getScoreBoardItems(): List<ScoreBoardItem> {
//        return scoreBoardItems
//    }


    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        val holder: RecordViewHolder

        if (i == 0) {
            establishRankings(scoreBoardItems)
        }

        if (view == null) {
            val recordInflater = scoreBoardContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = recordInflater.inflate(R.layout.score_board, null)

            holder = RecordViewHolder()
            holder.Name = view!!.findViewById<View>(R.id.name) as TextView
            holder.CurrentBid = view.findViewById<View>(R.id.currentBid) as TextView
            holder.TotalPts = view.findViewById<View>(R.id.totalPoints) as TextView
            holder.Rank = view.findViewById<View>(R.id.rank) as TextView
            holder.Order = view.findViewById(R.id.order)
            view.tag = holder
        } else {
            holder = view.tag as RecordViewHolder
        }

        val scoreBoardItem = getItem(i) as ScoreBoardItem
        holder.Name!!.text = scoreBoardItem.playerName
        holder.CurrentBid!!.text = scoreBoardItem.currentBid.toString()
        holder.TotalPts!!.text = scoreBoardItem.currentPoints.toString()
        holder.Rank!!.text = scoreBoardItem.boardRank.toString()
        holder.Order!!.text = scoreBoardItem.order.toString()
        return view
    }

    fun updateOrderAndPoints(player: ScoreBoardItem, newOrderNumber: Int, newPoints: Int, activeStatus: Boolean) {
        val previousOrder = player.order
        for (item in scoreBoardItems) {
            if (item.idNumber == player.idNumber) {
                item.order = newOrderNumber
                item.currentPoints = newPoints
                item.isActiveInGame = activeStatus
            } else if (item.order < previousOrder && item.order >= newOrderNumber) {
                item.order = item.order + 1
                update(item)
            } else if (item.order > previousOrder && item.order <= newOrderNumber) {
                item.order = item.order - 1
            } else {
                continue
            }
            update(item)
        }
    }

    private class RecordViewHolder {

        var Name: TextView? = null
        var CurrentBid: TextView? = null
        var TotalPts: TextView? = null
        var Rank: TextView? = null
        var Order: TextView? = null
    }
}
