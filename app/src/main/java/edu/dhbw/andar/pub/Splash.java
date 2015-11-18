package edu.dhbw.andar.pub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import edu.dhbw.andar.AndARActivity;
import getcomp.educar.quadrics.R;

/**
 * Created by Lidiane on 17/04/2015.
 */
public class Splash extends AndARActivity implements Runnable {

    private final int DELAY = 2500;

    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.splash_layout);


        Handler handler = new Handler();
        handler.postDelayed(this, DELAY);
    }

    public void run() {
        Intent intent = new Intent(getApplicationContext(), CustomActivity.class);
        startActivity(intent);
        finish();
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        ex.getCause().printStackTrace();
        Log.e("AndAR EXCEPTION", ex.getMessage());
        finish();
    }
}
