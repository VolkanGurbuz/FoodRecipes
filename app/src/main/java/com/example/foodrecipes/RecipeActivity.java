package com.example.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

  // UI components
  private AppCompatImageView mRecipeImage;
  private TextView mRecipeTitle, mRecipeRank;
  private LinearLayout mRecileIngredientsContainer;
  private ScrollView mScrollView;
  private RecipeViewModel mRecipeViewModel;

  private static final String TAG = "RecipeActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    mRecipeImage = findViewById(R.id.recipe_image);
    mRecipeTitle = findViewById(R.id.recipe_title);
    mRecipeRank = findViewById(R.id.recipe_social_score);
    mRecileIngredientsContainer = findViewById(R.id.ingredients_container);
    mScrollView = findViewById(R.id.parent);

    mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

    subscribeObserver();
    getIncomingIntent();
  }

  private void getIncomingIntent() {
    if (getIntent().hasExtra("recipe")) {
      Recipe recipe = getIntent().getParcelableExtra("recipe");
      Log.d(TAG, "getincomingIntent" + recipe.getTitle());
      Log.d(TAG, "getincomingIntent" + recipe.getRecipe_id());

      mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
    }
  }

  private void subscribeObserver() {
    mRecipeViewModel
        .getRecipe()
        .observe(
            this,
            new Observer<Recipe>() {
              @Override
              public void onChanged(@Nullable Recipe recipe) {

                Log.d(TAG, "ON CHANGED ============");
                Log.d(TAG, recipe.getTitle());

                for (String ingredient : recipe.getIngredients()) {

                  Log.d(TAG, ingredient);
                }
              }
            });
  }
}
