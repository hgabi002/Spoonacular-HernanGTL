package com.example.spoonacular.model.local

import androidx.room.*

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title_recipe") val title: String,
    val instructions: String,
    val summary: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<String>,
    @ColumnInfo(name = "last_update") val lastUpdate: Long
)