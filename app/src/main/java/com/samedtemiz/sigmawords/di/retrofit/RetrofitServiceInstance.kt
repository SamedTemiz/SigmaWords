package com.samedtemiz.sigmawords.di.retrofit

import com.samedtemiz.sigmawords.domain.models.Word
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServiceInstance {
    @GET("words_A1")
    fun getWords_A1(): Call<Word>

    @GET("words_A2")
    fun getWords_A2(): Call<Word>
}