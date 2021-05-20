package com.example.spoonacular.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spoonacular.databinding.RecipeDetailLayoutBinding
import com.example.spoonacular.model.SpoonDetail
import com.squareup.picasso.Picasso

private const val TAG = "RecipesDetailFragment"

class RecipesDetailFragment: Fragment() {
    companion object{
        private const val KEY_RECIPE_DETAIL: String = "RECIPE_ID_RecipesDetailFragment"

        fun newInstance(detail: SpoonDetail): RecipesDetailFragment =
            RecipesDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RECIPE_DETAIL, detail)
                }
            }
    }

    private lateinit var binding: RecipeDetailLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RecipeDetailLayoutBinding.inflate(inflater)
        arguments?.getParcelable<SpoonDetail>(KEY_RECIPE_DETAIL)?.let {
            updateView(it)
        }
        return binding.root
    }

    private fun updateView(data: SpoonDetail) {
        binding.tvIngredientsDetail.text = data.analyzedInstructions[0].steps[0].ingredients.toString()
        binding.tvInstructionsDetail.text = data.instructions
        binding.tvSpoonUrlDetail.text = data.spoonacularSourceUrl
        binding.tvSummaryDetail.text = data.summary
        binding.tvTitleDetail.text = data.title
        Picasso.get().load(data.image).into(binding.ivRecipeImageDetail)
    }

}