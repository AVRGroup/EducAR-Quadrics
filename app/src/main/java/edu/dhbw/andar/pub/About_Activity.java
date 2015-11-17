package edu.dhbw.andar.pub;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
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

        sobre = (TextView)findViewById(R.id.btnSobre);
        ok = (TextView)findViewById(R.id.btnOk);
    }

    public void ajudaOKOnClick(View v){
        Intent mainActivityIntent = new Intent();
        mainActivityIntent.setClass(About_Activity.this, CustomActivity.class);
        startActivity(mainActivityIntent);
        //finish();
    }

    public void ajudaSobreOnClick(View v){
        Intent sobreActivityIntent = new Intent();
        sobreActivityIntent.setClass(About_Activity.this, SobreActivity.class);
        startActivity(sobreActivityIntent);
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Id correspondente ao botão Up/Home da actionbar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
