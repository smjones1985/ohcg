package com.aprivate.sean.ohcg.dataBase

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "gameStates")
class GameStateEntity : Serializable {
    @PrimaryKey
    var uid: String = ""

    @ColumnInfo(name = "game_state_object")
    var gameStateObject: String = ""
}
