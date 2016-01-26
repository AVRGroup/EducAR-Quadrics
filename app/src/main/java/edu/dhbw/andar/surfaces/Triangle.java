package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;

/**
 * Created by Lidiane on 25/11/2015.
 */
public class Triangle extends SurfaceObject{

    private FloatBuffer vertexBuffer;
    final int buffers[] = new int[1];
    static final int POSITION_DATA_SIZE = 3;
    static final int COLOR_DATA_SIZE = 4;
    static final int BYTES_PER_FLOAT = 4;
    final int stride = (POSITION_DATA_SIZE + COLOR_DATA_SIZE)*BYTES_PER_FLOAT;//(coords por vertices + coords por cor)*bytes por floats

    static float triangleCoords[] = {   // in counterclockwise order:
            //X, Y, Z
            //R, G, B, A
            0.0f,  1.0f, 0.0f, // top
            1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.0f, 0.0f, // bottom left
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, 0.0f, 0.0f,  // bottom right
            1.0f, 0.0f, 0.0f, 1.0f
    };

    public Triangle(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

        buildSurface();

    }

    public void buildSurface(){
        /** PREENCHIMENTO DO FLOATBUFFER **/
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        /** CRIAÇÃO DOS BUFFERS **/
        GLES20.glGenBuffers(1, buffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT, vertexBuffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public synchronized void draw( GL10 glUnused ) {
        /** DESENHO A PARTIR DO BUFFER **/
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glEnableVertexAttribArray(mModelPositionHandle);
        GLES20.glVertexAttribPointer(mModelPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glEnableVertexAttribArray(mModelColorHandle);
        GLES20.glVertexAttribPointer(mModelColorHandle, COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(0);
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
}
