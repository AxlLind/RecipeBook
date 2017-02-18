package com.axel.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;

import me.grantland.widget.AutofitTextView;

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
    Recipe recipe;

    /**
     * Called when the activity is created. The name of the recipe
     * to be viewed should have been added in the intent to this activity.
     *
     * Reads in the recipe object from the saved file. Shows the recipe
     * name in the title of the page and adds the ingredients of the
     * recipe in the table of the page.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        String recipeName = getIntent().getExtras().getString("recipeName");
        File recipeFile = new File(getFilesDir().getAbsolutePath() + "/recipes/" + recipeName);
        recipe = new Recipe(recipeFile);

        TextView recipeNameView = (TextView) findViewById(R.id.RecipeViewer_RecipeName);
        recipeNameView.setText(recipe.getName());

        TableLayout table = (TableLayout) findViewById(R.id.RecipeViewer_IngredientsTable);
        for ( Ingredient i : recipe.getIngredients() ) {
            showIngredientOnTable(i, table);
        }
    }

    /**
     * Called when clicking on the "Edit Recipe"-button. Switches to
     * the EditRecipe_Activity with the current viewed recipe.
     *
     * @param view
     */
    public void onClickEditRecipe(View view) {
        Intent intent = new Intent(this, EditRecipe_Activity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    /**
     * Adds a tableRow containing two TextViews to the ingredient table.
     * One containing the name of the ingredient and one with the amount.
     *
     * @param i Ingredient to be displayed in the ingredient table
     */
    private void showIngredientOnTable(Ingredient i, TableLayout table) {
        AutofitTextView name = new AutofitTextView(this);
        name.setMaxLines(1);
        name.setMaxTextSize( TypedValue.COMPLEX_UNIT_SP, 24 );
        name.setText( i.getName() );

        AutofitTextView amount = new AutofitTextView(this);
        amount.setMaxLines(1);
        amount.setMaxTextSize( TypedValue.COMPLEX_UNIT_SP, 24 );
        amount.setText( i.getAmount() );
        amount.setGravity(Gravity.RIGHT);

        CheckBox cb = new CheckBox(this);

        TableRow tr = new TableRow(this);
        tr.addView(name);
        tr.addView(amount);
        tr.addView(cb);

        table.addView(tr);
    }
}
