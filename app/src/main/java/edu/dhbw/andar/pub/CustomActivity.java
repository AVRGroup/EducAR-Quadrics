package edu.dhbw.andar.pub;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.view.View;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.surfaces.Cone;
import edu.dhbw.andar.surfaces.Elipsoide;
import edu.dhbw.andar.surfaces.HiperboloideDuasFolhas;
import edu.dhbw.andar.surfaces.HiperboloideUmaFolha;
import edu.dhbw.andar.surfaces.Paraboloide;
import edu.dhbw.andar.surfaces.ParaboloideHiperbolico;
import getcomp.educar.quadrics.R;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.widget.RadioButton;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class CustomActivity extends AndARActivity {
		
	private static int superficie = 1;

    private SeekBar seekBar = null;

    private TextView textView = null;

	SurfaceObject rendedObj = null;

    SurfaceObject elipsoide, cone, paraboloide, hiperb_uma, hiperb_duas, paraboloide_hiperb;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        CriaSuperficies();

        CriaScaleBar();

        CriaBotoes();

		DesenhaSuperficie();		
	}

    public void CriaSuperficies(){
        elipsoide = new Elipsoide("elipsoide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
        cone = new Cone("cone", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
        paraboloide = new Paraboloide("paraboloide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
        hiperb_uma = new HiperboloideUmaFolha("hiperbuma", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
        hiperb_duas = new HiperboloideDuasFolhas("hiperbduas", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
        paraboloide_hiperb = new ParaboloideHiperbolico("parabhip", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());

    }

    public void CriaScaleBar(){

        seekBar = (SeekBar) findViewById(R.id.scale_bar);
        textView = (TextView) findViewById(R.id.txt_progress);

        textView.setText("Valor: " + seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            int progress = 10;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Valor: " + progress);
                rendedObj.setParameter(progress);
                rendedObj.buildSurface();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rd_a:
                if (checked)
                    rendedObj.index = 0;
                    break;
            case R.id.rd_b:
                if (checked)
                    rendedObj.index = 1;
                    break;
            case R.id.rd_c:
                if (checked)
                    rendedObj.index = 2;
                    break;
        }
    }

    public void Visibilidade(){
        /*seekbar_layout = (View) findViewById(R.id.scale_bar);

        if(seekbar_layout.getVisibility() == View.GONE) {
            seekbar_layout.setVisibility(View.VISIBLE);
        }else{
            seekbar_layout.setVisibility(View.GONE);
        }*/
    }

    public void CriaBotoes(){
        ImageButton btnElipsoide = (ImageButton) findViewById(R.id.btn_elipsoide);
        ImageButton btnCone = (ImageButton) findViewById(R.id.btn_cone);
        ImageButton btnParaboloide = (ImageButton) findViewById(R.id.btn_paraboloide);
        ImageButton btnHiperb_uma = (ImageButton) findViewById(R.id.btn_hiperb_uma);
        ImageButton btnHiperb_duas = (ImageButton) findViewById(R.id.btn_hiperb_duas);
        ImageButton btnParaboloide_hiperb = (ImageButton) findViewById(R.id.btn_paraboloide_hiperb);

        //Evento ao clicar no ImageButton
        btnElipsoide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 1;
                DesenhaSuperficie();
            }
        });

        btnCone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 2;
                DesenhaSuperficie();
            }
        });

        btnParaboloide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 3;
                DesenhaSuperficie();
            }
        });

        btnHiperb_uma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 4;
                DesenhaSuperficie();
            }
        });

        btnHiperb_duas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 5;
                DesenhaSuperficie();
            }
        });

        btnParaboloide_hiperb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 6;
                DesenhaSuperficie();
            }
        });
    }

    public void DesenhaSuperficie(){
		try {
            if(rendedObj != null) {
                super.getArtoolkit().unregisterARObject(rendedObj);
                rendedObj = null;
            }

			if( super.isGLES20() ) {
				switch(superficie) {
				case 1:
                    rendedObj = elipsoide;
					//rendedObj = new Elipsoide("elipsoide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
                    break;
				case 2:
                    rendedObj = cone;
					//rendedObj = new Cone("cone", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 3:
                    rendedObj = paraboloide;
					//rendedObj =  new Paraboloide("paraboloide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 4:
                    rendedObj = hiperb_uma;
					//rendedObj = new HiperboloideUmaFolha("hiperbuma", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 5:
                    rendedObj = hiperb_duas;
					//rendedObj = new HiperboloideDuasFolhas("hiperbduas", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 6:
                    rendedObj = paraboloide_hiperb;
					//rendedObj =  new ParaboloideHiperbolico("parabhip", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				default:
                    rendedObj = elipsoide;
					//rendedObj =  new Elipsoide("test", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
				}
				
				super.getArtoolkit().registerARObject(rendedObj);
			}
		} catch (AndARException ex){
			//handle the exception, that means: show the user what happened
            Log.e("AndAR EXCEPTION", ex.getMessage());
		}
	}

	/**
	 * Inform the user about exceptions that occurred in background threads.
	 * This exception is rather severe and can not be recovered from.
	 * Inform the user and shut down the application.
	 */

	public void uncaughtException(Thread thread, Throwable ex) {
        ex.getCause().printStackTrace();
		Log.e("AndAR EXCEPTION", ex.getMessage());
		finish();
	}	

}
