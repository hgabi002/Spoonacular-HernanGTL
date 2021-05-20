package com.example.spoonacular.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.databinding.ItemRecipeBinding
import com.example.spoonacular.model.SpoonItem
import com.example.spoonacular.model.SpoonResponse
import com.squareup.picasso.Picasso

class RecipesAdapter(var dataset: SpoonResponse?, val callback: (SpoonItem)->Unit):
    RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    class RecipesViewHolder(private val binding: ItemRecipeBinding):
        RecyclerView.ViewHolder(binding.root){
            fun onBind(dataItem: SpoonItem, listener: (SpoonItem)-> Unit){
                binding.tvTitle.text = dataItem.title
                binding.tvReady.text = dataItem.readyInMinutes.toString()
                Picasso.get().load(dataItem.image).into(binding.ivRecipeImage)

                binding.root.setOnClickListener{
                    listener(dataItem)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecipesViewHolder(ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        dataset?.let{
            holder.onBind(it.results[position], callback)
        }
    }

    override fun getItemCount()= dataset?.results?.size ?: 0

    fun updateDatSet(dataset: SpoonResponse?){
        this.dataset = dataset
        notifyDataSetChanged()
    }
}