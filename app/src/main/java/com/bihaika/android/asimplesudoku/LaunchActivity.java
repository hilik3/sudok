package com.bihaika.android.asimplesudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    Button button_start;
    Button button_policy;
    Button button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        init();
        setOnClickListeners();
    }

    private void init(){
        button_start = findViewById(R.id.button_start);
        button_policy = findViewById(R.id.button_policy);
        button_settings = findViewById(R.id.button4);
    }


    private boolean setOnClickListeners(){
        boolean val = true;

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(LaunchActivity.this, GameActivity.class);
                startActivity(settings);
            }
        });

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(LaunchActivity.this, SettingsActivity.class);
                startActivity(settings);
            }
        });



        button_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch policy
            }
        });

        return val;
    }

}
