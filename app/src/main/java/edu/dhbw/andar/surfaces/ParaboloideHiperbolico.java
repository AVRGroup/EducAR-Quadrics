package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class ParaboloideHiperbolico extends SurfaceObject{

	public final int slices = 20;
	public final int stacks = 20;

	public final float passoU = (float) ((8.0f)/slices);
	public final float passoV = (float) ((8.0f)/stacks);
	
	public float u = -1.0f;
	public float v = -1.0f;

	public ParaboloideHiperbolico(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 8.0f;
        parameters[1] = 8.0f;
        parameters[2] = 0.0f;

        max_progress = 40;

        capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*2*(slices)*(stacks)*BYTES_PER_FLOAT;
        wirecapacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*8*(slices)*(stacks)*BYTES_PER_FLOAT;

        buffer = allocateFloatBuffer(capacity);
        wirebuffer = allocateFloatBuffer(wirecapacity);

		buildSurface();
	}
	
	public void buildSurface(){
        buffer.clear();
        wirebuffer.clear();
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

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = ab.vetorial(bc);
				normalT1.normaliza();

				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = cb.vetorial(bd);
				normalT2.normaliza();

                //Normal para dentro, paraboloide interno
                preenche(buffer, a, color, normalT1.neg());
                preenche(buffer, c, color, normalT1.neg());
                preenche(buffer, b, color, normalT1.neg());
                preenche(buffer, d, color, normalT2.neg());
                preenche(buffer, b, color, normalT2.neg());
                preenche(buffer, c, color, normalT2.neg());

                //Normal para fora, paraboloide externo
                preenche(buffer, a, color, normalT1);
                preenche(buffer, b, color, normalT1);
                preenche(buffer, c, color, normalT1);
                preenche(buffer, c, color, normalT2);
                preenche(buffer, b, color, normalT2);
                preenche(buffer, d, color, normalT2);

                preenche(wirebuffer, a, cor, normalT1);
                preenche(wirebuffer, b, cor, normalT1);
                preenche(wirebuffer, b, cor, normalT1);
                preenche(wirebuffer, d, cor, normalT1);
                preenche(wirebuffer, d, cor, normalT1);
                preenche(wirebuffer, c, cor, normalT1);
                preenche(wirebuffer, c, cor, normalT1);
                preenche(wirebuffer, a, cor, normalT1);
			}
		}
        buffer.position(0);
        wirebuffer.position(0);
	}
	
	public float coordX(float u, float v){
		return (float) (parameters[0]*u);
	}
	
	public float coordY(float u, float v){
		return (float) (parameters[1]*v);
	}
	
	public float coordZ(float u, float v){
		return (float) (parameters[0] + parameters[1] + (u*u)-(v*v));
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

        GLES20.glUseProgram(myProgram);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        // Pass in the position information
        buffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer); // 3 = Size of the position data in elements.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //Atencao para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        buffer.position(POSITION_DATA_SIZE);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        buffer.position(POSITION_DATA_SIZE + COLOR_DATA_SIZE);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha cone externo
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, capacity / stride);

        GLES20.glUseProgram(myProgram2);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        wirebuffer.position(0);
        GLES20.glVertexAttribPointer(mWirePosHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, wirebuffer); // 3 = Size of the position data in elements.
        GLES20.glEnableVertexAttribArray(mWirePosHandle);

        GLES20.glLineWidth(2.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, wirecapacity / stride);
    }

}
