package com.example.cookbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeForm extends AppCompatActivity {

    private Button buttonRecipeSave;
    private TextInputEditText name;
    private EditText ingredients;
    private EditText recipe;
    private RatingBar ratingBarPortionSize;
    private RatingBar ratingBarPreparationTime;
    private RatingBar ratingBarDifficultyLevel;
    private Boolean editRecipe = false;
    private Recipe editRecipeObject;
    private Intent myLocalIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dodawanie nowego przepisu");
        buttonRecipeSave = findViewById(R.id.buttonSave);
        name = findViewById(R.id.textInputName);
        ingredients = findViewById(R.id.editTextIngredients);
        recipe = findViewById(R.id.editTextRecipe);
        ratingBarPortionSize = findViewById(R.id.ratingBarPortionSize);
        ratingBarPreparationTime = findViewById(R.id.ratingBarTime);
        ratingBarDifficultyLevel = findViewById(R.id.ratingBarDifficultyLevel);
        myLocalIntent = getIntent();
        Bundle dataBundle = myLocalIntent.getExtras();
        if (dataBundle != null) {
            editRecipe = true;
            editRecipeObject = Recipe.findById(Recipe.class, dataBundle.getLong("RecipeID"));
            getSupportActionBar().setTitle("Edytowanie przepisu: " + editRecipeObject.name);
            name.setText(editRecipeObject.name);
            ingredients.setText(editRecipeObject.ingredients);
            recipe.setText(editRecipeObject.recipe);
            ratingBarPortionSize.setRating(editRecipeObject.portionSize);
            ratingBarPreparationTime.setRating(editRecipeObject.preparationTime);
            ratingBarDifficultyLevel.setRating(editRecipeObject.difficultyLevel);
        }



        buttonRecipeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRecipe) {
                    editRecipeObject.name = name.getText().toString();
                    editRecipeObject.ingredients = ingredients.getText().toString();
                    editRecipeObject.recipe = recipe.getText().toString();
                    editRecipeObject.portionSize = ratingBarPortionSize.getRating();
                    editRecipeObject.preparationTime = ratingBarPreparationTime.getRating();
                    editRecipeObject.difficultyLevel = ratingBarDifficultyLevel.getRating();
                    editRecipeObject.save();
                    Toast.makeText(RecipeForm.this, "Edycja przepisu " + editRecipeObject.name, Toast.LENGTH_LONG).show();
                } else {
                    Log.i("INFO", Float.toString(ratingBarPortionSize.getRating()));
                    Log.i("INFO", Float.toString(ratingBarPreparationTime.getRating()));
                    Log.i("INFO", Float.toString(ratingBarDifficultyLevel.getRating()));
                    Recipe recipeDb = new Recipe(name.getText().toString(),
                            ingredients.getText().toString(),
                            recipe.getText().toString(),
                            ratingBarPortionSize.getRating(),
                            ratingBarPreparationTime.getRating(),
                            ratingBarDifficultyLevel.getRating());
                    recipeDb.save();
                    Toast.makeText(RecipeForm.this, "Zapisano nowy przepis", Toast.LENGTH_LONG).show();
                }
                setResult(101, myLocalIntent);
                finish();
            }
        });
    }

}
