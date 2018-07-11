package com.aprivate.sean.ohcg;

public class Global {
    public static ScoreBoardItemAdapter getRecordAdapter() {
        return recordAdapter;
    }

    public static void setRecordAdapter(ScoreBoardItemAdapter adapter) {
        recordAdapter = adapter;
    }

    private static ScoreBoardItemAdapter recordAdapter;

    public static boolean isNewGame() {
        return newGame;
    }

    public static void setNewGame(boolean newGame) {
        Global.newGame = newGame;
    }

    private static boolean newGame;

}
