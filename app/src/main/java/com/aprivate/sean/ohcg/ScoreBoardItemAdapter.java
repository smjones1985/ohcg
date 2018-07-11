package com.aprivate.sean.ohcg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ScoreBoardItemAdapter extends BaseAdapter {

    private Context scoarBoardContext;
    private List<ScoreBoardItem> scoreBoardItems;

    public ScoreBoardItemAdapter(Context context, List<ScoreBoardItem> items) {
        scoreBoardItems = items;
        scoarBoardContext = context;
    }

    public void add(ScoreBoardItem record) {
        scoreBoardItems.add(record);
        notifyDataSetChanged();
    }

    public void update(ScoreBoardItem updateRecord, int index){
        scoreBoardItems.set(index, updateRecord);
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

        if (view ==null){
            LayoutInflater recordInflater = (LayoutInflater)
                    scoarBoardContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.score_board, null);

            holder = new RecordViewHolder();
            holder.Name = (TextView) view.findViewById(R.id.name);
            holder.CurrentBid = (TextView) view.findViewById(R.id.currentBid);
            holder.TotalPts = (TextView) view.findViewById(R.id.totalPoints);
            holder.Rank = (TextView) view.findViewById(R.id.rank);
            view.setTag(holder);
        }else {
            holder = (RecordViewHolder) view.getTag();
        }

        ScoreBoardItem scoreBoardItem = (ScoreBoardItem) getItem(i);
        holder.Name.setText(scoreBoardItem.getPlayerName());
        holder.CurrentBid.setText(scoreBoardItem.getCurrentBid());
        holder.TotalPts.setText(scoreBoardItem.getCurrentPoints());
        holder.Rank.setText(scoreBoardItem.getBoardRank());
        return view;
    }

    private static class RecordViewHolder {

        public TextView Name;
        public TextView CurrentBid;
        public TextView TotalPts;
        public TextView Rank;
    }
}
