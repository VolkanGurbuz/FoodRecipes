package com.example.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    showProgressBar(true);
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
                if (recipe != null) {
                  if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
                    setRecipeProperties(recipe);
                    mRecipeViewModel.setmDIidRetrieveRecipe(true);
                  }
                }
              }
            });

    mRecipeViewModel
        .isRecipeRequestTimedOut()
        .observe(
            this,
            new Observer<Boolean>() {
              @Override
              public void onChanged(@Nullable Boolean isTime) {

                if (isTime && !mRecipeViewModel.ismDIidRetrieveRecipe()) {
                  Log.d(TAG, "on changed timed out");
                  displayErrorScreen("error, check connection...");
                }
              }
            });
  }

  private void displayErrorScreen(String errorMessage) {
    mRecipeTitle.setText("Error retrieveing recipe...");
    mRecipeRank.setText("");
    TextView textView = new TextView(this);
    if (!errorMessage.equals("")) {
      textView.setText(errorMessage);

    } else {
      textView.setText("Error");
    }
    textView.setTextSize(15);
    textView.setLayoutParams(
        new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    mRecileIngredientsContainer.addView(textView);
    RequestOptions requestOptions =
        new RequestOptions().placeholder(R.drawable.ic_launcher_background);
    Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(R.drawable.ic_launcher_background)
        .into(mRecipeImage);
    showParent();
    showProgressBar(false);

    mRecipeViewModel.setmDIidRetrieveRecipe(true);
  }

  private void setRecipeProperties(Recipe recipe) {
    if (recipe != null) {
      RequestOptions requestOptions =
          new RequestOptions().placeholder(R.drawable.ic_launcher_background);
      Glide.with(this)
          .setDefaultRequestOptions(requestOptions)
          .load(recipe.getImage_url())
          .into(mRecipeImage);
      mRecipeTitle.setText(recipe.getTitle());
      mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

      mRecileIngredientsContainer.removeAllViews();
      for (String ingredient : recipe.getIngredients()) {
        TextView textView = new TextView(this);
        textView.setText(ingredient);
        textView.setTextSize(15);
        textView.setLayoutParams(
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecileIngredientsContainer.addView(textView);
      }
    }
    showParent();
    showProgressBar(false);
  }

  private void showParent() {
    mScrollView.setVisibility(View.VISIBLE);
  }
}
