package com.aprivate.sean.ohcg;

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

}
