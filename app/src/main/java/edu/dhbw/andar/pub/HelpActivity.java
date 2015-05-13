package edu.dhbw.andar.pub;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;

import android.widget.CheckBox;

import edu.dhbw.andar.AndARActivity;
import getcomp.educar.quadrics.R;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Lidiane on 17/04/2015.
 */
public class HelpActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    public void dismiss(View v) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (((CheckBox) findViewById(R.id.not_show)).isChecked())
            prefs.edit().putBoolean("firstLaunch", false).commit();
        startActivity(new Intent(this, CustomActivity.class));
        finish();
    }

}
