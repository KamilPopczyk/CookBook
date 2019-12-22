package com.example.cookbook;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orm.SugarDb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ListAdapter;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.Console;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView recipeListView;
    ArrayAdapter<Recipe> adapter;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recipeListView = findViewById(R.id.recipeListView);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        reloadRecipeList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeFormActivity = new Intent(MainActivity.this, RecipeForm.class);
                startActivity(recipeFormActivity);
                reloadRecipeList();
            }
        });

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
//                Log.d("INFO", recipes.get(position).toString());
            }
        });
        recipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, arg1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()){
                            case "Edytuj":
                                Bundle dataBundle = new Bundle();
                                Intent editRecipeActivity = new Intent(MainActivity.this, RecipeForm.class);
                                dataBundle.putLong("RecipeID", recipes.get(pos).getId());
                                editRecipeActivity.putExtras(dataBundle);
                                startActivity(editRecipeActivity);
                                break;
                            case "Usuń":
                                Recipe selectedRecipe = recipes.get(pos);
                                String recipeName = selectedRecipe.name;
                                selectedRecipe.delete();
                                Toast.makeText(MainActivity.this,"Usunięto : " + recipeName, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        reloadRecipeList();
                        return true;
                    }
                });

                popup.show();//showing popup menu
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadRecipeList() {
        recipes = Recipe.listAll(Recipe.class);
        adapter = new ArrayAdapter<Recipe>(MainActivity.this, android.R.layout.simple_list_item_1, recipes);
        recipeListView.setAdapter(adapter);
    }
}
