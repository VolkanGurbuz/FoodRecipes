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
  private boolean mIsPerformingQuery;

  public RecipeListViewModel() {
    mRecipeRepository = RecipeRepository.getInstance();
    mIsPerformingQuery = false;
  }

  public boolean ismIsPerformingQuery() {
    return mIsPerformingQuery;
  }

  public void setmIsPerformingQuery(boolean mIsPerformingQuery) {
    this.mIsPerformingQuery = mIsPerformingQuery;
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipeRepository.getRecipes();
  }

  public LiveData<Boolean> isQueryExhausted() {
    return mRecipeRepository.IsQueryExhausted();
  }

  public void searchRecipesApi(String query, int pageNumber) {
    mIsViewingRecipes = true;
    mIsPerformingQuery = true;
    mRecipeRepository.searchRecipesApi(query, pageNumber);
  }

  public void searchNextPage() {
    if (!mIsPerformingQuery && mIsViewingRecipes && !isQueryExhausted().getValue()) {
      mRecipeRepository.searchNextPage();
    }
  }

  public boolean ismIsViewingRecipes() {
    return mIsViewingRecipes;
  }

  public void setmIsViewingRecipes(boolean mIsViewingRecipes) {
    this.mIsViewingRecipes = mIsViewingRecipes;
  }

  public boolean onBackPressed() {
    if (ismIsPerformingQuery()) {
      // cancel the query
      mRecipeRepository.cancelRequert();
      mIsPerformingQuery = false;
    }
    if (mIsViewingRecipes) {
      mIsViewingRecipes = false;
      return false;
    }
    return true;
  }
}
