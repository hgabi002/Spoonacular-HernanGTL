package com.example.spoonacular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spoonacular.model.Repository

class RecipesViewmodelProvider (private val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipesViewModel(repository) as T
    }
}