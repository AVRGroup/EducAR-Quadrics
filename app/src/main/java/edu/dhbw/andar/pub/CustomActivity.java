package edu.dhbw.andar.pub;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.view.View;

import edu.dhbw.andar.ARToolkit;
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

    private ARToolkit art;

    private RadioButton rd_a, rd_b, rd_c;

	SurfaceObject rendedObj = null;

    SurfaceObject elipsoide, cone, paraboloide, hiperb_uma, hiperb_duas, paraboloide_hiperb;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        art = super.getArtoolkit();

        CriaBotoes();

		DesenhaSuperficie();

        CriaScaleBar();
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

        seekBar.setMax(rendedObj.getMaxProgress());
        seekBar.setProgress(rendedObj.getParameter());

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            int progress = rendedObj.getParameter();

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
                if(rendedObj != null) {
                    try {
                        art.unregisterARObject(rendedObj);
                        rendedObj.setParameter(progress);
                        rendedObj.buildSurface();
                        art.registerARObject(rendedObj);
                    } catch (AndARException ex){
                        //handle the exception, that means: show the user what happened
                        Log.e("AndAR EXCEPTION", ex.getMessage());
                    }
                }
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
                    textView.setText("Valor: " + rendedObj.getParameter());
                    seekBar.setProgress(rendedObj.getParameter());
                    break;
            case R.id.rd_b:
                if (checked)
                    rendedObj.index = 1;
                    textView.setText("Valor: " + rendedObj.getParameter());
                    seekBar.setProgress(rendedObj.getParameter());
                    break;
            case R.id.rd_c:
                if (checked)
                    rendedObj.index = 2;
                    textView.setText("Valor: " + rendedObj.getParameter());
                    seekBar.setProgress(rendedObj.getParameter());
                    break;
        }
    }

    public void CriaBotoes(){
        ImageButton btnElipsoide = (ImageButton) findViewById(R.id.btn_elipsoide);
        ImageButton btnCone = (ImageButton) findViewById(R.id.btn_cone);
        ImageButton btnParaboloide = (ImageButton) findViewById(R.id.btn_paraboloide);
        ImageButton btnHiperb_uma = (ImageButton) findViewById(R.id.btn_hiperb_uma);
        ImageButton btnHiperb_duas = (ImageButton) findViewById(R.id.btn_hiperb_duas);
        ImageButton btnParaboloide_hiperb = (ImageButton) findViewById(R.id.btn_paraboloide_hiperb);

        rd_a = (RadioButton)findViewById(R.id.rd_a);
        rd_b = (RadioButton)findViewById(R.id.rd_b);
        rd_c = (RadioButton)findViewById(R.id.rd_c);


        //Evento ao clicar no ImageButton
        btnElipsoide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 1;
                rd_c.setVisibility(View.VISIBLE);
                DesenhaSuperficie();
            }
        });

        btnCone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 2;
                rd_c.setVisibility(View.GONE);
                DesenhaSuperficie();
            }
        });

        btnParaboloide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 3;
                rd_c.setVisibility(View.GONE);
                DesenhaSuperficie();
            }
        });

        btnHiperb_uma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 4;
                rd_c.setVisibility(View.VISIBLE);
                DesenhaSuperficie();
            }
        });

        btnHiperb_duas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 5;
                rd_c.setVisibility(View.VISIBLE);
                DesenhaSuperficie();
            }
        });

        btnParaboloide_hiperb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                superficie = 6;
                rd_c.setVisibility(View.GONE);
                DesenhaSuperficie();
            }
        });
    }

    public void DesenhaSuperficie(){
		try {
            if(rendedObj != null) {
                art.unregisterARObject(rendedObj);
                rendedObj = null;
            }

			if( super.isGLES20() ) {
				switch(superficie) {
				case 1:
					rendedObj = new Elipsoide("elipsoide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
                    break;
				case 2:
					rendedObj = new Cone("cone", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 3:
					rendedObj =  new Paraboloide("paraboloide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 4:
					rendedObj = new HiperboloideUmaFolha("hiperbuma", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 5:
					rendedObj = new HiperboloideDuasFolhas("hiperbduas", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 6:
					rendedObj =  new ParaboloideHiperbolico("parabhip", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				default:
					rendedObj =  new Elipsoide("test", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
				}

                art.registerARObject(rendedObj);
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
