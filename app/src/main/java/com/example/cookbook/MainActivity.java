package com.example.cookbook;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orm.SugarDb;
import com.orm.query.Condition;
import com.orm.query.Select;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    SearchView searchViewRecipe;
    ListView recipeListView;
    ArrayAdapter<Recipe> adapter;
    List<Recipe> recipes;
    SensorManager sensorManager;

    boolean favouriteListClicked;

    float accelertionValue;
    float accelerationLastValue;
    float accelerationShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        searchViewRecipe = findViewById(R.id.searchViewRecipe);
        recipeListView = findViewById(R.id.recipeListView);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        accelertionValue = SensorManager.GRAVITY_EARTH;
        accelerationLastValue = SensorManager.GRAVITY_EARTH;
        accelerationShake = 0.00f;

        FloatingActionButton fab = findViewById(R.id.fab);
        final FloatingActionButton fabFavouriteList = findViewById(R.id.fabFavouriteList);
        reloadRecipeList();
        favouriteListClicked = false;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeFormActivity = new Intent(MainActivity.this, RecipeForm.class);
                startActivityForResult(recipeFormActivity, 101);
                reloadRecipeList();
            }
        });

        fabFavouriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favouriteListClicked){
                    reloadRecipeList();
                    favouriteListClicked = false;
                    fabFavouriteList.setImageResource(android.R.drawable.btn_star);
                }
                else {
                    List<FavouriteRecipe> favouriteRecipes = FavouriteRecipe.listAll(FavouriteRecipe.class);
                    Log.d("INFO", favouriteRecipes.toString());
                    List<Recipe> favRecipes = new ArrayList<>();
                    for (FavouriteRecipe favRecipe: favouriteRecipes) {
                        favRecipes.add(favRecipe.recipe);
                    }
                    recipes = favRecipes;
                    adapter = new ArrayAdapter<Recipe>(MainActivity.this, android.R.layout.simple_list_item_1, recipes);
                    recipeListView.setAdapter(adapter);
                    favouriteListClicked = true;
                    fabFavouriteList.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    Toast.makeText(MainActivity.this, "Lista ulubionych", Toast.LENGTH_LONG).show();
                }
            }
        });


        searchViewRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
//                view.setSelected(true);
                Bundle dataBundle = new Bundle();
                Intent viewRecipeActivity = new Intent(MainActivity.this, RecipeView.class);
                dataBundle.putLong("RecipeID", recipes.get(position).getId());
                viewRecipeActivity.putExtras(dataBundle);
                startActivity(viewRecipeActivity);
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
                        Recipe selectedRecipe = recipes.get(pos);
                        switch (item.getTitle().toString()){
                            case "Dodaj/usuń lista ulubionych":
                                List<FavouriteRecipe> favouriteRecipeFoundList =
                                        Select.from(FavouriteRecipe.class)
                                        .where(Condition.prop("recipe").eq(selectedRecipe.getId().toString()))
                                        .list();
                                if (favouriteRecipeFoundList.isEmpty()) {
                                    FavouriteRecipe favouriteRecipe = new FavouriteRecipe(selectedRecipe);
                                    favouriteRecipe.save();
                                    Toast.makeText(MainActivity.this, "Dodano do ulubionych: " + selectedRecipe.name, Toast.LENGTH_LONG).show();
                                }
                                else {
                                    favouriteRecipeFoundList.get(0).delete();
                                    Toast.makeText(MainActivity.this, "Usunięto z ulubionych: " + selectedRecipe.name, Toast.LENGTH_LONG).show();
                                }
                                break;
                            case "Edytuj":
                                Bundle dataBundle = new Bundle();
                                Intent editRecipeActivity = new Intent(MainActivity.this, RecipeForm.class);
                                dataBundle.putLong("RecipeID", recipes.get(pos).getId());
                                editRecipeActivity.putExtras(dataBundle);
                                startActivityForResult(editRecipeActivity, 101);
                                reloadRecipeList();
                                break;
                            case "Usuń":
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

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelerationLastValue = accelertionValue;
            accelertionValue = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = accelertionValue - accelerationLastValue;
            accelerationShake = accelerationShake * 0.9f + delta;

//            Log.i("ShakeEvent", Float.toString(accelerationShake));
            if (accelerationShake > 9) {
                Bundle dataBundle = new Bundle();
                Intent viewRecipeActivity = new Intent(MainActivity.this, RecipeView.class);
                dataBundle.putLong("RecipeID", new Random().nextInt(recipes.size()));
                viewRecipeActivity.putExtras(dataBundle);
                startActivity(viewRecipeActivity);
                Toast toast = Toast.makeText(MainActivity.this, "Wybrano losowy przepis", Toast.LENGTH_LONG);
                toast.show();

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 101 ) {
                reloadRecipeList();
            }
        }
        catch (Exception e) {
        }
    }

    private void reloadRecipeList() {
        recipes = Recipe.listAll(Recipe.class);
        adapter = new ArrayAdapter<Recipe>(MainActivity.this, android.R.layout.simple_list_item_1, recipes);
        recipeListView.setAdapter(adapter);
    }
}
