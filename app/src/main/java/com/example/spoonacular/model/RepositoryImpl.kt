package com.example.spoonacular.model

import android.content.Context
import com.example.spoonacular.model.local.RecipeEntity
import com.example.spoonacular.model.local.RecipesDao
import com.example.spoonacular.model.remote.Network
import retrofit2.Response

class RepositoryImpl(private val dao: RecipesDao, private val context: Context) : Repository {

    companion object {
        const val OLD_TIME_STAMP = 1000 * 3600 * 24 * 7// 7 days
    }

    override suspend fun getRecipesFromInput(input: String): DataState {
        return dataFactoryRecipes(
            Network.getNetwork().getClientApi().getRecipes(input)
        )
    }

    private fun dataFactoryRecipes(recipes: Response<SpoonResponse>): DataState {
        return if(recipes.isSuccessful && recipes.body() != null)
            DataState.SucessResponseRecipes(recipes.body()!!)
        else
            DataState.ErrorResponse(recipes.message())
    }

    override suspend fun getRecipeDetail(id: Int): DataState {
        //check cache survivability
        // do remote or read from cache

        val localRecipe = dao.getLocalRecipes(id)
        return when {
            localRecipe == null || !getLongevityFromCache(localRecipe.id) -> {
                val response = getRecipesFromRemote(id)
                updateFromRemote(response)
            }
            else -> {
                dataFactoryFromLocal(localRecipe)
            }
        }

    }

    private suspend fun updateFromRemote(response: Response<SpoonDetail>): DataState {
        return dataFactoryFromRemote(response)
    }

    /**
     * Create Presentation Data from Room Entity
     */
    private fun dataFactoryFromLocal(localRecipe: RecipeEntity): DataState {
        return DataState.SucessResponseDetail(
            SpoonDetail(
                localRecipe.id,
                localRecipe.title,
                "",
                localRecipe.instructions,
                "",
                localRecipe.summary,
                createAnalyzedInstructions(localRecipe.ingredients)
            )
        )
    }

    private fun createAnalyzedInstructions(ingredients: List<String>): List<AnalyzedInstruction> {
        val stepIngredients = mutableListOf<Ingredient>()//list ingredient
        val analyzedInstruction = mutableListOf<AnalyzedInstruction>()
        ingredients.forEachIndexed { index, item ->
            stepIngredients.add(
                Ingredient(index, item)
            )
        }
        analyzedInstruction.add(
            AnalyzedInstruction(mutableListOf(Step(stepIngredients)))
        )
        return analyzedInstruction
    }

    /**
     * Create Presentation Data from Retrofit response
     */
    private suspend fun dataFactoryFromRemote(response: Response<SpoonDetail>): DataState {
        return if (response.isSuccessful && response.body() != null) {
            updateLocalFromRemote(response.body()!!)
            DataState.SucessResponseDetail(response.body()!!)
        } else {
            DataState.ErrorResponse(response.message())
        }
    }

    /**
     * Find remote data from ID
     */
    private suspend fun getRecipesFromRemote(id: Int): Response<SpoonDetail> {
        return Network.getNetwork().getClientApi().getRecipeId(id)
    }

    /**
     * Local data out of date or new data from Remote. Update Room entity
     */
    private suspend fun updateLocalFromRemote(spoonDetail: SpoonDetail) {
        dao.replaceCache(
            RecipeEntity(
                spoonDetail.id,
                spoonDetail.title,
                spoonDetail.instructions,
                spoonDetail.summary,
                createIngredientList(
                    spoonDetail.analyzedInstructions[0].steps[0].ingredients
                ),
                System.currentTimeMillis()
            )
        )
    }

    private fun createIngredientList(ingredients: List<Ingredient>?): List<String> {
        val result = mutableListOf<String>()
        if (ingredients == null) return  result
        ingredients?.forEach {
            result.add(it.name)
        }
        return result
    }


    /**
     * Check if current Recipe ID is valid. Valid up to 7 days.
     */
    private suspend fun getLongevityFromCache(recipeId: Int): Boolean {
        val longevity = dao.getLocalRecipeLastUpdate(recipeId)
        return System.currentTimeMillis() < longevity + OLD_TIME_STAMP
    }

    sealed class DataState {
        data class SucessResponseRecipes(val data: SpoonResponse) : DataState()
        data class SucessResponseDetail(val data: SpoonDetail) : DataState()
        data class ErrorResponse(val errorMessage: String) : DataState()
        data class LoadingResponse(val boolean: Boolean) : DataState()
    }
}