package edu.dhbw.andar.arobjects;

import android.opengl.GLES20;
import edu.dhbw.andar.ARGLES20Object;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.surfaces.Paraboloide;
import edu.dhbw.andar.surfaces.ParaboloideWireframe;
import edu.dhbw.andar.util.GraphicsUtil;

public class ParaboloideObject extends ARGLES20Object{
	
	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;
	
	/** This will be used to pass in model normal information. */
	private int mNormalHandle;
	
	Paraboloide paraboloide;	
	Paraboloide paraboloideint;
	ParaboloideWireframe paraboloideWire;

	public ParaboloideObject(String name, String patternName,
			double markerWidth, double[] markerCenter,
			AndARGLES20Renderer renderer) {
		super(name, patternName, markerWidth, markerCenter, renderer);
		
		paraboloide = new Paraboloide(1);		
		paraboloideint = new Paraboloide(-1);
		paraboloideWire = new ParaboloideWireframe(1);
	}

	@Override
	public void initGLES20() {
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
		if (mPositionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aPosition");
		}

		mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");

		mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
		
	}

	@Override
	public void predrawGLES20() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawGLES20() {
		/** PARABOLOIDE WIREFRAME**/		
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, paraboloideWire.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, paraboloideWire.getWire());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, paraboloideWire.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, paraboloideWire.getNumIndices());
		
		/** PARABOLOIDE INTERNO**/
	
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, paraboloideint.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, paraboloideint.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, paraboloideint.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide interno
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, paraboloideint.getNumIndices());

		/** PARABOLOIDE**/		
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, paraboloide.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, paraboloide.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, paraboloide.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, paraboloide.getNumIndices());
		
	}

	/**
	 * Set the shader program files for this object
	 */
	@Override
	public String vertexProgramPath() {
		return "shaders/meuVertexShader.vs";
	}

	@Override
	public String fragmentProgramPath() {
		return "shaders/meuFragmentShader.fs";
	}

}
