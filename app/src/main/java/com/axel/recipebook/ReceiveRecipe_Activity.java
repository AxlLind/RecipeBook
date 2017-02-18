package com.axel.recipebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * An activity where the user receive a recipe from a friend.
 * The friend first has to first upload their recipe to the database (via the ShareRecipe_Activity).
 * Works via a firebase database which the user reads the recipe from.
 *
 * User has to fill in a download code which the other user enters when uploading the recipe.
 *
 * @author Axel Lindeberg
 */
public class ReceiveRecipe_Activity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dataref;
    String dlName;

    /**
     * Called when activity is created.
     * Grabs a reference to the firebase database.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_recipe_);

        database = FirebaseDatabase.getInstance();
        dataref = database.getReference();
    }

    /**
     * Called when clicking the "Download Recipe"-button.
     * Checks if the user has entered a download code, shows error dialog if not.
     * Checks the database for the download code, shows error dialog if it cannot find
     * the download code.
     *
     * If it passes the tests above it downloads and saves the recipe from the database
     * and the deletes the recipe from the database.
     *
     * @param view
     */
    public void onClickDownload(View view) {
        EditText et = (EditText) findViewById(R.id.DownloadName_ReceiveActvitiy);
        dlName = et.getText().toString().trim();
        if (dlName.equals("")) {
            showErrorDialog("Error downloading", getString(R.string.downloadError_NoDLName));
            return;
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe r = dataSnapshot.getValue(Recipe.class);
                if (r == null) {
                    showErrorDialog("Error downloading", getString(R.string.downloadError_NoRecipe));
                    return;
                }
                r.saveToFile(ReceiveRecipe_Activity.this);
                dataref.child(dlName).removeValue();
                showSuccessDialog(r.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        dataref.child(dlName).addListenerForSingleValueEvent(listener);
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
     * Shows a dialog when the recipe was successfully downloaded.
     *
     * @param recipeName
     */
    private void showSuccessDialog(String recipeName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity( new Intent(ReceiveRecipe_Activity.this, Main_Activity.class));
            }
        };

        final DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity( new Intent(ReceiveRecipe_Activity.this, Main_Activity.class));
            }
        };

        dialog.setTitle("Success!");
        dialog.setMessage("The recipe '"+ recipeName +"' downloaded successfully.");
        dialog.setPositiveButton("Ok", listener);
        dialog.setOnDismissListener(dismissListener);
        dialog.show();
    }
}
