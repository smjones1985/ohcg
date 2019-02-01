package com.aprivate.sean.ohcg

import android.os.Handler
import com.aprivate.sean.ohcg.dataBase.DbWorkerThread
import com.aprivate.sean.ohcg.dataBase.GameStateDb
import com.aprivate.sean.ohcg.dataBase.GameStateEntity
import com.fasterxml.jackson.databind.ObjectMapper



class Utility {
    fun convertToEntity(x: GameState): GameStateEntity {
        val entity = GameStateEntity()
        entity.uid = x.uid.toString()
        val mapper = ObjectMapper()
        entity.gameStateObject = mapper.writeValueAsString(x)
        return entity
    }



}
