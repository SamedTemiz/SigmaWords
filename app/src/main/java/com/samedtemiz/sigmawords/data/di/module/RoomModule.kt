package com.samedtemiz.sigmawords.data.di.module

import android.content.Context
import androidx.room.Room
import com.samedtemiz.sigmawords.data.dao.WordDao
import com.samedtemiz.sigmawords.data.repository.word.WordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideWordDatabase(@ApplicationContext context: Context) : WordDatabase {
        return Room.databaseBuilder(
            context,
            WordDatabase::class.java,
            "sigma_words.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDao(wordDatabase: WordDatabase) : WordDao {
        return wordDatabase.dao
    }
}