package edu.dhbw.andar.pub;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.arobjects.ConeCilindricoObject;
import edu.dhbw.andar.arobjects.ElipsoideObject;
import edu.dhbw.andar.arobjects.HiperDuasObject;
import edu.dhbw.andar.arobjects.HiperbUmaObject;
import edu.dhbw.andar.arobjects.ParabHipObject;
import edu.dhbw.andar.arobjects.ParaboloideObject;
import edu.dhbw.andar.exceptions.AndARException;
import getcomp.educar.quadrics.R;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class CustomActivity extends AndARActivity {
		
	private static int superficie = 1;
	
	//ARGLES20Object rendedObj = null;
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
					rendedObj = new ElipsoideObject("elipsoide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 2:
					rendedObj = new ConeCilindricoObject("cone", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 3:
					rendedObj =  new ParaboloideObject("paraboloide", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 4:
					rendedObj = new HiperbUmaObject("hiperbuma", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 5:
					rendedObj = new HiperDuasObject("hiperbduas", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				case 6:
					rendedObj =  new ParabHipObject("parabhip", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
					break;
				default:
					rendedObj =  new ElipsoideObject("test", "avr.patt", 50.0, new double[]{0,0}, (AndARGLES20Renderer) super.getRenderer());
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
	    
//	    menu.findItem(0).setTitle("");
//	    menu.findItem(1).setTitle("");
//	    menu.findItem(2).setTitle("");
//	    menu.findItem(3).setTitle("");
//	    menu.findItem(4).setTitle("");
//	    menu.findItem(5).setTitle("");
	    
		
//		menu.add(0, R.id.cone, 0, getResources().getText(R.string.cone))
//		.setIcon(R.drawable.coneicon);
//		menu.add(0, R.id.elipsoide, 0, getResources().getText(R.string.elipsoide))
//		.setIcon(R.drawable.elipsoideicon);
//		menu.add(0, 2, 0, getResources().getText(R.string.cone))
//		.setIcon(R.drawable.screenshoticon);
		return true;
	}
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
		
//		if(item.getItemId()==R.id.elipsoide) {
//			superficie = 1;
//			DesenhaSuperficie();
//		} else if(item.getItemId()==R.id.cone) {
//			superficie = 2;
//			DesenhaSuperficie();
////			try {
////				someObject = new CustomObject
////				("test", "patt.hiro", 80.0, new double[]{0,0});
////				artoolkit.registerARObject(someObject);
////			} catch (AndARException e) {
////				e.printStackTrace();
////			}
//		}
//
//		return true;
	}

	
}
