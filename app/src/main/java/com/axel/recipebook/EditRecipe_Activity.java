package com.axel.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An activity which enables the user to edit an existing
 * recipe. The user can add new ingredients, fix already existing ones or
 * remove them all together.
 *
 * Takes in the recipe object from the RecipeViewer_Activity.
 *
 * @author Axel Lindeberg
 */
public class EditRecipe_Activity extends AppCompatActivity {
    Recipe recipe;

    /**
     * Called when the activity is created. The recipe which
     * should be edited should have been added to the intent.
     * Recipe read from the intent and displayed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recipe = (Recipe) getIntent().getExtras().get("Recipe");
        TextView title = (TextView) findViewById(R.id.RecipeName_TextView);
        title.setText(recipe.getName());

        TableLayout table = (TableLayout) findViewById(R.id.EditRecipe_IngredientsTable);
        for (Ingredient i : recipe.getIngredients()) {
            this.addIngredientRow(i, table);
        }
    }

    /**
     * Adds a table row containing two EditText views.
     * One containing the name of the ingredients, the other the amount.
     *
     * @param i
     * @param table
     */
    private void addIngredientRow(Ingredient i, TableLayout table) {
        EditText e1 = new EditText(this);
        e1.setText(i.getName());
        e1.setEms(10);
        EditText e2 = new EditText(this);
        e2.setText(i.getAmount());
        e2.setEms(5);

        TableRow row = new TableRow(this);
        row.addView(e1);
        row.addView(e2);

        table.addView(row);
    }

    /**
     * Called when clicking the "Add Ingredient"-button. Creates a new row
     * in the ingredient table where you can enter a new ingredient.
     *
     * @param view
     */
    public void onClickAddIngredient(View view) {
        EditText name = new EditText(this);
        name.setHint("Ingredient");
        name.setEms(10);
        EditText amount = new EditText(this);
        amount.setHint("Amount");
        amount.setEms(5);

        TableRow row = new TableRow(this);
        row.addView(name);
        row.addView(amount);

        TableLayout table = (TableLayout) findViewById(R.id.EditRecipe_IngredientsTable);
        table.addView(row);
    }

    /**
     * Called when clicking the "Save Recipe"-button. Reads the text from every
     * EditText in the table and saves those in to the recipe and saves that recipe
     * as a file. Switches to the Main_Activity.
     *
     * @param view
     */
    public void onClickSaveRecipe(View view) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        TableLayout table = (TableLayout) findViewById(R.id.EditRecipe_IngredientsTable);
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            String name = ( (EditText) row.getChildAt(0) ).getText().toString().trim();
            String amount = ( (EditText) row.getChildAt(1) ).getText().toString().trim();
            if (!name.equals("") && !amount.equals("")) {
                ingredients.add( new Ingredient(name, amount) );
            }
        }
        recipe.setNewIngredients(ingredients);
        recipe.saveToFile(this);
        startActivity( new Intent(this, Main_Activity.class) );
    }
}
