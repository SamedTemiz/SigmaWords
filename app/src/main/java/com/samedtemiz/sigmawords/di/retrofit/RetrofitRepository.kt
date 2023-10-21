package com.samedtemiz.sigmawords.di.retrofit

import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.domain.models.Word
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val retrofitServiceInstance: RetrofitServiceInstance) {

    fun getWords_A1(liveData: MutableLiveData<Word>){
        retrofitServiceInstance.getWords_A1().enqueue(object : Callback<Word> {
            override fun onResponse(call: Call<Word>, response: Response<Word>) {
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Word>, t: Throwable) {
                liveData.postValue(null)
            }

        })
    }
}