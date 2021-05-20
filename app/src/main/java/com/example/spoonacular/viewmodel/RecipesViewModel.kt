package com.example.spoonacular.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spoonacular.model.Repository
import com.example.spoonacular.model.RepositoryImpl
import kotlinx.coroutines.*

class RecipesViewModel (private val repository: Repository): ViewModel() {
    private val job = CoroutineScope(Dispatchers.IO)

    private val mutableLiveData = MutableLiveData<RepositoryImpl.DataState>()
    val liveData: LiveData<RepositoryImpl.DataState> = mutableLiveData

    fun getRecipesFromInput(input: String){
        job.launch {
            val data = repository.getRecipesFromInput(input)
            withContext(Dispatchers.Main) {
                mutableLiveData.value = RepositoryImpl.DataState.LoadingResponse(true)
                delay(1000)
                mutableLiveData.value = data
            }
        }
    }

    fun getRecipesFromId(id: Int){
        job.launch {
            val data = repository.getRecipeDetail(id)
            withContext(Dispatchers.Main){
                mutableLiveData.value = RepositoryImpl.DataState.LoadingResponse(true)
                delay(1000)
                mutableLiveData.value = data
            }
        }
    }
}