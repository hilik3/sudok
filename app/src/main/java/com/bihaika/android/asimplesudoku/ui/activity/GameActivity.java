package com.bihaika.android.asimplesudoku.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bihaika.android.asimplesudoku.R;
import com.bihaika.android.asimplesudoku.dialog.DialogExit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends Activity {

    private final int NUM_ROTATION = 17;
    private final int WIN_PERCENT = 20;

    private Toast toast;
    private MediaPlayer mediaPlayer;
    private boolean isForeground;
    private SoundPool sounder_pool_new;
    private int btn_for_wn, sndBtn, sndCredit, sndSpin;

    private int lvl = 1;
    private int numItems = 7;
    private int credit = 500;
    private int[] bets = {10, 20, 30};
    private int[][] wins = {{2500, 1000, 500, 200, 100, 50, 20}, {5000, 2000, 1000, 400, 200, 100, 40}, {7500, 3000, 1500, 600, 300, 150, 60}};

    private SharedPreferences prefs;


    private Timer timer = new Timer();
    private TimerTask rotationTask;
    private int numRotations, object0, object1, object2;
    private int bet = 0;
    private int win = 0;
    private AnimatorSet animWin = new AnimatorSet();
    private AnimatorSet animCredit = new AnimatorSet();
    private AnimatorSet animBet = new AnimatorSet();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        // fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout parent_layout = findViewById(R.id.root);

        // preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        prefs = getSharedPreferences("flag", MODE_PRIVATE);

        // bg sound
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("sonded_Bg.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.setLooping(true);

                    if (isForeground)
                        if (prefs.getBoolean("check", true))
                            mediaPlayer.setVolume(0.2f, 0.2f);
                        else
                            mediaPlayer.setVolume(0, 0);
                    else
                        mediaPlayer.setVolume(0, 0);

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception ignored) { }

        // sound pool
        sounder_pool_new = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? new SoundPool.Builder().setMaxStreams(5).build() : new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        try {
            btn_for_wn = sounder_pool_new.load(getAssets().openFd("sounded_playw.mp3"), 1);
            sndBtn = sounder_pool_new.load(getAssets().openFd("sounded_Btn_clicl.mp3"), 1);
            sndCredit = sounder_pool_new.load(getAssets().openFd("sounded_credit_mon.mp3"), 1);
            sndSpin = sounder_pool_new.load(getAssets().openFd("sounded_spining.mp3"), 1);
        } catch (IOException ignored) { }

        // hide navigation bar listener
        findViewById(R.id.root).setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideNavigation();
            }
        });


        // random items on start
        ((ImageView) findViewById(R.id.item0)).setImageResource(getResources().getIdentifier("object_" + Math.round(Math.random() * (numItems - 1)), "drawable", getPackageName()));
        ((ImageView) findViewById(R.id.item1)).setImageResource(getResources().getIdentifier("object_" + Math.round(Math.random() * (numItems - 1)), "drawable", getPackageName()));
        ((ImageView) findViewById(R.id.item2)).setImageResource(getResources().getIdentifier("object_" + Math.round(Math.random() * (numItems - 1)), "drawable", getPackageName()));

        // bg animation
        List<Animator> animatorArrayList = new ArrayList<>();
        ObjectAnimator anim = ObjectAnimator.ofFloat(findViewById(R.id.bg), "scaleX", 1.1f);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        animatorArrayList.add(anim);
        anim = ObjectAnimator.ofFloat(findViewById(R.id.bg), "scaleY", 1.1f);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        animatorArrayList.add(anim);
        AnimatorSet animStart = new AnimatorSet();
        animStart.playTogether(animatorArrayList);
        animStart.setDuration(3000);
        animStart.start();

        // animLvl
        animatorArrayList = new ArrayList<>();
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtLvl), "scaleX", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtLvl), "scaleY", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        animWin.playTogether(animatorArrayList);
        animWin.setDuration(100);

        // animWin
        animatorArrayList = new ArrayList<>();
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtWin), "scaleX", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtWin), "scaleY", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        animWin.playTogether(animatorArrayList);
        animWin.setDuration(100);

        // animCredit
        animatorArrayList.clear();
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtCredit), "scaleX", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtCredit), "scaleY", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        animCredit.playTogether(animatorArrayList);
        animCredit.setDuration(100);

        // animBet
        animatorArrayList.clear();
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtBet), "scaleX", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        anim = ObjectAnimator.ofFloat(findViewById(R.id.txtBet), "scaleY", 0.7f);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(1);
        animatorArrayList.add(anim);
        animBet.playTogether(animatorArrayList);
        animBet.setDuration(100);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        if (mediaPlayer != null)
            mediaPlayer.setVolume(0, 0);

        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;

        if (mediaPlayer != null) {
            if (prefs.getBoolean("check", true))
                mediaPlayer.setVolume(0.2f, 0.2f);
            else
                mediaPlayer.setVolume(0, 0);
        }

    }

    @Override
    public void onBackPressed() {
        new DialogExit().dialogExit(this);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        animWin.cancel();
        animCredit.cancel();
        animBet.cancel();

        // toast
        if (toast != null)
            toast.cancel();


        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideNavigation();
    }

    // hideNavigation
    void hideNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // TOAST
    void TOAST(int mess) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, getString(mess), Toast.LENGTH_SHORT);
        ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER);
        toast.show();
    }

    // onClick
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOne:
                bet = bet < bets.length - 1 ? bet + 1 : 0;
                updateText();
                animBet.start();

                // sound
                if (isForeground)
                    if (prefs.getBoolean("check2", true))
                    sounder_pool_new.play(sndBtn, 1, 1, 0, 0, 1);
                break;
            case R.id.btnMax:
                bet = bets.length - 1;
                updateText();
                animBet.start();

                // sound
                if (isForeground)
                    if (prefs.getBoolean("check2", true))
                    sounder_pool_new.play(sndBtn, 1, 1, 0, 0, 1);
                break;
            case R.id.btnSpin:
                spin();

                // sound
                if (isForeground)
                    if (prefs.getBoolean("check2", true))
                    sounder_pool_new.play(sndBtn, 1, 1, 0, 0, 1);
                break;

            case R.id.settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                if (isForeground)
                    if (prefs.getBoolean("check2", true))
                        sounder_pool_new.play(sndBtn, 1, 1, 0, 0, 1);
                break;
        }
    }

    // spin
    void spin() {
        animCredit.start();

        // no credit
        if (credit < bets[bet]) {
            noCredit();
            return;
        }

        // credit
        credit -= bets[bet];
        updateText();

        // disable buttons
        findViewById(R.id.btnOne).setEnabled(false);
        findViewById(R.id.btnMax).setEnabled(false);
        findViewById(R.id.btnSpin).setEnabled(false);

        // sound
        if (isForeground)
            if (prefs.getBoolean("check2", true))
            sounder_pool_new.play(sndSpin, 1, 1, 0, 0, 1);

        // rotate
        numRotations = 0;
        if (rotationTask != null)
            rotationTask.cancel();
        rotationTask = new rotation();
        timer.schedule(rotationTask, 0, 100);
    }

    // rotation
    private class rotation extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                numRotations++;


                object0 = (int) Math.round(Math.random() * (numItems - 1));
                if (numRotations == NUM_ROTATION && Math.random() <= (float) WIN_PERCENT * 0.01f)
                    object1 = object2 = object0;
                else {
                    object1 = (int) Math.round(Math.random() * (numItems - 1));
                    object2 = (int) Math.round(Math.random() * (numItems - 1));
                }


                ((ImageView) findViewById(R.id.item0)).setImageResource(getResources().getIdentifier("object_" + object0, "drawable", getPackageName()));
                ((ImageView) findViewById(R.id.item1)).setImageResource(getResources().getIdentifier("object_" + object1, "drawable", getPackageName()));
                ((ImageView) findViewById(R.id.item2)).setImageResource(getResources().getIdentifier("object_" +
                        "" + object2, "drawable", getPackageName()));


                if (numRotations == NUM_ROTATION) {
                    rotationTask.cancel();

                    findViewById(R.id.btnOne).setEnabled(true);
                    findViewById(R.id.btnMax).setEnabled(true);
                    findViewById(R.id.btnSpin).setEnabled(true);


                    if (object0 == object1 && object0 == object2)
                        addMoney(wins[bet][object0]);

                    if(credit <= 0) {
                        updateLvl();
                    }
                }
            });
        }
    }

    void updateText() {
        ((TextView) findViewById(R.id.txtLvl)).setText(Integer.toString(lvl));
        ((TextView) findViewById(R.id.txtCredit)).setText(getString(R.string.money) + credit);
        ((TextView) findViewById(R.id.txtBet)).setText(getString(R.string.money) + bets[bet]);
        ((TextView) findViewById(R.id.txtWin)).setText(getString(R.string.money) + win);
    }

    void updateLvl() {
        TOAST(R.string.new_level);

        ++lvl;
        credit = 500;

        updateText();


        if (isForeground)
            sounder_pool_new.play(btn_for_wn, 1, 1, 0, 0, 1);

        animWin.start();
    }


    void noCredit() {

        if(credit > 0) {
            TOAST(R.string.no_credit);
        } else {
            updateLvl();
        }

        if (isForeground)
            sounder_pool_new.play(sndCredit, 1, 1, 0, 0, 1);


    }

    void addMoney(int money) {
        win += money;
        updateText();

        if (isForeground)
            if (prefs.getBoolean("check2", true))
            sounder_pool_new.play(btn_for_wn, 1, 1, 0, 0, 1);

        animWin.start();
    }
}