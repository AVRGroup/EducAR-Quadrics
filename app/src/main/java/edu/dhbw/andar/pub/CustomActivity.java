package edu.dhbw.andar.pub;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class CustomActivity extends AndARActivity {
		
	private static int superficie = 1;

	private ListView mDrawerList;
	private ArrayAdapter<String> mAdapter;

	ARObject rendedObj = null;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DesenhaSuperficie();		
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
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_layout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.cone:
	        	superficie = 2;
				DesenhaSuperficie();
	            return true;
	        case R.id.elipsoide:
	        	superficie = 1;
				DesenhaSuperficie();
	            return true;
	        case R.id.paraboloide:
	        	superficie = 3;
				DesenhaSuperficie();
	            return true;
	        case R.id.hiperb_uma:
	        	superficie = 4;
				DesenhaSuperficie();
	            return true;
	        case R.id.hiperb_duas:
	        	superficie = 5;
				DesenhaSuperficie();
	            return true;
	        case R.id.parab_hip:
	        	superficie = 6;
				DesenhaSuperficie();
	            return true;
	        default:
	            return true;
	    }
	}

	private void addDrawerItems() {
		String[] osArray = { "TESTE", "TESTE", "TESTE", "TESTE", "TESTE" };
		mAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, osArray);
		mDrawerList.setAdapter(mAdapter);
	}


}
