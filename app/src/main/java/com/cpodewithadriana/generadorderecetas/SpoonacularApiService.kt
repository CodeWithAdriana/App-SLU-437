package com.cpodewithadriana.generadorderecetas

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/findByIngredients")
    fun searchRecipes(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String
    ): Call<List<Recipe>>
}
