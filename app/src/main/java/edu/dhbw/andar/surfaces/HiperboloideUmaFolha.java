package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class HiperboloideUmaFolha extends SurfaceObject{

	public SurfaceBuffer hiperbUmaExt;
	public SurfaceBuffer hiperbUmaInt;
	public SurfaceBuffer hiperbUmaWire;

	public final int slices = 30;
	public final int stacks = 30;

	public final float passoU = (float) (3.0f/stacks);
	public final float passoV = (float) ((2*Math.PI)/slices);
	
	public float u = -1.5f;
	public float v = 0.0f;
		
	public final int numCoord = slices*(stacks+1)*3*6;
	public final int numCoordWire = slices*(stacks+1)*3*8;

	public float A = 10.0f;
	public float B = 10.0f;
	public float C = 10.0f;
	public float Xo = 0.0f;
	public float Yo = 0.0f;
	public float Zo = 0.0f;

	public HiperboloideUmaFolha(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
		super(name, patternName, markerWidth, markerCenter, renderer);

        parameters[0] = 10.0f;
        parameters[1] = 10.0f;
        parameters[2] = 10.0f;

        max_progress = 40;

		if(hiperbUmaInt != null){
			hiperbUmaInt = null;
			hiperbUmaExt = null;
			hiperbUmaWire = null;
		}

		hiperbUmaInt = new SurfaceBuffer(numCoord, 0, -1);
		hiperbUmaExt = new SurfaceBuffer(numCoord, 0, 1);
		hiperbUmaWire = new SurfaceBuffer(numCoordWire, 1, 1);

		buildSurface();
	}
	
	public void buildSurface(){
		hiperbUmaExt.clearBuffers();
		hiperbUmaInt.clearBuffers();
		hiperbUmaWire.vertices.clear();
		
		for(u = -1.5f; u < 1.5f; u+=passoU){
			for(v = 0.0f; v < 2*Math.PI; v+= passoV){
				
				float x = coordX(v, u), y = coordY(v, u), z = coordZ(v, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(v + passoV, u);
				y = coordY(v + passoV, u);
				z = coordZ(v + passoV, u);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(v, u + passoU);
				y = coordY(v, u + passoU);
				z = coordZ(v, u+passoU);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(v+passoV, u+passoU);
				y = coordY(v+passoV, u+passoU);
				z = coordZ(v+passoV, u+passoU);
				Vetor d = new Vetor(x, y, z);

				hiperbUmaWire.preencheVertices(a);
				hiperbUmaWire.preencheVertices(b);

				hiperbUmaWire.preencheVertices(b);
				hiperbUmaWire.preencheVertices(d);

				hiperbUmaWire.preencheVertices(d);
				hiperbUmaWire.preencheVertices(c);

				hiperbUmaWire.preencheVertices(c);
				hiperbUmaWire.preencheVertices(a);

				//Normal para fora, paraboloide externo
				//Primeiro triangulo (inferior)
				hiperbUmaExt.preencheVertices(a);
				hiperbUmaExt.preencheVertices(b);
				hiperbUmaExt.preencheVertices(c);

				//Segundo triangulo (superior)
				hiperbUmaExt.preencheVertices(c);
				hiperbUmaExt.preencheVertices(b);
				hiperbUmaExt.preencheVertices(d);

				//Normal para dentro, paraboloide interno
				//Primeiro triangulo (inferior)
				hiperbUmaInt.preencheVertices(a);
				hiperbUmaInt.preencheVertices(c);
				hiperbUmaInt.preencheVertices(b);

				//Segundo triangulo (superior)
				hiperbUmaInt.preencheVertices(b);
				hiperbUmaInt.preencheVertices(c);
				hiperbUmaInt.preencheVertices(d);

				for (int i = 0; i < 6; i++){
					hiperbUmaExt.preencheCores(cor);
					hiperbUmaInt.preencheCores(cor);
				}

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = ab.vetorial(bc);
				normalT1.normaliza();

				for(int j = 0; j < 3; j++) {
					hiperbUmaExt.preencheNormais(normalT1);
					hiperbUmaInt.preencheNormais(normalT1);
				}
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = cb.vetorial(bd);
				normalT2.normaliza();

				for(int j = 0; j < 3; j++) {
					hiperbUmaExt.preencheNormais(normalT2);
					hiperbUmaInt.preencheNormais(normalT2);
				}
			}
		}
		hiperbUmaExt.vertices.position(0);
		hiperbUmaInt.vertices.position(0);
		hiperbUmaExt.normais.position(0);
		hiperbUmaInt.normais.position(0);
		hiperbUmaExt.cores.position(0);
		hiperbUmaInt.cores.position(0);
		hiperbUmaWire.vertices.position(0);
	}
	
	public float coordX(float v, float u){
		return (float) (Xo + (parameters[0] * Math.cosh(u)*Math.cos(v)));
	}
	
	public float coordY(float v, float u){
		return (float) (Yo + (parameters[1] * Math.cosh(u)*Math.sin(v)));
	}
	
	public float coordZ(float v, float u){
		return (float) (2 * parameters[2] + (Zo + (parameters[2] * Math.sinh(u))));
	}

	@Override
	public int getParameter(){
		return (int)parameters[index];
	}

	@Override
	public int getMaxProgress(){
		return this.max_progress;
	}

    @Override
    public void setParameter(float progress){
        this.parameters[index] = progress;
    }

	@Override
	public synchronized void draw( GL10 glUnused ) {
		if(!initialized) {
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
		}

		// Let the object draw

		/** HIPERBOLOIDE UMA FOLHA EXTERNO **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, hiperbUmaExt.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		//aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, hiperbUmaExt.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);

		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
				0, hiperbUmaExt.getNormals());

		GLES20.glEnableVertexAttribArray(mNormalHandle);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hiperbUmaExt.getNumIndices());

		/** HIPERBOLOIDE UMA FOLHA INTERNO **/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, hiperbUmaInt.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		//aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				0, hiperbUmaInt.getCores());
		GLES20.glEnableVertexAttribArray(mColorHandle);

		// Pass in the normal information
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
				0, hiperbUmaInt.getNormals());

		GLES20.glEnableVertexAttribArray(mNormalHandle);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hiperbUmaInt.getNumIndices());

		// Ensure we're using the program we need
		GLES20.glUseProgram(myProgram2);

		if( glCameraMatrixBuffer != null) {
			// Transform to where the marker is
			GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
			GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
			GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
		}

		/** HIPERBOLOIDE UMA FOLHA WIREFRAME**/
		// Pass in the position information
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, hiperbUmaWire.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, hiperbUmaWire.getNumIndices());
	}
}
