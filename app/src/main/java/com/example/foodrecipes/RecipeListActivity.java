package com.example.foodrecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApi;
import com.example.foodrecipes.requests.ServiceGenerator;
import com.example.foodrecipes.responses.RecipeSearchResponse;
import com.example.foodrecipes.util.Constants;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

  private static final String TAG = "RecipeListActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);

    findViewById(R.id.test_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                testRetrofitRequst();
              }
            });
  }

  private void testRetrofitRequst() {
    RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

    Call<RecipeSearchResponse> responseCall =
        recipeApi.searchRecipe(Constants.API_KEY, "chicken breast", "1");

    responseCall.enqueue(
        new Callback<RecipeSearchResponse>() {
          @Override
          public void onResponse(
              Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {

            Log.d(TAG, "On response:service response: " + response.toString());
            if (response.code() == 200) {
              Log.d(TAG, "On response:service response: " + response.body().toString());

              List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
              for (Recipe r : recipes) {
                Log.d(TAG, r.getTitle());
              }
            } else {

              try {
                Log.d(TAG, "on response" + response.errorBody().string());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {}
        });
  }
}
