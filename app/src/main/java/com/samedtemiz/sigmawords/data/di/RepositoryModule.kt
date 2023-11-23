package com.samedtemiz.sigmawords.data.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.samedtemiz.sigmawords.data.DataStoreRepository
import com.samedtemiz.sigmawords.data.api.WordService
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.data.repository.user.UserRepositoryImp
import com.samedtemiz.sigmawords.util.Constant.BASE_URL
//import com.samedtemiz.sigmawords.data.repository.word.WordRepository
//import com.samedtemiz.sigmawords.data.repository.word.WordRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImp(database = database)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context = context)


    // Retrofit
    @Provides
    @Singleton
    fun provideRetrofitInstance(): WordService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordService::class.java)
    }
}