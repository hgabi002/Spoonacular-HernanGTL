package com.example.spoonacular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.example.spoonacular.databinding.ActivityMainBinding
import com.example.spoonacular.model.*
import com.example.spoonacular.model.local.RecipesDao
import com.example.spoonacular.model.local.RecipesRoom
import com.example.spoonacular.view.RecipesDetailFragment
import com.example.spoonacular.view.RecipesListFragment
import com.example.spoonacular.viewmodel.RecipesViewModel
import com.example.spoonacular.viewmodel.RecipesViewmodelProvider
import java.lang.Exception

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    val viewModel: RecipesViewModel by lazy {
        RecipesViewmodelProvider(repository).create(RecipesViewModel::class.java)
    }

    val repository: Repository by lazy {
        RepositoryImpl(dao, this)
    }

    val dao: RecipesDao by lazy {
        RecipesRoom.getInstance(this).dao()
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()

        viewModel.liveData.observe(this) { dataState ->
            when (dataState) {
                is RepositoryImpl.DataState.SucessResponseRecipes ->
                    createListFragment(dataState.data)
                is RepositoryImpl.DataState.ErrorResponse ->
                    updateError(dataState.errorMessage)
                is RepositoryImpl.DataState.LoadingResponse ->
                    updateLoading(dataState.boolean)
                is RepositoryImpl.DataState.SucessResponseDetail ->
                    createDetailFragment(dataState.data)
            }
        }
    }

    private fun createDetailFragment(data: SpoonDetail) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, RecipesDetailFragment.newInstance(data))
            .commit()
    }

    fun openDetailFragment(spoonItem: SpoonItem){
        viewModel.getRecipesFromId(spoonItem.id)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun updateLoading(boolean: Boolean) {
        if (boolean)
            binding.progressBar.visibility = View.GONE
        else
            binding.progressBar.visibility = View.VISIBLE
    }

    private fun updateError(errorMessage: String) {
        Log.e(TAG, "updateError:", Exception(errorMessage))
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun createListFragment(data: SpoonResponse) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RecipesListFragment.newInstance(data))
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.search_menu)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.getRecipesFromInput(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            }
        )

        return super.onCreateOptionsMenu(menu)
    }

}