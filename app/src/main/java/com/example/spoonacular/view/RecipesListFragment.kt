package com.example.spoonacular.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spoonacular.MainActivity
import com.example.spoonacular.databinding.RecipeListLayoutBinding
import com.example.spoonacular.model.SpoonItem
import com.example.spoonacular.model.SpoonResponse

private const val TAG = "RecipesListFragment"

class RecipesListFragment : Fragment() {

    companion object{
        const val KEY_RESPONSE_DATA = "KEY_RESPONSE_DATA_RecipesListFragment"

        fun newInstance(response: SpoonResponse) =
            RecipesListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESPONSE_DATA, response)
                }
            }
    }

    private val adapter by lazy {
        RecipesAdapter(null, ::openDetailFragment)
    }

    fun openDetailFragment(spoonItem: SpoonItem) {
        (activity as MainActivity).openDetailFragment(spoonItem)
    }

    private lateinit var binding: RecipeListLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RecipeListLayoutBinding.inflate(inflater)

        arguments?.getParcelable<SpoonResponse>(KEY_RESPONSE_DATA)?.let{
            setupViews()
            adapter.updateDatSet(it)
        }
        return binding.root
    }

    private fun setupViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }
}