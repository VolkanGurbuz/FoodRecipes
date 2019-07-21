package com.example.foodrecipes.requests;

import com.example.foodrecipes.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

  // search
  @GET("api/seach")
  Call<RecipeSearchResponse> searchRecipe(
      @Query("key") String key, @Query("q") String query, @Query("page") String page);

  // search
  @GET("api/get")
  Call<RecipeSearchResponse> getRecipe(@Query("key") String key, @Query("rId") String recipe_id);
}
