package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class HiperboloideDuasFolhas extends SurfaceObject{

    SurfaceBuffer hiperbDuasExt;
    SurfaceBuffer hiperbDuasInt;
    SurfaceBuffer hiperbDuasWire;

	public final int slices = 30;
	public final int stacks = 30;

	public final float passoU = (float) (2*Math.PI/slices);
	public final float passoT = (float) ((10.0f)/stacks);
	
	public float u = 0.0f;
	public float t = -3.0f;
    public float zMin = 0.0f;
		
	public final int numCoord = (slices+1)*(stacks+1)*3*6;
	public final int numCoordWire = (slices+1)*(stacks+1)*3*8;

	public HiperboloideDuasFolhas(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 5.0f;
        parameters[1] = 5.0f;
        parameters[2] = 5.0f;

        max_progress = 30;

        if(hiperbDuasExt != null){
            hiperbDuasExt = null;
            hiperbDuasInt = null;
            hiperbDuasWire = null;
        }

        hiperbDuasExt = new SurfaceBuffer(numCoord, 0, 1);
        hiperbDuasInt = new SurfaceBuffer(numCoord, 0, -1);
        hiperbDuasWire= new SurfaceBuffer(numCoordWire, 1, 1);
		buildSurface();
	}
	
	public void buildSurface(){
        hiperbDuasExt.clearBuffers();
        hiperbDuasInt.clearBuffers();
        hiperbDuasWire.clearBuffers();
		
		for(t = -5.0f; t <= 5.0f; t+= passoT){
			for(u = 0.0f; u <= 2*Math.PI; u+=passoU){

                if(t == -5.0f && u == 0.0f){
                    zMin = coordZ(t, u);
                }
				
				float x = coordX(t, u), y = coordY(t, u), z = coordZ(t, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(t + passoT, u);
				y = coordY(t + passoT, u);
				z = coordZ(t+passoT, u);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(t, u+passoU);
				y = coordY(t, u+passoU);
				z = coordZ(t, u+passoU);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(t+passoT, u+passoU);
				y = coordY(t+passoT, u+passoU);
				z = coordZ(t+passoT, u+passoU);
				Vetor d = new Vetor(x, y, z);

                hiperbDuasWire.preencheVertices(a);
				hiperbDuasWire.preencheVertices(b);

                hiperbDuasWire.preencheVertices(b);
                hiperbDuasWire.preencheVertices(d);

                hiperbDuasWire.preencheVertices(d);
                hiperbDuasWire.preencheVertices(c);

                hiperbDuasWire.preencheVertices(c);
                hiperbDuasWire.preencheVertices(a);

				//Normal para dentro, paraboloide interno
                //Primeiro triangulo (inferior)
                hiperbDuasInt.preencheVertices(a);
                hiperbDuasInt.preencheVertices(c);
                hiperbDuasInt.preencheVertices(b);

                //Segundo triangulo (superior)
                hiperbDuasInt.preencheVertices(b);
                hiperbDuasInt.preencheVertices(c);
                hiperbDuasInt.preencheVertices(d);

				//Normal para fora, paraboloide externo
                //Primeiro triangulo (inferior)
                hiperbDuasExt.preencheVertices(a);
                hiperbDuasExt.preencheVertices(b);
                hiperbDuasExt.preencheVertices(c);

                //Segundo triangulo (superior)
                hiperbDuasExt.preencheVertices(c);
                hiperbDuasExt.preencheVertices(b);
                hiperbDuasExt.preencheVertices(d);

                for (int i = 0; i < 6; i++){
                    hiperbDuasExt.preencheCores(cor);
                    hiperbDuasInt.preencheCores(cor);
                }

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = ab.vetorial(bc);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    hiperbDuasExt.preencheNormais(normalT1);
                    hiperbDuasInt.preencheNormais(normalT1);
                }
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = cb.vetorial(bd);
				normalT2.normaliza();

                for(int j = 0; j < 3; j++) {
                    hiperbDuasExt.preencheNormais(normalT2);
                    hiperbDuasInt.preencheNormais(normalT2);
                }
			}
		}
		hiperbDuasExt.vertices.position(0);
		hiperbDuasInt.vertices.position(0);
        hiperbDuasExt.normais.position(0);
        hiperbDuasInt.normais.position(0);
        hiperbDuasExt.cores.position(0);
        hiperbDuasInt.cores.position(0);
        hiperbDuasWire.vertices.position(0);
        zMin = 0.0f;
	}
	
	public float coordX(float t, float u){
		return (float) (parameters[0] * Math.cos(u)*Math.sqrt((t*t)-1));
	}
	
	public float coordY(float t, float u){
		return (float) (parameters[1] * Math.sin(u)*Math.sqrt((t*t)-1));
	}
	
	public float coordZ(float t, float u){
		return (zMin - (parameters[2] * t));
	}

    @Override
    public int getParameter(){
        return (int)this.parameters[index];
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

        /** HIPERBOLOIDE DUAS FOLHAS EXTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, hiperbDuasExt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, hiperbDuasExt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, hiperbDuasExt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hiperbDuasExt.getNumIndices());

        /** HIPERBOLOIDE DUAS FOLHAS INTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, hiperbDuasInt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, hiperbDuasInt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, hiperbDuasInt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, hiperbDuasInt.getNumIndices());

        // Ensure we're using the program we need
        GLES20.glUseProgram(myProgram2);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        /** HIPERBOLOIDE DUAS FOLHAS WIREFRAME**/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, hiperbDuasWire.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, hiperbDuasWire.getNumIndices());
    }
}
