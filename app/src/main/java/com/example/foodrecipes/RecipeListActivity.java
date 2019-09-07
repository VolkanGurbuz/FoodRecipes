package com.example.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodrecipes.adapters.OnRecipeListener;
import com.example.foodrecipes.adapters.RecipeRecylerAdapter;
import com.example.foodrecipes.models.Recipe;

import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.viewmodels.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

  private static final String TAG = "RecipeListActivity";
  private RecipeListViewModel mReciPeListViewModel;
  private RecyclerView mRecyclerView;
  private RecipeRecylerAdapter mRecipeRecylerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);

    mRecyclerView = findViewById(R.id.recipe_list);

    // observing the live data the main reason to use
    mReciPeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    initRecylerView();
    subsriciveObserver();

    initSeachView();
  }

  private void initRecylerView() {
    mRecipeRecylerAdapter = new RecipeRecylerAdapter(this);
    mRecyclerView.setAdapter(mRecipeRecylerAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                  mRecipeRecylerAdapter.setRecipes(recipes);
                } else {

                  Toast.makeText(RecipeListActivity.this, "error ", Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  private void searchRecipesApi(String query, int pageNumber) {

    mReciPeListViewModel.searchRecipesApi(query, pageNumber);
  }

  private void initSeachView() {

    final android.support.v7.widget.SearchView searchView = findViewById(R.id.search_view);

    searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String s) {
            mRecipeRecylerAdapter.displayLoading();
            searchRecipesApi(s, 1);
            return false;
          }

          @Override
          public boolean onQueryTextChange(String s) {
            return false;
          }
        });
  }

  @Override
  public void onRecipeClick(int position) {}

  @Override
  public void onCategoryClick(String category) {}
}
