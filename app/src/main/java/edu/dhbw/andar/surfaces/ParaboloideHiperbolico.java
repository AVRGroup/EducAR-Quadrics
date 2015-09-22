package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class ParaboloideHiperbolico extends SurfaceObject{

    public SurfaceBuffer parabHipExt;
    public SurfaceBuffer parabHipInt;
    public SurfaceBuffer parabHipWire;

	public final int slices = 20;
	public final int stacks = 20;
	public final int numCoord = slices*stacks*18;
	public final int numCoordWire = slices*stacks*3*8;
	
	public float A = 8.0f, B = 8.0f;

	public final float passoU = (float) ((8.0f)/slices);
	public final float passoV = (float) ((8.0f)/stacks);
	
	public float u = -1.0f;
	public float v = -1.0f;

	public ParaboloideHiperbolico(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parabHipExt = new SurfaceBuffer(numCoord, 0, 1);
        parabHipInt = new SurfaceBuffer(numCoord, 0, -1);
        parabHipWire = new SurfaceBuffer(numCoordWire, 1, 1);
		buildSurface();
	}
	
	public void buildSurface(){
		
		for(u = -4.0f; u < 4.0f; u+= passoU){
			for(v = -4.0f; v < 4.0f; v+= passoV){
								
				float x = coordX(u, v), y = coordY(u, v), z = coordZ(u, v);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(u, v + passoV);
				y = coordY(u, v + passoV);
				z = coordZ(u, v+passoV);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(u+passoU, v);
				y = coordY(u+passoU, v);
				z = coordZ(u+passoU, v);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(u+passoU, v+passoV);
				y = coordY(u+passoU, v+passoV);
				z = coordZ(u+passoU, v+passoV);
				Vetor d = new Vetor(x, y, z);

				parabHipWire.preencheVertices(a);
                parabHipWire.preencheVertices(b);

				parabHipWire.preencheVertices(b);
                parabHipWire.preencheVertices(d);

                parabHipWire.preencheVertices(d);
                parabHipWire.preencheVertices(c);

                parabHipWire.preencheVertices(c);
                parabHipWire.preencheVertices(a);
				
				//Normal para dentro, paraboloide interno
                //Primeiro triangulo (inferior)
                parabHipInt.preencheVertices(a);
                parabHipInt.preencheVertices(c);
                parabHipInt.preencheVertices(b);

                //Segundo triangulo (superior)
                parabHipInt.preencheVertices(d);
                parabHipInt.preencheVertices(b);
                parabHipInt.preencheVertices(c);

				//Normal para fora, paraboloide externo
				//Primeiro triangulo (inferior)
                parabHipExt.preencheVertices(a);
                parabHipExt.preencheVertices(b);
                parabHipExt.preencheVertices(c);

                //Segundo triangulo (superior)
                parabHipExt.preencheVertices(c);
                parabHipExt.preencheVertices(b);
                parabHipExt.preencheVertices(d);

                for (int i = 0; i < 6; i++){
                    parabHipExt.preencheCores(cor);
                    parabHipInt.preencheCores(cor);
                }

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = ab.vetorial(bc);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    parabHipExt.preencheNormais(normalT1);
                    parabHipInt.preencheNormais(normalT1);
                }
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = cb.vetorial(bd);
				normalT2.normaliza();

                for(int j = 0; j < 3; j++) {
                    parabHipExt.preencheNormais(normalT2);
                    parabHipInt.preencheNormais(normalT2);
                }
			}
		}
		parabHipExt.vertices.position(0);
		parabHipInt.vertices.position(0);
		parabHipExt.normais.position(0);
        parabHipInt.normais.position(0);
		parabHipExt.cores.position(0);
        parabHipInt.cores.position(0);
		parabHipWire.vertices.position(0);
		
	}
	
	public float coordX(float u, float v){
		return (float) (A*u);
	}
	
	public float coordY(float u, float v){
		return (float) (B*v);
	}
	
	public float coordZ(float u, float v){
		return (float) (A+B + (u*u)-(v*v));
	}

    @Override
    public float getParameter(){
        return this.A;
    }

    @Override
    public void setParameter(float progress){
        this.A = progress;
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

        /** PARABOLOIDE HIPERBOLICO EXTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, parabHipExt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, parabHipExt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, parabHipExt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, parabHipExt.getNumIndices());

        /** PARABOLOIDE HIPERBOLICO INTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, parabHipInt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, parabHipInt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, parabHipInt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, parabHipInt.getNumIndices());

        // Ensure we're using the program we need
        GLES20.glUseProgram(myProgram2);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        /** PARABOLOIDE HIPERBOLICO WIREFRAME**/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, parabHipWire.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, parabHipWire.getNumIndices());
    }

}
