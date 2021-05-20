package com.example.spoonacular.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spoonacular.model.SpoonDetail

@Dao
interface RecipesDao {
    @Insert(entity = RecipeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceCache(data: RecipeEntity)

    @Query("SELECT * FROM recipe_table WHERE id = :id")
    suspend fun getLocalRecipes(id: Int): RecipeEntity?

    @Query("SELECT last_update FROM recipe_table WHERE id = :id")
    suspend fun getLocalRecipeLastUpdate(id: Int): Long
}