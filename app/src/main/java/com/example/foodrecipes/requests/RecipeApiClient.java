package com.example.foodrecipes.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeApiClient {

  private static RecipeApiClient instance;
  private MutableLiveData<List<Recipe>> mRecipes;

  public static RecipeApiClient getInstance() {
    if (instance == null) {

      instance = new RecipeApiClient();
    }
    return instance;
  }

  public RecipeApiClient() {
    mRecipes = new MutableLiveData<>();
  };

  public LiveData<List<Recipe>> getRecipes() {

    return mRecipes;
  }
}
