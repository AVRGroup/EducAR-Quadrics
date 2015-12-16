package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;

public class Paraboloide extends SurfaceObject{

	SurfaceBuffer paraboloideExt;
    SurfaceBuffer paraboloideInt;
    SurfaceBuffer paraboloideWire;

	public final int slices = 32;
	public final int stacks = 32;
	public final int numCoord = slices*stacks*18;
	public final int numCoordWire = slices*stacks*3*8;
    public final int numCoordCor = slices*stacks*4*6;
	public float theta = 0.0f;
	public float alpha = 0.0f;
	public final float passoT = (float) ((2*Math.PI)/slices);
	public final float passoA = (float) ((2*Math.PI)/stacks);
	public final float erro = 0.05f;

	public Paraboloide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

        parameters[0] = 3.0f;
        parameters[1] = 3.0f;
        parameters[2] = 0.1f;

        max_progress = 12;


        if(paraboloideExt != null){
            paraboloideExt = null;
            paraboloideInt = null;
            paraboloideWire = null;
        }

        paraboloideExt = new SurfaceBuffer(numCoord, numCoordCor, 0, 1);
        paraboloideInt = new SurfaceBuffer(numCoord, numCoordCor, 0, -1);
        paraboloideWire = new SurfaceBuffer(numCoordWire, numCoordCor, 1, 1);
		buildSurface();
	}
	
	public void buildSurface(){
        paraboloideExt.clearBuffers();
        paraboloideInt.clearBuffers();
        paraboloideWire.clearBuffers();
		
		for(theta = 0.0f; theta < 2*Math.PI-passoT+erro; theta+= passoT){
			for(alpha = 0.0f; alpha < 2*Math.PI-passoA+erro; alpha+= passoA){
				
				float x = coordX(alpha, theta), y = coordY(alpha, theta), z = ((x*x+y*y)*parameters[2]);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(alpha, theta + passoT);
				y = coordY(alpha, theta+passoT);
				z = ((x*x+y*y)*parameters[2]);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(alpha + passoA, theta);
				y = coordY(alpha+passoA, theta);
				z = ((x*x+y*y)*parameters[2]);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(alpha+passoA, theta+passoT);
				y = coordY(alpha+passoA, theta+passoT);
				z = ((x*x+y*y)*parameters[2]);
				Vetor d = new Vetor(x, y, z);

				paraboloideWire.preencheVertices(a);
                paraboloideWire.preencheVertices(b);

                paraboloideWire.preencheVertices(b);
                paraboloideWire.preencheVertices(d);

				paraboloideWire.preencheVertices(d);
                paraboloideWire.preencheVertices(c);

				paraboloideWire.preencheVertices(c);
                paraboloideWire.preencheVertices(a);

				//Normal para dentro, paraboloide interno
                //Primeiro triangulo (inferior)
                paraboloideInt.preencheVertices(a);
                paraboloideInt.preencheVertices(c);
                paraboloideInt.preencheVertices(b);

                //Segundo triangulo (superior)
                paraboloideInt.preencheVertices(d);
                paraboloideInt.preencheVertices(b);
                paraboloideInt.preencheVertices(c);

				//Normal para fora, paraboloide externo
                //Primeiro triangulo (inferior)
                paraboloideExt.preencheVertices(a);
                paraboloideExt.preencheVertices(b);
                paraboloideExt.preencheVertices(c);

                //Segundo triangulo (superior)
                paraboloideExt.preencheVertices(c);
                paraboloideExt.preencheVertices(b);
                paraboloideExt.preencheVertices(d);

                for (int i = 0; i < 6; i++){
                    paraboloideExt.preencheCores(cor);
                    paraboloideInt.preencheCores(cor);
                }

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = ab.vetorial(bc);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    paraboloideExt.preencheNormais(normalT1);
                    paraboloideInt.preencheNormais(normalT1);
                }
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = cb.vetorial(bd);
				normalT2.normaliza();

                for(int j = 0; j < 3; j++) {
                    paraboloideExt.preencheNormais(normalT2);
                    paraboloideInt.preencheNormais(normalT2);
                }
			}
		}				
		paraboloideExt.vertices.position(0);
        paraboloideInt.vertices.position(0);
        paraboloideExt.normais.position(0);
        paraboloideInt.normais.position(0);
        paraboloideExt.cores.position(0);
        paraboloideInt.cores.position(0);
        paraboloideWire.vertices.position(0);
	}
	
	public float coordX(float alpha, float theta){
		return (float) (parameters[0]*alpha*Math.cos(theta));
	}
	
	public float coordY(float alpha, float theta){
		return (float) (parameters[1]*alpha*Math.sin(theta));
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

        /** PARABOLOIDE EXTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, paraboloideExt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, paraboloideExt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, paraboloideExt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, paraboloideExt.getNumIndices());

        /** PARABOLOIDE INTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, paraboloideInt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, paraboloideInt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, paraboloideInt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, paraboloideInt.getNumIndices());

        // Ensure we're using the program we need
        GLES20.glUseProgram(myProgram2);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        /** PARABOLOIDE WIREFRAME**/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, paraboloideWire.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, paraboloideWire.getNumIndices());
    }

}