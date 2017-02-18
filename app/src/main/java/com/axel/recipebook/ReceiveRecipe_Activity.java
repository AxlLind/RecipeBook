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

public class ReceiveRecipe_Activity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dataref;
    String dlName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_recipe_);

        database = FirebaseDatabase.getInstance();
        dataref = database.getReference();
    }

    public void onClickDownload(View view) {
        EditText et = (EditText) findViewById(R.id.DownloadName_ReceiveActvitiy);
        dlName = et.getText().toString().trim();
        if (dlName.equals("")) {
            showErrorDialog("Error downloading", "You have need to specify a download code.");
            return;
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe r = dataSnapshot.getValue(Recipe.class);
                if (r == null) {
                    showErrorDialog("Error downloading", "Cannot find recipe in database. Make sure the other user sends it first.");
                    return;
                }
                r.saveToFile(ReceiveRecipe_Activity.this);
                dataref.child("sendingHub").child(dlName).removeValue();
                showSuccessDialog(r.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        dataref.child("sendingHub").child(dlName).addListenerForSingleValueEvent(listener);
    }

    private void showErrorDialog(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok", null);
        dialog.show();
    }

    private void showSuccessDialog(String recipeName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity( new Intent(ReceiveRecipe_Activity.this, Main_Activity.class));
            }
        };

        final DialogInterface.OnDismissListener listenerDismiss = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity( new Intent(ReceiveRecipe_Activity.this, Main_Activity.class));
            }
        };

        dialog.setTitle("Success!");
        dialog.setMessage("The recipe '"+ recipeName +"' downloaded successfully.");
        dialog.setPositiveButton("Ok", listener);
        dialog.setOnDismissListener(listenerDismiss);
        dialog.show();
    }
}
