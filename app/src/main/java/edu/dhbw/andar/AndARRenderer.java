/**
	Copyright (C) 2009,2010  Tobias Domhan

    This file is part of AndOpenGLCam.

    AndObjViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndObjViewer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndObjViewer.  If not, see <http://www.gnu.org/licenses/>.
 
 */
package edu.dhbw.andar;


import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import edu.dhbw.andar.interfaces.OpenGLRenderer;
import edu.dhbw.andar.interfaces.PreviewFrameSink;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * Opens the camera and displays the output on a square (as a texture)
 * @author Tobias Domhan
 *
 */
public class AndARRenderer implements Renderer, PreviewFrameSink{
	protected Resources res;
	protected float[] textureCoords = new float[] {
			// Camera preview
			 0.0f, 0.625f,
			 0.9375f, 0.625f,
			 0.0f, 0.0f,
			 0.9375f, 0.0f			 
		};
	
	/**
	 * Light definitions
	 */
	private float[] ambientlight = {.3f, .3f, .3f, 1f};
	private float[] diffuselight = {.7f, .7f, .7f, 1f};
	private float[] specularlight = {0.6f, 0.6f, 0.6f, 1f};
	private float[] lightposition = {100.0f,-200.0f,200.0f,0.0f};
	
	protected FloatBuffer lightPositionBuffer =  makeFloatBuffer(lightposition);
	protected FloatBuffer specularLightBuffer = makeFloatBuffer(specularlight);
	protected FloatBuffer diffuseLightBuffer = makeFloatBuffer(diffuselight);
	protected FloatBuffer ambientLightBuffer = makeFloatBuffer(ambientlight);
	
	protected FloatBuffer textureBuffer;
	protected boolean frameEnqueued = false;
	protected boolean takeScreenshot = false;
	protected Bitmap screenshot;
	protected Object screenshotMonitor = new Object();
	protected boolean screenshotTaken = false;
	protected ByteBuffer frameData = null;
	protected ReentrantLock frameLock = new ReentrantLock();
	protected boolean isTextureInitialized = false;
	protected Writer log = new LogWriter();
	protected int textureSize = 256;
	protected int previewFrameWidth = 256;
	protected int previewFrameHeight = 256;
	public int screenWidth = 0;
	public int screenHeight = 0;
	protected ARToolkit markerInfo;
	protected float aspectRatio=1;
	protected OpenGLRenderer customRenderer;
	public AndARActivity activity;
	
	/**
	 * mode, being either GL10.GL_RGB or GL10.GL_LUMINANCE
	 */
	private int mode = GL10.GL_RGB;
	
	/**
	 * the default constructer
	 * @param res Resources
	 */
	public AndARRenderer(Resources res, ARToolkit markerInfo, AndARActivity activity)  {
		this.res = res;
		this.markerInfo = markerInfo;
		this.activity = activity;
	}
	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);		

		if(customRenderer != null)
			customRenderer.setupEnv(gl);
		else {
			gl.glEnable(GL10.GL_LIGHTING);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientLightBuffer);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseLightBuffer);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularLightBuffer);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
			gl.glEnable(GL10.GL_LIGHT0);
		}
		
		markerInfo.draw(gl);
		
		if(customRenderer != null)
			customRenderer.draw(gl);
	}

	/* 
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		aspectRatio = (float)width/(float)height;
		markerInfo.setScreenSize(width, height);
		screenHeight = height;
		screenWidth = width;
	}
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		Thread.currentThread().setUncaughtExceptionHandler(activity);
		
		markerInfo.initGL(gl);
		if(customRenderer != null)
			customRenderer.initGL(gl);
	}
	
	/**
	 * Make a direct NIO FloatBuffer from an array of floats
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	protected static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	/* (non-Javadoc)
	 * @see edu.dhbw.andopenglcam.interfaces.PreviewFrameSink#setNextFrame(java.nio.ByteBuffer)
	 */

	public final void setNextFrame(ByteBuffer buf) {
		this.frameData = buf;
		this.frameEnqueued = true;
	}


	/* (non-Javadoc)
	 * @see edu.dhbw.andopenglcam.interfaces.PreviewFrameSink#getFrameLock()
	 */

	public ReentrantLock getFrameLock() {
		return frameLock;
	}

	/* Set the size of the texture(must be power of two)
	 * @see edu.dhbw.andopenglcam.interfaces.PreviewFrameSink#setTextureSize()
	 */

	public void setPreviewFrameSize(int textureSize, int realWidth, int realHeight) {
		//test if it is a power of two number
		if (!GenericFunctions.isPowerOfTwo(textureSize))
			return;
		this.textureSize = textureSize;
		this.previewFrameHeight = realHeight;
		this.previewFrameWidth = realWidth;
		//calculate texture coords
		this.textureCoords = new float[] {
				// Camera preview
				 0.0f, ((float)realHeight)/textureSize,
				 ((float)realWidth)/textureSize, ((float)realHeight)/textureSize,
				 0.0f, 0.0f,
				 ((float)realWidth)/textureSize, 0.0f			 
			};		
		textureBuffer= makeFloatBuffer(textureCoords);		
	}
	
	/**
	 * sets the mode(either GL10.GL_RGB or GL10.GL_LUMINANCE)
	 * @param pMode
	 */
	public void setMode(int pMode) {
		switch(pMode) {		
		case GL10.GL_RGB:
		case GL10.GL_LUMINANCE:
			this.mode = pMode;
			break;
		default:
			this.mode = GL10.GL_RGB;
			break;
		}
		if(pMode != this.mode)
			isTextureInitialized = false;
	}

	public void setNonARRenderer(OpenGLRenderer customRenderer) {
		this.customRenderer = customRenderer;
	}

	public Bitmap takeScreenshot() {
		synchronized (screenshotMonitor) {
			screenshotTaken = false;
			takeScreenshot = true;
			while(!screenshotTaken) {
				//protect against spurios wakeups
				try {
					screenshotMonitor.wait();
				} catch (InterruptedException e) {}
			}
		}		
		return screenshot;
	}
	
	
	
}

/**
 * write stuff to Android log
 * @author Tobias Domhan
 *
 */
class LogWriter extends Writer {

    @Override public void close() {
        flushBuilder();
    }

    @Override public void flush() {
        flushBuilder();
    }

    @Override public void write(char[] buf, int offset, int count) {
        for(int i = 0; i < count; i++) {
            char c = buf[offset + i];
            if ( c == '\n') {
                flushBuilder();
            }
            else {
                mBuilder.append(c);
            }
        }
    }

    private void flushBuilder() {
        if (mBuilder.length() > 0) {
            Log.e("OpenGLCam", mBuilder.toString());
            mBuilder.delete(0, mBuilder.length());
        }
    }

    private StringBuilder mBuilder = new StringBuilder();
}
