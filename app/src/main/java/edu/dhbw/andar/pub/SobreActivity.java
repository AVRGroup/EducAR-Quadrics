package edu.dhbw.andar.pub;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import getcomp.educar.quadrics.R;

public class SobreActivity extends Activity {

    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sobre);

        ok = (TextView)findViewById(R.id.txtOk);
    }

    public void ajudaOKOnClick(View v){
        ok.setBackgroundColor(getResources().getColor(R.color.cinza_claro));
        Intent mainActivityIntent = new Intent();
        mainActivityIntent.setClass(SobreActivity.this, CustomActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

}
