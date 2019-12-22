package com.example.cookbook;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeForm extends AppCompatActivity {

    private Button buttonRecipeSave;
    private TextInputEditText name;
    private EditText ingredients;
    private EditText recipe;
    private Boolean editRecipe = false;
    private Recipe editRecipeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonRecipeSave = findViewById(R.id.buttonSave);
        name = findViewById(R.id.textInputName);
        ingredients = findViewById(R.id.editTextIngredients);
        recipe = findViewById(R.id.editTextRecipe);
        Intent myLocalIntent = getIntent();
        Bundle dataBundle = myLocalIntent.getExtras();
        if (dataBundle.getLong("RecipeID") > 0) {
            editRecipe = true;
            editRecipeObject = Recipe.findById(Recipe.class, dataBundle.getLong("RecipeID"));
            name.setText(editRecipeObject.name);
            ingredients.setText(editRecipeObject.ingredients);
            recipe.setText(editRecipeObject.recipe);
        }



        buttonRecipeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRecipe) {
                    editRecipeObject.name = name.getText().toString();
                    editRecipeObject.ingredients = ingredients.getText().toString();
                    editRecipeObject.recipe = recipe.getText().toString();
                    editRecipeObject.save();
                } else {
                    Recipe recipeDb = new Recipe(name.getText().toString(),
                            ingredients.getText().toString(), recipe.getText().toString());
                    recipeDb.save();
                }
                finish();
            }
        });
    }

}
