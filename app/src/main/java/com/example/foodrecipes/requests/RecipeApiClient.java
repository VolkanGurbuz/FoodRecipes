package com.example.foodrecipes.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.foodrecipes.AppExecutor;
import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.responses.RecipeResponse;
import com.example.foodrecipes.responses.RecipeSearchResponse;
import com.example.foodrecipes.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okio.Timeout;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

  private static RecipeApiClient instance;
  private MutableLiveData<List<Recipe>> mRecipes;

  private static final String TAG = "RecipeApiClient";
  private RetrieveRecipesRunnable mRetrieveRunnable;
  private MutableLiveData<Recipe> mRecipe;
  private RetrieveRecipeRunnable mRetrieveRecipeRunnable;
  private MutableLiveData<Boolean> mRecipeRequestTimout = new MutableLiveData<>();

  public static RecipeApiClient getInstance() {
    if (instance == null) {

      instance = new RecipeApiClient();
    }
    return instance;
  }

  public RecipeApiClient() {
    mRecipes = new MutableLiveData<>();
    mRecipe = new MutableLiveData<>();
  };

  public LiveData<List<Recipe>> getRecipes() {

    return mRecipes;
  }

  public LiveData<Recipe> getRecipe() {

    return mRecipe;
  }

  public LiveData<Boolean> isRecipeRequestTimedOut() {

    return mRecipeRequestTimout;
  }

  public void searchRecipesApi(String query, int pageNumber) {
    if (mRetrieveRunnable == null) {
      mRetrieveRunnable = new RetrieveRecipesRunnable(query, pageNumber);
    }

    final Future handler = AppExecutor.getInstance().networkIO().submit(mRetrieveRunnable);

    AppExecutor.getInstance()
        .networkIO()
        .schedule(
            new Runnable() {
              @Override
              public void run() {

                // let the user know its timed out

                handler.cancel(true);
              }
            },
            NETWORK_TIMEOUT,
            TimeUnit.MILLISECONDS);
  }

  public void searchRecipeByApi(String recipeId) {
    if (mRetrieveRecipeRunnable != null) {
      mRetrieveRecipeRunnable = null;
    }

    mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);

    // we want to be able to stop it, if we need to
    final Future handler = AppExecutor.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

    mRecipeRequestTimout.setValue(false);

    // timeout
    AppExecutor.getInstance()
        .networkIO()
        .schedule(
            new Runnable() {
              @Override
              public void run() {

                // let the user know its timed out
                mRecipeRequestTimout.postValue(true);
                handler.cancel(true);
              }
            },
            NETWORK_TIMEOUT,
            TimeUnit.MILLISECONDS);
  }

  private class RetrieveRecipesRunnable implements Runnable {
    // parameters of the queery
    private String query;
    private int pageNumber;
    boolean cancelRequest;

    public RetrieveRecipesRunnable(String query, int pageNumber) {
      this.query = query;
      this.pageNumber = pageNumber;
      cancelRequest = false;
    }

    @Override
    public void run() {
      try {
        Response response = getRecipes(query, pageNumber).execute();
        if (cancelRequest) {
          return;
        }
        if (response.code() == 200) {

          List<Recipe> list =
              new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());

          if (pageNumber == 1) {
            mRecipes.postValue(list);
          } else {

            List<Recipe> currentRecipes = mRecipes.getValue();
            currentRecipes.addAll(list);
            mRecipes.postValue(currentRecipes);
          }

        } else {
          String error = response.errorBody().string();
          Log.e(TAG, error);
          mRecipes.postValue(null);
        }

      } catch (IOException e) {

        e.printStackTrace();
        mRecipes.postValue(null);
      }
    }

    private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
      return ServiceGenerator.getRecipeApi()
          .searchRecipe(Constants.API_KEY, query, String.valueOf(pageNumber));
    }

    private void cancelRequest() {
      Log.d(TAG, "cancel request: canceling the search request.");
      cancelRequest = true;
    }
  }

  private class RetrieveRecipeRunnable implements Runnable {
    // parameters of the queery
    private String recipeId;
    boolean cancelRequest;

    public RetrieveRecipeRunnable(String recipeId) {
      this.recipeId = recipeId;
      cancelRequest = false;
    }

    @Override
    public void run() {
      try {
        Response response = getRecipe(recipeId).execute();
        if (cancelRequest) {
          return;
        }
        if (response.code() == 200) {
          Recipe recipe = ((RecipeResponse) response.body()).getRecipe();
          mRecipe.postValue(recipe);

        } else {
          String error = response.errorBody().string();
          Log.e(TAG, error);
          mRecipes.postValue(null);
        }

      } catch (IOException e) {

        e.printStackTrace();
        mRecipes.postValue(null);
      }
    }

    private Call<RecipeResponse> getRecipe(String recipeId) {
      return ServiceGenerator.getRecipeApi().getRecipe(Constants.API_KEY, recipeId);
    }

    private void cancelRequest() {
      Log.d(TAG, "cancel request: canceling the search request.");
      cancelRequest = true;
    }
  }

  public void cancelRequest() {
    if (mRetrieveRunnable != null) {
      mRetrieveRunnable.cancelRequest();
    }

    if (mRetrieveRecipeRunnable != null) {
      mRetrieveRunnable.cancelRequest();
    }
  }
}
