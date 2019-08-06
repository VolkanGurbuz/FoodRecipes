package com.example.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

  private static RecipeRepository instance;
  private RecipeApiClient mRecipeApiClient;

  public static RecipeRepository getInstance() {

    if (instance == null) {
      instance = new RecipeRepository();
    }
    return instance;
  }

  private RecipeRepository() {
    mRecipeApiClient = RecipeApiClient.getInstance();
  }

  public LiveData<List<Recipe>> getRecipes() {

    return mRecipeApiClient.getRecipes();
  }

  public void searchRecipesApi(String query, int pageNumber) {
    if (pageNumber == 0) {
      pageNumber = 1;
    }
    mRecipeApiClient.searchRecipesApi(query, pageNumber);
  }
}
