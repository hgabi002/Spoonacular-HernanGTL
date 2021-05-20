package com.example.spoonacular.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipesRoom : RoomDatabase() {
    abstract fun dao(): RecipesDao

    companion object {
        private var INSTANCE: RecipesRoom? = null

        fun getInstance(context: Context): RecipesRoom {
            return INSTANCE ?: synchronized(this) {
                var temp = INSTANCE
                if (temp != null) return temp

                temp = Room.databaseBuilder(
                    context,
                    RecipesRoom::class.java,
                    "recipes_db"
                ).build()
                INSTANCE = temp
                return temp
            }
        }
    }
}