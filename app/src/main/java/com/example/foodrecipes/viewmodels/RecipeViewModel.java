package com.example.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

  private RecipeRepository mRecipeRepository;

  public RecipeViewModel() {
    mRecipeRepository = RecipeRepository.getInstance();
    ;
  }

  public LiveData<Recipe> getRecipe() {

    return mRecipeRepository.getRecipe();
  }

  public void searchRecipeById(String recipeId) {

    mRecipeRepository.searchRecipeById(recipeId);
  }
}
