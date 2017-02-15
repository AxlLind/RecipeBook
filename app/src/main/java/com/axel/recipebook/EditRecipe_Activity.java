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

public class EditRecipe_Activity extends AppCompatActivity {
    Recipe recipe;

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
        recipe.setIngredients(ingredients);
        recipe.saveToFile(this);
        Intent intent = new Intent(this, Main_Activity.class);
        startActivity(intent);
    }
}
