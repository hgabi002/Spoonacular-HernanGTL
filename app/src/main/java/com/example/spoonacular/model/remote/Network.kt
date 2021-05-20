package com.example.spoonacular.model.remote

import com.example.spoonacular.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network private constructor(){

    companion object{
        private var INSTANCE: Network? = null

        fun getNetwork(): Network =
            INSTANCE ?: synchronized(this){
                var temp = INSTANCE
                if(temp != null) return temp

                temp = Network()
                INSTANCE = temp
                return temp
            }
    }

    private fun getClient(): OkHttpClient{
        return OkHttpClient.Builder().build()
    }

    fun getClientApi(): RecipeApi{
        return Retrofit.Builder()
            .client(getClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(getConverterFactory())
            .build()
            .create(RecipeApi::class.java)
    }

    private fun getConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }
}