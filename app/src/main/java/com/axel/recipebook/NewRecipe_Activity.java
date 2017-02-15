package com.axel.recipebook;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * An activity where the user can create a new recipe.
 * In different fields the user can enter the name of the recipe
 * and the name of each ingredient (and amount) included in the recipe
 *
 * The user can click on a button to add more ingredients as well as
 * a button to finalize the recipe which sends the user back
 * to the Main_Activity
 *
 * @author Axel Lindeberg
 */
public class NewRecipe_Activity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * Does nothing beyond the default except changing the "softInputMode".
     * This makes the keyboard not move buttons and such on the screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * Called when the user clicks the "create"-button. Reads the string
     * value of each field and creates and saves to a file a recipe object
     * based on those values.
     *
     * Displays an error dialog box if the recipe does not have a name filled in
     * or if it does not have at least one ingredient.
     *
     * @param view
     */
    public void createRecipe(View view) {
        EditText et = (EditText) findViewById(R.id.NewRecipe_RecipeName);
        String recipeName = et.getText().toString();
        if (recipeName.equals("")) {
            showErrorDialog("Recipe needs to have a name.");
            return;
        }

        Recipe r = new Recipe(recipeName);
        TableLayout table = (TableLayout) findViewById(R.id.NewRecipe_InputTable);
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            EditText t1 = (EditText) row.getChildAt(0);
            EditText t2 = (EditText) row.getChildAt(1);
            String name = t1.getText().toString().trim();
            String amount = t2.getText().toString().trim();
            if (!name.equals("") && !amount.equals("")) {
                r.addIngredient(name, amount);
            }
        }
        if (r.getIngredients().size() == 0) {
            showErrorDialog("Recipe needs to have at least one ingredient.");
            return;
        }
        r.saveToFile(this);

        Intent intent = new Intent(this, Main_Activity.class);
        startActivity(intent);
    }

    /**
     * Adds a new row for the user to enter an other ingredient in the recipe.
     *
     * @param view
     */
    public void addIngredientRow(View view) {
        EditText ingredientName = new EditText(this);
        ingredientName.setHint("Ingredient");
        EditText ingredientAmount = new EditText(this);
        ingredientAmount.setHint("Amount");

        TableRow tr = new TableRow(this);
        tr.addView(ingredientName);
        tr.addView(ingredientAmount);

        TableLayout table = (TableLayout) findViewById(R.id.NewRecipe_InputTable);
        table.addView(tr);
    }

    /**
     * Helper method to display an error dialog when the user tries to create
     * an incomplete recipe. The parameter-string is displayed in the middle
     * of the dialog.
     *
     * @param s string displayed in the dialog
     */
    private void showErrorDialog(String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Incorrect recipe creation");
        dialog.setMessage(s);
        dialog.setNegativeButton("Ok", null);
        dialog.show();
    }
}
