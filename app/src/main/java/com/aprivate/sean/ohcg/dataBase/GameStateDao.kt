package com.aprivate.sean.ohcg.dataBase

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao
interface GameStateDao {

    @Query("SELECT * FROM gameStates")
    fun getAll(): List<GameStateEntity>

//    @Query("SELECT * FROM gamestateentity ORDER BY date_started DESC LIMIT 1")
//    fun findByMostRecent(): GameState

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg gameStates: GameStateEntity)

    @Delete
    fun delete(gameState: GameStateEntity)
}