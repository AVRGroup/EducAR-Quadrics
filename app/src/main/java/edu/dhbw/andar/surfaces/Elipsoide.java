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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class Elipsoide extends SurfaceObject{

    public SurfaceBuffer elipsoide;
    public SurfaceBuffer elipsoideWireframe;
    public FloatBuffer teste;

    //Tem que ser multiplo de 3 para fechar a superficie
    public final int slices = 24;
    public final int stacks = 24;

    public final float passoU = (float) ((2*Math.PI)/slices);
    public final float passoV = (float) ((Math.PI)/stacks);

    public final int numCoord = (slices)*(stacks)*3*6;
    public final int numCoordWire = (slices)*(stacks)*3*8;

    public final float Xo = 0.0f;
    public final float Yo = 0.0f;
    public final float Zo = 0.0f;

    public float dir = 1;

    public int position_size = 3;
    public int color_size = 4;
    public int normal_size = 3;

    public int capacity = (position_size + color_size + normal_size)*4*(slices)*(stacks)*BYTES_PER_FLOAT;

    final int buffers[] = new int[1];
    static final int POSITION_DATA_SIZE = 3;
    static final int COLOR_DATA_SIZE = 4;
    static final int NORMAL_DATA_SIZE = 3;
    static final int BYTES_PER_FLOAT = 4;
    final int stride = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*BYTES_PER_FLOAT;//(coords por vertices + coords por cor)*bytes por floats

    public Elipsoide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 25.0f;
        parameters[1] = 25.0f;
        parameters[2] = 25.0f;

        max_progress = 45;

        if(elipsoide != null){
            elipsoide = null;
            elipsoideWireframe = null;
        }

        elipsoide = new SurfaceBuffer(numCoord, 0, 1);
        elipsoideWireframe = new SurfaceBuffer(numCoordWire, 1, 1);

        teste = allocateFloatBuffer(capacity);

        buildSurface();
    }

    public void buildSurface(){
        elipsoide.clearBuffers();
        elipsoideWireframe.clearBuffers();

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
                preenche(a, color, normalT1);
                preenche(b, color, normalT1);
                preenche(c, color, normalT2);
                preenche(d, color, normalT2);

            }
        }
        teste.position(0);
        elipsoide.vertices.position(0);
        elipsoide.normais.position(0);
        elipsoide.cores.position(0);
        elipsoideWireframe.vertices.position(0);
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
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, teste.capacity() * BYTES_PER_FLOAT, teste, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        teste.clear();
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);





       /* // Let the object draw
        *//** ELIPSOIDE **//*
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

        *//** ELIPSOIDE WIREFRAME **//*
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, elipsoideWireframe.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, elipsoideWireframe.getNumIndices());*/
    }

    public static FloatBuffer allocateFloatBuffer(int capacity){
        ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }

    public void preenche(Vetor pos, Vetor cor, Vetor norm){
        teste.put(pos.getX());
        teste.put(pos.getY());
        teste.put(pos.getZ());
        teste.put(cor.getX());
        teste.put(cor.getY());
        teste.put(cor.getZ());
        teste.put(cor.getAlpha());
        teste.put(norm.getX());
        teste.put(norm.getY());
        teste.put(norm.getZ());
    }
}