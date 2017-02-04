package com.axel.recipebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * An activity which displays the name of a recipe at the top.
 * The ingredients and amount of each ingredient of the recipe
 * are displayed in a table covering the view.
 *
 * Takes in the name of the recipe to be displayed via the Intent
 * object from the Main_Activity.
 *
 * @author Axel Lindeberg
 */
public class RecipeViewer_Activity extends AppCompatActivity {
    Recipe viewedRecipe;
    TableLayout table;

    /**
     * Called when the activity is created. The name of the recipe
     * to be viewed should be added in the intent to this activity.
     *
     * Reads in the recipe object from the saved file. Shows the recipe
     * name in the title of the page and adds the ingredients of the
     * recipe in the table of the page.
     *
     * @param savedInstanceState contains the name of the recipe to be displayed
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        String recipeName = getIntent().getExtras().getString("recipeName");
        viewedRecipe = new Recipe(this, recipeName);

        TextView recipeNameView = (TextView) findViewById(R.id.RecipeViewer_RecipeName);
        recipeNameView.setText(viewedRecipe.getName());

        table = (TableLayout) findViewById(R.id.RecipeViewer_IngredientsTable);
        for ( Ingredient i : viewedRecipe.getIngredients() ) {
            showIngredientOnTable(i);
        }
    }

    /**
     * Adds a tableRow containing two TextView:s to the ingredient table.
     * One containing the name of the ingredient and one with the amount.
     *
     * @param i Ingredient to be displayed in the ingredient table
     */
    private void showIngredientOnTable(Ingredient i) {
        TextView ingredient = new TextView(this);
        ingredient.setText( i.getName() );
        ingredient.setTextSize( TypedValue.COMPLEX_UNIT_SP, 24 );

        TextView amount = new TextView(this);
        amount.setText( i.getAmount() );
        amount.setTextSize( TypedValue.COMPLEX_UNIT_SP, 24 );
        amount.setGravity(Gravity.RIGHT);

        CheckBox cb = new CheckBox(this);

        TableRow tr = new TableRow(this);
        tr.addView(ingredient);
        tr.addView(amount);
        tr.addView(cb);

        table.addView(tr);
    }
}
