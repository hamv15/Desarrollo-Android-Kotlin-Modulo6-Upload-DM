package com.hamv15.uploaddm.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    companion object{ //usaria el patrón singleton

        fun getRetrofit(): Retrofit{

            return Retrofit.Builder()
                .baseUrl("https://www.serverbpw.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

    }

}