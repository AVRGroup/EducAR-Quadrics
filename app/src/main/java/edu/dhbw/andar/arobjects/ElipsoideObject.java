package edu.dhbw.andar.arobjects;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.surfaces.Elipsoide;
import edu.dhbw.andar.util.GraphicsUtil;

/**
 * Created by Lidiane on 08/06/2015.
 */
public class ElipsoideObject extends ARObject {
	protected AndARGLES20Renderer mRenderer;
	protected int myProgram;
	protected int myProgram2;
	protected int simpleProgram;
	protected int muMVMatrixHandle;
	protected int muPMatrixHandle;
	protected float[] mMVMatrix = new float[16]; // ModelView Matrix
	protected float[] mPMatrix = new float[16]; // Projection Matrix

	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;

	/** This will be used to pass in model normal information. */
	private int mNormalHandle;

	private Elipsoide elipsoide;
	private Elipsoide elipWire;

	public ElipsoideObject(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
		super(name, patternName, markerWidth, markerCenter);
		mRenderer = renderer;
		myProgram = 0;
		myProgram2 = 0;
		simpleProgram = 0;
		muMVMatrixHandle = 0;
		muPMatrixHandle = 0;

		elipsoide = new Elipsoide(1, 0);
		elipWire = new Elipsoide(1, 1);
	}

	/**
	 * Compile and load a vertex and fragment program for this object
	 * @param vspath Path relative to the "assets" directory which denotes location of the vertex shader
	 * @param fspath Path relative to the "assets" directory which denotes location of the fragment shader
	 */
	public void setProgram( String vspath, String fspath, int mode)
	{
		if (mode == 1){
			// Load and compile the program, grab the attribute for transformation matrix
			myProgram = GraphicsUtil.loadProgram(mRenderer.activity, vspath, fspath);
			muMVMatrixHandle = GLES20.glGetUniformLocation(myProgram, "uMVMatrix");
			GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uMVMatrix");
			if (muMVMatrixHandle == -1) {
				throw new RuntimeException("Requested shader does not have a uniform named uMVMatrix");
			}
			muPMatrixHandle = GLES20.glGetUniformLocation(myProgram, "uPMatrix");
			GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uPMatrix");
			if (muPMatrixHandle == -1) {
				throw new RuntimeException("Requested shader does not have a uniform named uPMatrix");
			}
		}
		else{
			// Load and compile the program, grab the attribute for transformation matrix
			myProgram2 = GraphicsUtil.loadProgram(mRenderer.activity, vspath, fspath);
			muMVMatrixHandle = GLES20.glGetUniformLocation(myProgram2, "uMVMatrix");
			GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uMVMatrix");
			if (muMVMatrixHandle == -1) {
				throw new RuntimeException("Requested shader does not have a uniform named uMVMatrix");
			}
			muPMatrixHandle = GLES20.glGetUniformLocation(myProgram2, "uPMatrix");
			GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uPMatrix");
			if (muPMatrixHandle == -1) {
				throw new RuntimeException("Requested shader does not have a uniform named uPMatrix");
			}
		}

	}

	/**
	 * Set the shader program files for this object
	 */
	public String vertexProgramPath(int mode) {
		if (mode == 1)
			return "shaders/meuVertexShader.vs";
		else
			return "shaders/simpleShader.vs";
	}

	public String fragmentProgramPath(int mode) {
		if (mode == 1)
			return "shaders/meuFragmentShader.fs";
		else
			return "shaders/simpleShader.fs";
	}

	/**
	 * Initialize the shader and transform matrix attributes
	 * @param glUnused an unused 1.0 gl context
	 */
	@Override
	public void init( GL10 glUnused ) {
		setProgram( vertexProgramPath(1), fragmentProgramPath(1), 1);
		setProgram( vertexProgramPath(0), fragmentProgramPath(0), 0);

		mPositionHandle = GLES20.glGetAttribLocation(myProgram, "aPosition");
		GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
		if (mPositionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aPosition");
		}

		mColorHandle = GLES20.glGetAttribLocation(myProgram, "a_Color");

		mNormalHandle = GLES20.glGetAttribLocation(myProgram, "a_Normal");
	}

	@Override
	public synchronized void predraw( GL10 glUnused ) {
        /*if(!initialized) {
            init(glUnused);
            initialized = true;
        }

        // Ensure we're using the program we need
        GLES20.glUseProgram(myProgram);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }*/

	}

	/**
	 * Allow the program to draw without dealing with transformations
	 * @param glUnused an unused 1.0 gl context
	 */
	@Override
	public synchronized void draw( GL10 glUnused ) {
		if(!initialized) {
			init(glUnused);
			initialized = true;
		}

		// Ensure we're using the program we need
		GLES20.glUseProgram( myProgram );

		if( glCameraMatrixBuffer != null) {
			// Transform to where the marker is
			GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
			GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
		}

		// Let the object draw

		/** ELIPSOIDE **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, elipsoide.elipsoideBuffer.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		//aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, elipsoide.elipsoideBuffer.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);

		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
				0, elipsoide.elipsoideBuffer.getNormals());

		GLES20.glEnableVertexAttribArray(mNormalHandle);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, elipsoide.getNumIndices());

		// Ensure we're using the program we need
		GLES20.glUseProgram(myProgram2);

		if( glCameraMatrixBuffer != null) {
			// Transform to where the marker is
			GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
			GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
		}

		/** ELIPSOIDE WIREFRAME**/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, elipWire.elipsoideBuffer.getWire()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, elipWire.getNumIndices());
	}
}
