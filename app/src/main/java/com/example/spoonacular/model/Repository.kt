package com.example.spoonacular.model

interface Repository {
    suspend fun getRecipesFromInput(input: String): RepositoryImpl.DataState
    suspend fun getRecipeDetail(id: Int): RepositoryImpl.DataState
}