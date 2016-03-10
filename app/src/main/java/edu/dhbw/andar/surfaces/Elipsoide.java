package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;
import java.nio.FloatBuffer;


public class Elipsoide extends SurfaceObject{

    public FloatBuffer buffer;

    //Tem que ser multiplo de 3 para fechar a superficie
    public final int slices = 24;
    public final int stacks = 24;

    public final float passoU = (float) ((2*Math.PI)/slices);
    public final float passoV = (float) ((Math.PI)/stacks);

    public final float Xo = 0.0f;
    public final float Yo = 0.0f;
    public final float Zo = 0.0f;

    final int buffers[] = new int[1];

    public int capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*(slices)*(stacks)*BYTES_PER_FLOAT;

    public Elipsoide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 25.0f;
        parameters[1] = 25.0f;
        parameters[2] = 25.0f;

        max_progress = 45;

        buffer = allocateFloatBuffer(capacity);

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

                //Normal do primeiro triangulo
                Vetor ab = new Vetor();
                ab = ab.subtracao(b, a);

                Vetor bc = new Vetor();
                bc = bc.subtracao(c, a);

                Vetor normalT1 = bc.vetorial(ab);
                normalT1.normaliza();

                //Normal do segundo triangulo
                Vetor cb = new Vetor();
                cb = cb.subtracao(c, d);

                Vetor bd = new Vetor();
                bd = bd.subtracao(b, d);

                Vetor normalT2 = bd.vetorial(cb);
                normalT2.normaliza();

                preenche(buffer, a, color, normalT1);
                preenche(buffer, b, color, normalT1);
                preenche(buffer, c, color, normalT1);
                preenche(buffer, c, color, normalT2);
                preenche(buffer, b, color, normalT2);
                preenche(buffer, d, color, normalT2);
            }
        }
        buffer.position(0);
    }

    public float coordX(float v, float u){
        return (float) (Xo + parameters[0]*Math.cos(u)*Math.sin(v));
    }

    public float coordY(float v, float u){
        return (float) (Yo + parameters[1]*Math.sin(u)*Math.sin(v));
    }

    public float coordZ(float v, float u){
        return (float) (parameters[2] + (Zo + parameters[2]*Math.cos(v)));
    }

    @Override
    public int getParameter(){
        return (int) this.parameters[index];
    }

    @Override
    public void setParameter(float progress){
        this.parameters[index] = progress;
    }

    @Override
    public int getMaxProgress(){
        return this.max_progress;
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

        /** CRIAÇÃO DOS BUFFERS **/
        GLES20.glGenBuffers(1, buffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.capacity() * BYTES_PER_FLOAT, buffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        buffer.clear();
        GLES20.glDisableVertexAttribArray(0);

        /** DESENHO A PARTIR DO BUFFER **/
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, (POSITION_DATA_SIZE + COLOR_DATA_SIZE) * BYTES_PER_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, capacity/stride);
    }
}