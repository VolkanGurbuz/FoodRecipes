package com.example.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
  // holding recipe all the data keep update the list

  private MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();

  public RecipeListViewModel() {}

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipes;
  }
}
