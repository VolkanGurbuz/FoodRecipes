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

  public RecipeListViewModel() {

    mRecipeRepository = RecipeRepository.getInstance();
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipeRepository.getRecipes();
  }

  public void searchRecipesApi(String query, int pageNumber) {

    mRecipeRepository.searchRecipesApi(query, pageNumber);
  }
}
