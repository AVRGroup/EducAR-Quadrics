package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;
import android.os.SystemClock;
import android.util.Log;


public class Elipsoide extends SurfaceObject{
	
	public SurfaceBuffer elipsoide;
    public SurfaceBuffer elipsoideWireframe;

    //Tem que ser multiplo de 3 para fechar a superficie
	public final int slices = 24;
	public final int stacks = 24;

	public final float passoU = (float) ((2*Math.PI)/slices);
	public final float passoV = (float) ((Math.PI)/stacks);
		
	public final int numCoord = (slices)*(stacks)*3*6;
	public final int numCoordWire = (slices)*(stacks)*3*8;

    public float A = 10.0f;
    public float B = 10.0f;
    public float C = 10.0f;
    public final float Xo = 0.0f;
    public final float Yo = 0.0f;
    public final float Zo = 0.0f;

    public float dir = 1;

    public Elipsoide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

		elipsoide = new SurfaceBuffer(numCoord, 0, 1);
        elipsoideWireframe = new SurfaceBuffer(numCoordWire, 1, 1);
        buildSurface();
	}
	
	public void buildSurface(){
        for(float v = 0.0f; v <= Math.PI-passoV; v+= passoV){
            for(float u = 0.0f; u < 2*Math.PI-passoU; u+=passoU){

				float x = coordX(v, u), y = coordY(v, u), z = coordZ(v, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(v, u + passoU);
				y = coordY(v, u + passoU);
				z = coordZ(v, u + passoU);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(v + passoV, u);
				y = coordY(v + passoV, u);
				z = coordZ(v + passoV, u);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(v + passoV, u + passoU);
				y = coordY(v + passoV, u + passoU);
				z = coordZ(v + passoV, u + passoU);
				Vetor d = new Vetor(x, y, z);

                elipsoideWireframe.preencheVertices(a);
                elipsoideWireframe.preencheVertices(b);

                elipsoideWireframe.preencheVertices(d);
                elipsoideWireframe.preencheVertices(c);

                elipsoideWireframe.preencheVertices(b);
                elipsoideWireframe.preencheVertices(d);

                elipsoideWireframe.preencheVertices(c);
                elipsoideWireframe.preencheVertices(a);

				//Normal para fora, paraboloide externo
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

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(b, a);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(c, a);
				
				Vetor normalT1 = bc.vetorial(ab);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    elipsoide.preencheNormais(normalT1);
                }

				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, d);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = bd.vetorial(cb);
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
		return (float) (Xo + A*Math.cos(u)*Math.sin(v));
    }
	
	public float coordY(float v, float u){
        return (float) (Yo + B*Math.sin(u)*Math.sin(v));
    }
	
	public float coordZ(float v, float u){
        return (float) (C + (Zo + C*Math.cos(v)));
	}

    @Override
    public float getParameter(){
        return this.A;
    }

    @Override
    public void setParameter(float progress){
        this.C = progress;
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

        /*long time = SystemClock.uptimeMillis()  % 100L;
        if(A <= 15.0 || A >= 25.0) {
            dir *= -1;
        }
        A += (dir) * time;*/

        //buildSurface();

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

		/** ELIPSOIDE WIREFRAME **/
		// Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, elipsoideWireframe.getVertices()); // 3 = Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

		// Desenha elipsoide
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, elipsoideWireframe.getNumIndices());
    }
}
