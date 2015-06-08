package edu.dhbw.andar.arobjects;

import android.opengl.GLES20;
import edu.dhbw.andar.ARGLES20Object;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.surfaces.HiperboloideDuasFolhas;
import edu.dhbw.andar.surfaces.HiperboloideDuasWireframe;
import edu.dhbw.andar.util.GraphicsUtil;

public class HiperDuasObject extends ARGLES20Object{
	
	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;
	
	/** This will be used to pass in model normal information. */
	private int mNormalHandle;
	
	private HiperboloideDuasFolhas hipDuasFolhas;
	private HiperboloideDuasFolhas hipDuasFolhasInt;
	
	private HiperboloideDuasWireframe hipDuasWire;

	public HiperDuasObject(String name, String patternName, double markerWidth,
			double[] markerCenter, AndARGLES20Renderer renderer) {
		super(name, patternName, markerWidth, markerCenter, renderer);
		
		hipDuasFolhas = new HiperboloideDuasFolhas(1);
		hipDuasFolhasInt = new HiperboloideDuasFolhas(-1);
		
		hipDuasWire = new HiperboloideDuasWireframe(1);
	}

	@Override
	public void initGLES20() {
		mPositionHandle = GLES20.glGetAttribLocation(myProgram, "aPosition");
		GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
		if (mPositionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aPosition");
		}

		mColorHandle = GLES20.glGetAttribLocation(myProgram, "a_Color");

		mNormalHandle = GLES20.glGetAttribLocation(myProgram, "a_Normal");
		
	}

	@Override
	public void predrawGLES20() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawGLES20() {
		/** HIPERBOLOIDE DUAS FOLHAS INTERNO **/
		
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, hipDuasFolhasInt.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, hipDuasFolhasInt.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, hipDuasFolhasInt.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha hiperboloide interno
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hipDuasFolhasInt.getNumIndices());
				
		/** HIPERBOLOIDE DUAS FOLHAS EXTERNO **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, hipDuasFolhas.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, hipDuasFolhas.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, hipDuasFolhas.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		// Desenha hiperboloide externo
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hipDuasFolhas.getNumIndices());
		
		/** HIPERBOLOIDE DUAS FOLHAS WIREFRAME **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
		false, 0, hipDuasWire.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Pass in the color information
		//aten��o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, hipDuasWire.getWire());
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true, 
				0, hipDuasWire.getNormals());
		
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		

		// Desenha hiperboloide wireframe
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, hipDuasWire.getNumIndices());
		
	}

	/**
	 * Set the shader program files for this object
	 */
	@Override
	public String vertexProgramPath(int mode) {
		if (mode == 1)
			return "shaders/meuVertexShader.vs";
		return "shaders/simplecolor.vs";
	}

	@Override
	public String fragmentProgramPath(int mode) {
		if (mode == 1)
			return "shaders/meuFragmentShader.fs";
		return "shaders/simplecolor.fs";
	}

}
