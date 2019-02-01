package com.aprivate.sean.ohcg.dataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import java.lang.Exception

@Database(entities = arrayOf(GameStateEntity::class), version = 1)
abstract class GameStateDb : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: GameStateDb? = null

        fun getInstance(context: Context): GameStateDb {
            if(instance == null){
                instance = buildDatabase(context)
            }
            return instance!!
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): GameStateDb? {
            return try {
                Room.databaseBuilder(context, GameStateDb::class.java, "ohcg.db").build()
            }catch (exception: Exception){
                null
            }
        }
    }
}