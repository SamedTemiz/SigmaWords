package com.samedtemiz.sigmawords.di.module


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

//    @Provides
//    @Singleton
//    fun getRetrofitServiceInstance(retrofit: Retrofit) : RetrofitServiceInstance {
//        return retrofit.create(RetrofitServiceInstance::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun getRetrofitInstance(): Retrofit{
//        return Retrofit.Builder()
//            .baseUrl(WORDS_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

}