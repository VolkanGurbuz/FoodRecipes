package com.example.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.foodrecipes.adapters.OnRecipeListener;
import com.example.foodrecipes.adapters.RecipeRecylerAdapter;
import com.example.foodrecipes.models.Recipe;

import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.util.VerticalSpacingItemDecorator;
import com.example.foodrecipes.viewmodels.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

  private static final String TAG = "RecipeListActivity";
  private RecipeListViewModel mReciPeListViewModel;
  private RecyclerView mRecyclerView;
  private RecipeRecylerAdapter mRecipeRecylerAdapter;
  private SearchView mSearchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    mSearchView = findViewById(R.id.search_view);
    mRecyclerView = findViewById(R.id.recipe_list);

    // observing the live data the main reason to use
    mReciPeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    initRecylerView();
    subsriciveObserver();

    initSeachView();

    if (!mReciPeListViewModel.ismIsViewingRecipes()) {
      // display search categories
      displaySeachCategories();
    }

    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
  }

  private void initRecylerView() {
    mRecipeRecylerAdapter = new RecipeRecylerAdapter(this);
    VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
    mRecyclerView.addItemDecoration(itemDecorator);
    mRecyclerView.setAdapter(mRecipeRecylerAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mRecyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {

          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            // detect when we below on fthe page

            if (!mRecyclerView.canScrollVertically(1)) {

              // search the next page
              mReciPeListViewModel.searchNextPage();
            }
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
                  if (mReciPeListViewModel.ismIsViewingRecipes()) {
                    Testing.printRecipes(recipes, TAG);
                    mReciPeListViewModel.setmIsPerformingQuery(false);
                    mRecipeRecylerAdapter.setRecipes(recipes);
                  }

                } else {

                  Toast.makeText(RecipeListActivity.this, "error ", Toast.LENGTH_SHORT).show();
                }
              }
            });

    mReciPeListViewModel
        .isQueryExhausted()
        .observe(
            this,
            new Observer<Boolean>() {
              @Override
              public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                  mRecipeRecylerAdapter.setQueryExhausted();
                }
              }
            });
  }

  private void searchRecipesApi(String query, int pageNumber) {

    mReciPeListViewModel.searchRecipesApi(query, pageNumber);
  }

  private void initSeachView() {

    mSearchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String s) {
            mRecipeRecylerAdapter.displayLoading();
            searchRecipesApi(s, 1);
            mSearchView.clearFocus();
            return false;
          }

          @Override
          public boolean onQueryTextChange(String s) {
            return false;
          }
        });
  }

  private void displaySeachCategories() {
    mReciPeListViewModel.setmIsViewingRecipes(false);
    mRecipeRecylerAdapter.displaySearchCategories();
  }

  @Override
  public void onRecipeClick(int position) {

    Intent intent = new Intent(this, RecipeActivity.class);
    intent.putExtra("recipe", mRecipeRecylerAdapter.getSelectedRecipe(position));
    startActivity(intent);
  }

  @Override
  public void onCategoryClick(String category) {
    mRecipeRecylerAdapter.displayLoading();
    searchRecipesApi(category, 1);
    mSearchView.clearFocus();
  }

  // when back button it helps to stay on the application

  @Override
  public void onBackPressed() {

    if (mReciPeListViewModel.ismIsViewingRecipes()) {
      super.onBackPressed();
    } else {

      displaySeachCategories();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_categoris) {
      displaySeachCategories();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.recipe_search_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }
}
