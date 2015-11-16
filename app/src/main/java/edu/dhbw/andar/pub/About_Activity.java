package edu.dhbw.andar.pub;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import getcomp.educar.quadrics.R;

public class About_Activity extends Activity {

    private TextView sobre, ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_about_);

        sobre = (TextView)findViewById(R.id.txtSobre);
        ok = (TextView)findViewById(R.id.txtOk);
    }

    public void ajudaOKOnClick(View v){
        ok.setBackgroundColor(getResources().getColor(R.color.cinza_claro));
        Intent mainActivityIntent = new Intent();
        mainActivityIntent.setClass(About_Activity.this, CustomActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    public void ajudaSobreOnClick(View v){
        sobre.setBackgroundColor(getResources().getColor(R.color.cinza_claro));
        Intent sobreActivityIntent = new Intent();
        sobreActivityIntent.setClass(About_Activity.this, SobreActivity.class);
        startActivity(sobreActivityIntent);
        finish();
    }
}
