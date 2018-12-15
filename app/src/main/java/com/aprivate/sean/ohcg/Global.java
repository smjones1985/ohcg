package com.aprivate.sean.ohcg;


public class Global {
    public static final int EDIT_HAND = 2;
    public static final int START_HAND = 0;
    public static final int END_HAND = 1;
    public static final int EDIT_ORDER = 3;

    public static ScoreBoardItemAdapter getRecordAdapter() {
        return recordAdapter;
    }

    public static void setRecordAdapter(ScoreBoardItemAdapter adapter) {
        recordAdapter = adapter;
    }

    private static ScoreBoardItemAdapter recordAdapter;
}
