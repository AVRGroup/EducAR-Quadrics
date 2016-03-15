package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;

public class Cone extends SurfaceObject {

    public SurfaceBuffer coneInt;
    public SurfaceBuffer coneExt;
    public SurfaceBuffer coneWire;

    public final int slices = 30;
    public final int stacks = 30;

    public final float passoU = (float) ((50.0f)/stacks);
    public final float passoV = (float) ((2*Math.PI)/slices);

    public float u = -2.0f;
    public float v = 0.0f;
    public float zMin = 0.0f;

    public Cone(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

        parameters[0] = 1.0f;
        parameters[1] = 1.0f;
        parameters[2] = 1.0f;

        max_progress = 10;

        capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*2*(slices+1)*(stacks+1)*BYTES_PER_FLOAT;

        buffer = allocateFloatBuffer(capacity);

        buildSurface();
    }

    public void buildSurface(){
        for(u = -25.0f; u < 25.0f; u+=passoU){
            for(v = 0.0f; v < 2*Math.PI; v+= passoV){

                if(u == -25.0f && v == 0.0f){
                    zMin = coordZ(v, u);
                }

                float x = coordX(v, u), y = coordY(v, u), z = coordZ(v, u);
                Vetor a = new Vetor(x, y, z);

                x = coordX(v+passoV, u);
                y = coordY(v+passoV, u);
                z = coordZ(v+passoV, u);
                Vetor b = new Vetor(x, y, z);

                x = coordX(v, u+passoU);
                y = coordY(v, u+passoU);
                z = coordZ(v, u+passoU);
                Vetor c = new Vetor(x, y, z);

                x = coordX(v+passoV, u+passoU);
                y = coordY(v+passoV, u+passoU);
                z = coordZ(v+passoV, u+passoU);
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
            }
        }
        zMin = 0.0f;
        buffer.position(0);
    }

    public float coordX(float v, float u){
        return (float) (parameters[0]*(u*Math.sin(v)));

    }

    public float coordY(float v, float u){
        return (float) (parameters[1]*(u*Math.cos(v)));

    }

    public float coordZ(float v, float u){
        return (zMin - (parameters[0]*u));

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

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, buffer.capacity() * BYTES_PER_FLOAT, buffer);
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
