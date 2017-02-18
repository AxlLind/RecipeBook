package com.axel.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Share_Receive_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__receive_);
    }

    public void onClickShare(View view) {
        startActivity( new Intent(this, ShareRecipe_Activity.class));
    }

    public void onClickReceive(View view) {
        startActivity( new Intent(this, ReceiveRecipe_Activity.class));
    }
}
