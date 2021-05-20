package com.example.spoonacular.model.remote

import com.example.spoonacular.BuildConfig
import com.example.spoonacular.model.SpoonDetail
import com.example.spoonacular.model.SpoonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET(BuildConfig.END_POINT)
    suspend fun getRecipes(@Query("query") searchRecipeInput: String,
        @Query("apiKey") key: String =  BuildConfig.KEY): Response<SpoonResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeId(@Path("id")id: Int,
        @Query("apiKey") key: String =  BuildConfig.KEY): Response<SpoonDetail>
}