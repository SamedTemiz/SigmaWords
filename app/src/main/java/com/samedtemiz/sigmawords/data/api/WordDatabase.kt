package com.samedtemiz.sigmawords.data.api

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samedtemiz.sigmawords.data.dao.WordDao
import com.samedtemiz.sigmawords.data.model.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {

    abstract val dao: WordDao
}