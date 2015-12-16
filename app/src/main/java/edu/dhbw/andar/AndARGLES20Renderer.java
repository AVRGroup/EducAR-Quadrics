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


import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.interfaces.OpenGLRenderer;
import edu.dhbw.andar.interfaces.PreviewFrameSink;
import edu.dhbw.andar.util.GraphicsDebugDraw;
import edu.dhbw.andar.util.IO;
import edu.dhbw.andar.util.GraphicsUtil;

import android.R;
import android.content.res.Resources;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.opengl.GLDebugHelper;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * Sets up an AndAR OpenGL ES 2.0 renderer derived from AndARRenderer
 * @author Griffin Milsap
 *
 */
public class AndARGLES20Renderer extends AndARRenderer {
	private final String TAG = "AndARGLES20Renderer";
	
	// GLES 2.0 doesn't do matrix math for us for free.
    private float[] mProjMatrix = new float[16]; // Projection Matrix
    private float[] mVMatrix = new float[16]; // ModelView Matrix
	
	/**
	 * mode, being either GLES20.GL_RGB or GLES20.GL_LUMINANCE
	 */
	private int mode = GLES20.GL_RGB;
	
	/**
	 * the default constructor
	 * @param res Resources
	 * @param markerInfo The ARToolkit instance
	 * @param activity The spawning activity
	 */
	public AndARGLES20Renderer(Resources res, ARToolkit markerInfo, AndARActivity activity)  {
		super(res, markerInfo, activity);
		Log.i( TAG, "Using GLES Version 2.0" );
	}
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		//register unchaught exception handler
		Thread.currentThread().setUncaughtExceptionHandler(activity); 

		markerInfo.initGL(null);
		if(customRenderer != null)
			customRenderer.initGL(null);
				
		// Tell GL what to look at
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0, 0.0f, 0.0f, 5.0f, 0f, 1.0f, 0.0f);
		
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 glUnused) {
		if(customRenderer != null)
			customRenderer.setupEnv(null);
		
		markerInfo.predraw(null);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		markerInfo.draw(null);
		
		if(customRenderer != null)
			customRenderer.draw(null);
		
	}

	/* 
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		aspectRatio = (float)width/(float)height;
		
		Matrix.orthoM(mProjMatrix, 0, -100.0f*aspectRatio, 100.0f*aspectRatio, -100.0f, 100.0f, 1.0f, -1.0f);
        Matrix.setIdentityM(mVMatrix, 0);

		markerInfo.setScreenSize(width, height);
		screenHeight = height;
		screenWidth = width;
	}
	
	/**
	 * sets the mode(either GLES20.GL_RGB or GLES20.GL_LUMINANCE)
	 * @param pMode
	 */
	public void setMode(int pMode) {
		switch(pMode) {		
		case GLES20.GL_RGB:
		case GLES20.GL_LUMINANCE:
			this.mode = pMode;
			break;
		default:
			this.mode = GLES20.GL_RGB;
			break;
		}
		if(pMode != this.mode)
			isTextureInitialized = false;
	}
}

