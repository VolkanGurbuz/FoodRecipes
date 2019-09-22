package com.example.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

  private RecipeRepository mRecipeRepository;
  private String mRecipeId;
  private boolean mDIidRetrieveRecipe;

  public RecipeViewModel() {
    mRecipeRepository = RecipeRepository.getInstance();

    mDIidRetrieveRecipe = false;
  }

  public LiveData<Recipe> getRecipe() {

    return mRecipeRepository.getRecipe();
  }

  public void searchRecipeById(String recipeId) {
    mRecipeId = recipeId;
    mRecipeRepository.searchRecipeById(recipeId);
  }

  public LiveData<Boolean> isRecipeRequestTimedOut() {

    return mRecipeRepository.isRecipeRequestTimedOut();
  }

  public String getRecipeId() {
    return mRecipeId;
  }

  public void setmDIidRetrieveRecipe(boolean mDIidRetrieveRecipe) {
    this.mDIidRetrieveRecipe = mDIidRetrieveRecipe;
  }

  public boolean ismDIidRetrieveRecipe() {
    return mDIidRetrieveRecipe;
  }
}
