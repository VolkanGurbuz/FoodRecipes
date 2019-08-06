package com.example.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApi;
import com.example.foodrecipes.requests.ServiceGenerator;
import com.example.foodrecipes.responses.RecipeSearchResponse;
import com.example.foodrecipes.util.Constants;
import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.viewmodels.RecipeListViewModel;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

  private static final String TAG = "RecipeListActivity";
  private RecipeListViewModel mReciPeListViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);

    // observing the live data the main reason to use
    mReciPeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

    subsriciveObserver();

    findViewById(R.id.test_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                testRetrofitRequst();
              }
            });
  }

  private void subsriciveObserver() {
    mReciPeListViewModel
        .getRecipes()
        .observe(
            this,
            new Observer<List<Recipe>>() {
              @Override
              public void onChanged(List<Recipe> recipes) {

                if (recipes != null) {

                  Testing.printRecipes(recipes, TAG);

                } else {

                  Toast.makeText(RecipeListActivity.this, "error ", Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  private void searchRecipesApi(String query, int pageNumber) {

    mReciPeListViewModel.searchRecipesApi(query, pageNumber);
  }

  private void testRetrofitRequst() {
    searchRecipesApi("chicken", 1);
  }
}
