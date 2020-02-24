package com.bihaika.android.asimplesudoku.ui.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.bihaika.android.asimplesudoku.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        prefs = getSharedPreferences("flag", MODE_PRIVATE);
        // bg sound
        mp = new MediaPlayer();
        Switch sw = findViewById(R.id.switch1);
        sw.setChecked(prefs.getBoolean("check", true));
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences.Editor editor = getSharedPreferences("flag", MODE_PRIVATE).edit();
            if (isChecked){
                editor.putBoolean("check", true);

                if (mp != null)
                    mp.setVolume(0.2f, 0.2f);
            }else {
                editor.putBoolean("check", false);
                if (mp != null)
                    mp.setVolume(0, 0);
            }
            editor.apply();
        });

        Switch sw2 = findViewById(R.id.switch2);
        sw2.setChecked(prefs.getBoolean("check2", true));
        sw2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences("flag", MODE_PRIVATE).edit();
            if (isChecked){
                editor.putBoolean("check2", true);
            }else {
                editor.putBoolean("check2", false);
            }
            editor.apply();
        });

        Button button = findViewById(R.id.exit);
        button.setOnClickListener(v -> SettingsActivity.super.onBackPressed());

    }

}