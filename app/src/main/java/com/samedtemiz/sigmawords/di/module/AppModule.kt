package com.samedtemiz.sigmawords.di.module

import com.samedtemiz.sigmawords.di.retrofit.Constant.Companion.WORDS_BASE_URL
import com.samedtemiz.sigmawords.di.retrofit.RetrofitServiceInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRetrofitServiceInstance(retrofit: Retrofit) : RetrofitServiceInstance {
        return retrofit.create(RetrofitServiceInstance::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(WORDS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}