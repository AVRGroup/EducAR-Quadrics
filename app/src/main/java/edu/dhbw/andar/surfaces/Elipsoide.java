package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class Elipsoide extends SurfaceObject{
	
	public SurfaceBuffer elipsoide;
    public SurfaceBuffer elipsoideWireframe;

	public final int slices = 40;
	public final int stacks = 40;

	public final float passoU = (float) ((2*Math.PI)/stacks);
	public final float passoV = (float) ((2*Math.PI)/slices);
	
	public float u = 0.0f;
	public float v = 0.0f;
		
	public final int numCoord = (slices+1)*(stacks+1)*3*6;
	public final int numCoordWire = (slices+1)*(stacks+1)*3*8;

    public Elipsoide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

		elipsoide = new SurfaceBuffer(numCoord, 0, 1);
        elipsoideWireframe = new SurfaceBuffer(numCoordWire, 1, 1);

        //Passa o fator normal do elipsoide, porque o wireframe nao tem normal
        constroiElipsoide(elipsoide.fatorNormal);
	}
	
	public void constroiElipsoide(int fatorNormal){
		
		for(u = 0.0f; u < 2*Math.PI; u+=passoU){
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

                elipsoideWireframe.preencheVertices(a);
                elipsoideWireframe.preencheVertices(b);

                elipsoideWireframe.preencheVertices(b);
                elipsoideWireframe.preencheVertices(d);

                elipsoideWireframe.preencheVertices(d);
                elipsoideWireframe.preencheVertices(c);

                elipsoideWireframe.preencheVertices(c);
                elipsoideWireframe.preencheVertices(a);

				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					//Primeiro triangulo (inferior)
					elipsoide.preencheVertices(a);
                    elipsoide.preencheVertices(c);
                    elipsoide.preencheVertices(b);

					//Segundo triangulo (superior)
                    elipsoide.preencheVertices(b);
                    elipsoide.preencheVertices(c);
                    elipsoide.preencheVertices(d);

                    for (int i = 0; i < 6; i++){
                        elipsoide.preencheCores(cor);
                    }
				}
				
				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(b, a);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(c, a);
				
				Vetor normalT1 = new Vetor();
				normalT1 = bc.vetorial(ab);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    elipsoide.preencheNormais(normalT1);
                }

				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, d);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = new Vetor();
				normalT2 = bd.vetorial(cb);
				normalT2.normaliza();

                for(int j = 0; j < 3; j++){
					elipsoide.preencheNormais(normalT2);
                }
            }
		}

        elipsoide.vertices.position(0);
		elipsoide.normais.position(0);
		elipsoide.cores.position(0);
		elipsoideWireframe.vertices.position(0);
	}

	public float coordX(float v, float u){
		return (float) (30*Math.cos(u));
    }
	
	public float coordY(float v, float u){
		return (float) (20*Math.sin(u)*Math.cos(v));
    }
	
	public float coordZ(float v, float u){
		return (float) (20.0f+(20*Math.sin(u)*Math.sin(v)));				
	}

    @Override
    public synchronized void draw( GL10 glUnused ){
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

        /** ELIPSOIDE **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, elipsoide.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, elipsoide.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, elipsoide.getNormals());

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
				false, 0, elipsoideWireframe.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, elipsoideWireframe.getNumIndices());
    }
}
