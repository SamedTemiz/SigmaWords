package com.samedtemiz.sigmawords.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.data.repository.word.WordRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWordRepository(
        database: FirebaseFirestore
    ): WordRepository {
        return WordRepositoryImp(database)
    }

}