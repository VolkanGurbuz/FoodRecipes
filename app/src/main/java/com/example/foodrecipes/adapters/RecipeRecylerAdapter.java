package com.example.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodrecipes.R;
import com.example.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeRecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Recipe> mRecipes;
  private OnRecipeListener mOnRecipeListener;

  public RecipeRecylerAdapter(List<Recipe> mRecipes, OnRecipeListener mOnRecipeListener) {
    this.mRecipes = mRecipes;
    this.mOnRecipeListener = mOnRecipeListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.layout_recipe_list_item, viewGroup, false);

    return new RecipeViewHolder(view, mOnRecipeListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    RequestOptions requestOptions =
        new RequestOptions().placeholder(R.drawable.ic_launcher_background);
    Glide.with(viewHolder.itemView.getContext())
        .setDefaultRequestOptions(requestOptions)
        .load(mRecipes.get(i))
        .into(((RecipeViewHolder) viewHolder).image);

    ((RecipeViewHolder) viewHolder).title.setText(mRecipes.get(i).getTitle());
    ((RecipeViewHolder) viewHolder).publisher.setText(mRecipes.get(i).getPublisher());
    ((RecipeViewHolder) viewHolder)
        .socialScore.setText(String.valueOf(Math.round(mRecipes.get(i).getSocial_rank())));
  }

  @Override
  public int getItemCount() {
    return mRecipes.size();
  }

  public void setRecipes(List<Recipe> recipes) {
    mRecipes = recipes;
    notifyDataSetChanged();
  }
}
