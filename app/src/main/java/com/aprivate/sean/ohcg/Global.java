package com.aprivate.sean.ohcg;

import java.util.HashMap;
import java.util.List;

public class Global {
    public static ScoreBoardItemAdapter getRecordAdapter() {
        return recordAdapter;
    }

    public static void setRecordAdapter(ScoreBoardItemAdapter adapter) {
        recordAdapter = adapter;
    }

    private static ScoreBoardItemAdapter recordAdapter;


    public static final int START_HAND = 0;
    public static final int END_HAND = 1;

    public static List<HashMap<String, ScoreBoardItem>> getTotalScoreForGame() {
        return totalScoreForGame;
    }

    public static void setTotalScoreForGame(List<HashMap<String, ScoreBoardItem>> scoreForGame) {
        Global.totalScoreForGame = scoreForGame;
    }

    private static List<HashMap<String, ScoreBoardItem>> totalScoreForGame;


}
