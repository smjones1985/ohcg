package com.aprivate.sean.ohcg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class ScoreBoardItemAdapter extends BaseAdapter {

    private Context scoreBoardContext;
    private List<ScoreBoardItem> scoreBoardItems;

    public ScoreBoardItemAdapter(Context context, List<ScoreBoardItem> items) {
        scoreBoardItems = items;
        scoreBoardContext = context;
    }

    public void add(ScoreBoardItem record) {
        scoreBoardItems.add(record);
        notifyDataSetChanged();
    }

    public void establishRankings(List<ScoreBoardItem> players) {
        Collections.sort(players, (player1, player2) -> player1.getCurrentPoints() < player2.getCurrentPoints() ? 1 : player1.getCurrentPoints() == player2.getCurrentPoints() ? 0 : -1);
        int rank = 1;
        for (ScoreBoardItem player : players){
            player.setBoardRank(rank++);
        }
    }

    public void update(ScoreBoardItem updateRecord){
        for (int i = 0; i < scoreBoardItems.size(); i++) {
            if(scoreBoardItems.get(i).getIdNumber() == updateRecord.getIdNumber()){
                scoreBoardItems.set(i, updateRecord);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scoreBoardItems.size();
    }
    @Override
    public Object getItem(int i) {
        return scoreBoardItems.get(i);
    }

    public List<ScoreBoardItem> getScoreBoardItems() {
        return scoreBoardItems;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordViewHolder holder;

        if(i == 0){
            establishRankings(scoreBoardItems);
        }

        if (view ==null){
            LayoutInflater recordInflater = (LayoutInflater)
                    scoreBoardContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.score_board, null);

            holder = new RecordViewHolder();
            holder.Name = (TextView) view.findViewById(R.id.name);
            holder.CurrentBid = (TextView) view.findViewById(R.id.currentBid);
            holder.TotalPts = (TextView) view.findViewById(R.id.totalPoints);
            holder.Rank = (TextView) view.findViewById(R.id.rank);
            holder.Order = view.findViewById(R.id.order);
            view.setTag(holder);
        }else {
            holder = (RecordViewHolder) view.getTag();
        }

        ScoreBoardItem scoreBoardItem = (ScoreBoardItem) getItem(i);
        holder.Name.setText(scoreBoardItem.getPlayerName());
        holder.CurrentBid.setText(String.valueOf(scoreBoardItem.getCurrentBid()));
        holder.TotalPts.setText(String.valueOf(scoreBoardItem.getCurrentPoints()));
        holder.Rank.setText(String.valueOf(scoreBoardItem.getBoardRank()));
        holder.Order.setText(String.valueOf(scoreBoardItem.getOrder()));
        return view;
    }

    public void updateOrderAndPoints(ScoreBoardItem player, int newOrderNumber, int newPoints) {
        int previousOrder = player.getOrder();
        for (ScoreBoardItem item : scoreBoardItems) {
            if(item.getIdNumber() == player.getIdNumber()){
                item.setOrder(newOrderNumber);
                item.setCurrentPoints(newPoints);
            }else if(item.getOrder() < previousOrder && item.getOrder() >= newOrderNumber){
                item.setOrder(item.getOrder() + 1);
                update(item);
            } else if(item.getOrder() > previousOrder && item.getOrder() <= newOrderNumber){
                item.setOrder(item.getOrder() - 1);
            }else{
                continue;
            }
            update(item);
        }
    }

    private static class RecordViewHolder {

        public TextView Name;
        public TextView CurrentBid;
        public TextView TotalPts;
        public TextView Rank;
        public TextView Order;
    }
}
