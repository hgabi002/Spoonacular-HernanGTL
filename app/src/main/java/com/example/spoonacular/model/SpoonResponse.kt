package com.example.spoonacular.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpoonResponse(
    val results: List<SpoonItem>,
    val baseUri: String
): Parcelable

@Parcelize
data class SpoonItem(
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val image: String
): Parcelable

@Parcelize
data class SpoonDetail(
    val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val spoonacularSourceUrl: String,
    val summary: String,
    val analyzedInstructions: List<AnalyzedInstruction>
): Parcelable

@Parcelize
data class AnalyzedInstruction(
    val steps: List<Step>
): Parcelable

@Parcelize
data class Step(
    val ingredients: List<Ingredient>
): Parcelable

@Parcelize
data class Ingredient(
    val id: Int,
    val name: String
): Parcelable