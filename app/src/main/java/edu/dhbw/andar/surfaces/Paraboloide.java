package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;

public class Paraboloide extends SurfaceObject{

	public final int slices = 32;
	public final int stacks = 32;
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


        capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*2*(slices)*(stacks)*BYTES_PER_FLOAT;
        wirecapacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*8*(slices)*(stacks)*BYTES_PER_FLOAT;

        buffer = allocateFloatBuffer(capacity);
        wirebuffer = allocateFloatBuffer(wirecapacity);

		buildSurface();
	}
	
	public void buildSurface(){
        buffer.clear();
        wirebuffer.clear();
        axes.clear();
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

                //Normal para fora, paraboloide externo
                preenche(buffer, a, color, normalT1);
                preenche(buffer, b, color, normalT1);
                preenche(buffer, c, color, normalT1);
                preenche(buffer, c, color, normalT2);
                preenche(buffer, b, color, normalT2);
                preenche(buffer, d, color, normalT2);

                //Normal para dentro, paraboloide interno
                preenche(buffer, a, color, normalT1.neg());
                preenche(buffer, c, color, normalT1.neg());
                preenche(buffer, b, color, normalT1.neg());
                preenche(buffer, d, color, normalT2.neg());
                preenche(buffer, b, color, normalT2.neg());
                preenche(buffer, c, color, normalT2.neg());

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
        //eixo x
        preenche(axes, new Vetor(-(parameters[0]+20), 0.0f, 0.0f), new Vetor(1.0f, 1.0f, 0.0f, 1.0f), new Vetor(-1.0f, 1.0f, 1.0f));
        preenche(axes, new Vetor((parameters[0]+20), 0.0f, 0.0f), new Vetor(1.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));

        //eixo y
        preenche(axes, new Vetor(0.0f, -(parameters[0]+20), 0.0f), new Vetor(0.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, -1.0f, 1.0f));
        preenche(axes, new Vetor(0.0f, parameters[0]+20, 0.0f), new Vetor(0.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));

        //eixo z
        preenche(axes, new Vetor(0.0f, 0.0f, -5.0f), new Vetor(0.0f, 0.0f, 1.0f, 1.0f), new Vetor(1.0f, 1.0f, -1.0f));
        preenche(axes, new Vetor(0.0f, 0.0f, 50.0f), new Vetor(0.0f, 0.0f, 1.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));
        axes.position(0);

        buffer.position(0);
        wirebuffer.position(0);
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

        // Pass in the position information
        axes.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, axes); // 3 = Size of the position data in elements.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //Atencao para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        axes.position(POSITION_DATA_SIZE);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, stride, axes);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        axes.position(POSITION_DATA_SIZE + COLOR_DATA_SIZE);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, axes);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha cone externo
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, (POSITION_DATA_SIZE + COLOR_DATA_SIZE+NORMAL_DATA_SIZE)*6*BYTES_PER_FLOAT / stride);

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