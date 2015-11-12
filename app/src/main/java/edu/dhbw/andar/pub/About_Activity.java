package edu.dhbw.andar.pub;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import getcomp.educar.quadrics.R;

public class About_Activity extends Activity {

    TextView sobre = (TextView)findViewById(R.id.txtSobre);
    TextView ok = (TextView)findViewById(R.id.txtOk);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_about_);
    }



}
