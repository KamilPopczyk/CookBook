package com.example.cookbook;
import androidx.annotation.NonNull;

import com.orm.SugarRecord;

import java.text.DateFormat;


public class Recipe extends SugarRecord<Recipe> {
    public String name;
    public String ingredients;
    public String recipe;
    public float portionSize;
    public float preparationTime;
    public float difficultyLevel;

    public Recipe(){}

    public Recipe(String name, String ingredients, String recipe, float portionSize, float preparationTime, float difficultyLevel) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.portionSize = portionSize;
        this.preparationTime = preparationTime;
        this.difficultyLevel = difficultyLevel;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }


}
