package com.axel.recipebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DeflaterInputStream;

/**
 * An activity where the user can send a recipe to a friend.
 * Works by uploading the recipe to a firebase database which the friend then
 * reads from.
 */
public class ShareRecipe_Activity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dataref;
    private ArrayList<Recipe> recipes;
    private Recipe selectedRecipe;

    /**
     * Called when the activity is created.
     * Reads in every saved recipe and populates a spinner view object where
     * the user can select which recipe to send.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recipe_);

        database = FirebaseDatabase.getInstance();
        dataref = database.getReference();

        File recipeDir = new File(getFilesDir().getAbsolutePath() + "/recipes");
        recipes = new ArrayList<>();
        for (File f : recipeDir.listFiles()) {
            recipes.add( new Recipe(f) );
        }
        Collections.sort(recipes);
        recipes.add( new Recipe("Select Recipe...") ); // BS solution?

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, recipes);
        Spinner spinner = (Spinner) findViewById(R.id.RecipeSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(recipes.size() - 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == recipes.size() - 1) {
                    selectedRecipe = null;
                    return;
                }
                selectedRecipe = recipes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRecipe = null;
            }
        });
    }

    /**
     * Called when clicking the "Share Recipe"-button.
     * Checks that the user has selected a recipe and has filled in a download code.
     * Shows an error dialog if the user has not.
     * Otherwise it uploads the selected recipe to the firebase database and shows a
     * success-dialog.
     *
     * @param view
     */
    public void onClickShare(View view) {
        EditText et = (EditText) findViewById(R.id.DownloadNameEditText);
        String dlName = et.getText().toString().trim();
        if (selectedRecipe == null) {
            showErrorDialog("Error Sending", "You have to select a recipe to send.");
            return;
        } else if (dlName.equals("")) {
            showErrorDialog("Error Sending", "You need to enter a download code.");
            return;
        }
        dataref.child(dlName).setValue(selectedRecipe);
        showSentDialog();
    }

    /**
     * Shows an error dialog on the screen.
     *
     * @param title title of the dialog
     * @param msg message of the dialog
     */
    private void showErrorDialog(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok", null);
        dialog.show();
    }

    /**
     * Shows a dialog when the selected recipe was successfully uploaded to the database.
     */
    private void showSentDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity( new Intent(ShareRecipe_Activity.this, Main_Activity.class) );
            }
        };

        final DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity( new Intent(ShareRecipe_Activity.this, Main_Activity.class));
            }
        };

        dialog.setTitle("Recipe sent!");
        dialog.setPositiveButton("Ok", listener);
        dialog.setOnDismissListener(dismissListener);
        dialog.show();
    }
}
