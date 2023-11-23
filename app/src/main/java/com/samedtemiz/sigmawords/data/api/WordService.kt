package com.samedtemiz.sigmawords.data.api

import com.samedtemiz.sigmawords.data.model.Word
import retrofit2.Response
import retrofit2.http.GET

interface WordService {
    @GET("Oxford_Lists_ALL.json")
    suspend fun getAllWords(): Response<List<Word>>
}