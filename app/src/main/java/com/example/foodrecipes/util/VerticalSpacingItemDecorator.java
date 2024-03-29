package com.example.foodrecipes.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration {

  private final int vertivalSpaceHeight;

  public VerticalSpacingItemDecorator(int vertivalSpaceHeight) {
    this.vertivalSpaceHeight = vertivalSpaceHeight;
  }

  @Override
  public void getItemOffsets(
      @NonNull Rect outRect,
      @NonNull View view,
      @NonNull RecyclerView parent,
      @NonNull RecyclerView.State state) {

    // this certion offset to the recylerview

    outRect.top = vertivalSpaceHeight;
  }
}
