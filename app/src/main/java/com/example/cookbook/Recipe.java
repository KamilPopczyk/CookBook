package com.example.cookbook;
import androidx.annotation.NonNull;

import com.orm.SugarRecord;

public class Recipe extends SugarRecord<Recipe>{
    public String name;
    public String ingredients;
    public String recipe;

    public Recipe(){}

    public Recipe(String name, String ingredients, String recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }


}
