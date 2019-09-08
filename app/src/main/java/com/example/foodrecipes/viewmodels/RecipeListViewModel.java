package com.example.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
  // holding recipe all the data keep update the list

  private RecipeRepository mRecipeRepository;
  private boolean mIsViewingRecipes;

  public RecipeListViewModel() {
    mIsViewingRecipes = false;
    mRecipeRepository = RecipeRepository.getInstance();
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipeRepository.getRecipes();
  }

  public void searchRecipesApi(String query, int pageNumber) {
    mIsViewingRecipes = true;
    mRecipeRepository.searchRecipesApi(query, pageNumber);
  }

  public boolean ismIsViewingRecipes() {
    return mIsViewingRecipes;
  }

  public void setmIsViewingRecipes(boolean mIsViewingRecipes) {
    this.mIsViewingRecipes = mIsViewingRecipes;
  }
}
