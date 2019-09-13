package com.example.foodrecipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.foodrecipes.models.Recipe;

public class RecipeActivity extends BaseActivity {

  // UI components
  private AppCompatImageView mRecipeImage;
  private TextView mRecipeTitle, mRecipeRank;
  private LinearLayout mRecileIngredientsContainer;
  private ScrollView mScrollView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    getIncomingIntent();
    mRecipeImage = findViewById(R.id.recipe_image);
    mRecipeTitle = findViewById(R.id.recipe_title);
    mRecipeRank = findViewById(R.id.recipe_social_score);
    mRecileIngredientsContainer = findViewById(R.id.ingredients_container);
    mScrollView = findViewById(R.id.parent);
  }

  private void getIncomingIntent() {
    if (getIntent().hasExtra("recipe")) {
      Recipe recipe = getIntent().getParcelableExtra("recipe");
    }
  }
}
