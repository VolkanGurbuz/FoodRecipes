package com.example.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

  private static RecipeRepository instance;
  private RecipeApiClient mRecipeApiClient;
  private String mQuery;
  private int mPageNumber;
  // no more search result for the query
  private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
  // if you want to chance setup
  private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

  public static RecipeRepository getInstance() {

    if (instance == null) {
      instance = new RecipeRepository();
    }
    return instance;
  }

  public LiveData<Boolean> isRecipeRequestTimedOut() {

    return mRecipeApiClient.isRecipeRequestTimedOut();
  }

  private RecipeRepository() {
    mRecipeApiClient = RecipeApiClient.getInstance();
    initMediators();
  }

  private void initMediators() {
    LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
    // when this source changing, or query chancing observe is activiting
    mRecipes.addSource(
        recipeListApiSource,
        new Observer<List<Recipe>>() {
          @Override
          public void onChanged(@Nullable List<Recipe> recipes) {
            if (recipes != null) {
              mRecipes.setValue(recipes);
              doneQuery(recipes);
            } else {
              // search database cache

            }
          }
        });
  }

  public void doneQuery(List<Recipe> list) {

    if (list != null) {
      if (list.size() %30 !=0) {
        mIsQueryExhausted.setValue(true);
      }
    } else {
      mIsQueryExhausted.setValue(true);
    }
  }

  public LiveData<Boolean> IsQueryExhausted() {
    return mIsQueryExhausted;
  }

  public LiveData<List<Recipe>> getRecipes() {

    return mRecipes;
  }

  public LiveData<Recipe> getRecipe() {

    return mRecipeApiClient.getRecipe();
  }

  public void searchRecipesApi(String query, int pageNumber) {
    if (pageNumber == 0) {
      pageNumber = 1;
    }
    mQuery = query;
    mPageNumber = pageNumber;
    mIsQueryExhausted.setValue(false);
    mRecipeApiClient.searchRecipesApi(query, pageNumber);
  }

  public void searchRecipeById(String recipeID) {
    mRecipeApiClient.searchRecipeByApi(recipeID);
  }

  public void searchNextPage() {

    searchRecipesApi(mQuery, mPageNumber++);
  }

  public void cancelRequert() {
    mRecipeApiClient.cancelRequest();
  }
}
