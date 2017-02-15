package com.axel.recipebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Main page of the app. Contains a list of the users saved recipes.
 * Each recipe in the list can be clicked which brings the user to
 * an other activity displaying the info of that recipe.
 *
 * Also contains a button which brings the user to an activity to create
 * a new recipe.
 *
 * @author Axel Lindeberg
 */
public class Main_Activity extends AppCompatActivity {
    ArrayList<Recipe> recipes;
    ArrayAdapter<Recipe> adapter;
    int deletionIndex = -1;
    File deletionFile;

    /**
     * Called when the activity is created.
     * Reads in all recipes from saved files and stores them in the
     * <code>recipes</code> ArrayList and displays them in the ListView
     *
     * Calls the setListViewListeners method.
     *
     * @param savedInstanceState no other activity should send any info here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipes = new ArrayList<>();
        File recipeDir = new File(getFilesDir().getAbsolutePath() + "/recipes");
        if (!recipeDir.exists()) {
            recipeDir.mkdir();
        }
        for (String recipeName : recipeDir.list()) {
            recipes.add( new Recipe(this, recipeName) );
        }
        Collections.sort(recipes);

        ListView lv = (ListView) findViewById(R.id.Main_RecipeList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        lv.setAdapter(adapter);
        this.setListViewListeners(lv);
    }

    /**
     * Called when clicking the "create new recipe"-button
     * Simply changes activity to the NewRecipe_Activity.
     * Does nothing beyond that.
     *
     * @param view
     */
    public void onClickNewRecipe(View view) {
        Intent intent = new Intent(this, NewRecipe_Activity.class);
        startActivity(intent);
    }

    /**
     * Sets up listeners and methods for clicking on items in the listView
     * containing a list of all saved recipes.
     *
     * Single click results in switching to the RecipeViewer_Activity.
     * Intent adds extra info on what recipe was clicked
     *
     * Long click results in a popup asking if the user wishes to
     * delete the recipe.
     *
     * @param lv current ListView showing all saved recipes
     */
    private void setListViewListeners(ListView lv) {
        lv.setLongClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( Main_Activity.this, RecipeViewer_Activity.class );
                intent.putExtra("recipeName", recipes.get(position).getName());
                startActivity(intent);
            }
        });

        final DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletionFile.delete();
                recipes.remove(deletionIndex);
                adapter.notifyDataSetChanged();
                deletionIndex = -1;
                deletionFile = null;
            }
        };

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletionIndex = position;
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main_Activity.this);
                String recipeName = recipes.get(position).getName();
                deletionFile = new File(getFilesDir() + "/recipes/" + recipeName);
                dialog.setMessage("Are you sure you want to delete " + recipeName + "?");
                dialog.setTitle("Confirm deletion");
                dialog.setNegativeButton("No", null);
                dialog.setPositiveButton("Yes", dialogListener);
                dialog.show();
                return true;
            }
        });
    }
}
