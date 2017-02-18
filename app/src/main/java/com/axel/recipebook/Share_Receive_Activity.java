package com.axel.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Simple activity which just functions as a hub for sending and receiving recipes.
 * It just contains two buttons two switch to two other activities.
 */
public class Share_Receive_Activity extends AppCompatActivity {

    /**
     * Called when the activity is created. Does nothing.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__receive_);
    }

    /**
     * Called when clicking the "Share Recipe"-button. Switches to ShareRecipe_Activity.
     * @param view
     */
    public void onClickShare(View view) {
        startActivity( new Intent(this, ShareRecipe_Activity.class));
    }

    /**
     * Called when clicking the "Receive Recipe"-button. Switches to ReceiveRecipe_Activity.
     * @param view
     */
    public void onClickReceive(View view) {
        startActivity( new Intent(this, ReceiveRecipe_Activity.class));
    }
}
