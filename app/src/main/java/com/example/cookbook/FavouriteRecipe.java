package com.example.cookbook;

import androidx.annotation.NonNull;

import com.orm.SugarRecord;

public class FavouriteRecipe extends SugarRecord<FavouriteRecipe> {
    public Recipe recipe;

    public FavouriteRecipe(){}

    public FavouriteRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public String toString() {
        return this.recipe.toString();
    }
}
