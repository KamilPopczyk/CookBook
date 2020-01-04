package com.example.cookbook;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class RecipeView extends AppCompatActivity {
    private Intent myLocalIntent;
    private TextView textViewName;
    private TextView textViewIngredients;
    private TextView textViewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewName = findViewById(R.id.textViewName);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewRecipe = findViewById(R.id.textViewRecipe);
        myLocalIntent = getIntent();
        Bundle dataBundle = myLocalIntent.getExtras();
        Recipe recipe = Recipe.findById(Recipe.class, dataBundle.getLong("RecipeID"));
        textViewName.setText(recipe.name.toUpperCase());
        textViewIngredients.setText(recipe.ingredients);
        textViewRecipe.setText(recipe.recipe);
    }

}
