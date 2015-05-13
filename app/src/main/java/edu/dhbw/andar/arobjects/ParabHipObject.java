package edu.dhbw.andar.arobjects;

import android.opengl.GLES20;
import edu.dhbw.andar.ARGLES20Object;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.surfaces.ParabHiperbolicoWire;
import edu.dhbw.andar.surfaces.ParaboloideHiperbolico;
import edu.dhbw.andar.util.GraphicsUtil;

public class ParabHipObject extends ARGLES20Object{
	
	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;
	
	/** This will be used to pass in model normal information. */
	private int mNormalHandle;
	
	private ParaboloideHiperbolico parab;
	private ParaboloideHiperbolico parabInt;
	private ParabHiperbolicoWire parabWire;
	
	public ParabHipObject(String name, String patternName, double markerWidth,
			double[] markerCenter, AndARGLES20Renderer renderer) {
		super(name, patternName, markerWidth, markerCenter, renderer);
		
		parab = new ParaboloideHiperbolico(1);
		parabInt = new ParaboloideHiperbolico(-1);
		parabWire = new ParabHiperbolicoWire(1);
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
		
		/** PARABOLOIDE HIPERBOLICO INTERNO **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, parabInt.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, parabInt.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, parabInt.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, parabInt.getNumIndices());
		
		/** PARABOLOIDE HIPERBOLICO EXTERNO **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, parab.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, parab.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, parab.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, parab.getNumIndices());
		
		/** PARABOLOIDE HIPERBOLICO WIREFRAME **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, parabWire.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, parabWire.getWire());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, parabWire.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha paraboloide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, parabWire.getNumIndices());
		
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
