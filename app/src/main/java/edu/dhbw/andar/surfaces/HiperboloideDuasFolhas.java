package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;


public class HiperboloideDuasFolhas extends SurfaceObject{

	public final int slices = 30;
	public final int stacks = 30;

	public final float passoU = (float) (2*Math.PI/slices);
	public final float passoT = (float) ((10.0f)/stacks);
	
	public float u = 0.0f;
	public float t = -3.0f;
    public float zMin = 0.0f;

	public HiperboloideDuasFolhas(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 5.0f;
        parameters[1] = 5.0f;
        parameters[2] = 5.0f;

        max_progress = 30;

        capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*2*(slices+1)*(stacks+1)*BYTES_PER_FLOAT;
        wirecapacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*8*(slices+1)*(stacks+1)*BYTES_PER_FLOAT;

        buffer = allocateFloatBuffer(capacity);
        wirebuffer = allocateFloatBuffer(wirecapacity);

		buildSurface();
	}
	
	public void buildSurface(){
        buffer.clear();
        wirebuffer.clear();
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
                preenche(buffer, b, color, normalT2.neg());
                preenche(buffer, c, color, normalT2.neg());
                preenche(buffer, d, color, normalT2.neg());

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
