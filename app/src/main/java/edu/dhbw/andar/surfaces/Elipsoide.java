package edu.dhbw.andar.surfaces;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.pub.SurfaceObject;
import edu.dhbw.andar.pub.Vetor;
import edu.dhbw.andar.util.GraphicsUtil;

public class Elipsoide extends SurfaceObject{

    //Tem que ser multiplo de 3 para fechar a superficie
    public final int slices = 24;
    public final int stacks = 24;

    public final float passoU = (float) ((2*Math.PI)/slices);
    public final float passoV = (float) ((Math.PI)/stacks);

    public final float Xo = 0.0f;
    public final float Yo = 0.0f;
    public final float Zo = 0.0f;

    public Elipsoide(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);
        parameters[0] = 25.0f;
        parameters[1] = 25.0f;
        parameters[2] = 25.0f;

        max_progress = 45;

        capacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*6*(slices+1)*(stacks+1)*BYTES_PER_FLOAT;
        wirecapacity = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*8*(slices+1)*(stacks+1)*BYTES_PER_FLOAT;

        buffer = allocateFloatBuffer(capacity);
        wirebuffer = allocateFloatBuffer(wirecapacity);

        buildSurface();
    }

    public void buildSurface(){
        buffer.clear();
        wirebuffer.clear();
        axes.clear();
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
                preenche(buffer, c, color, normalT1);
                preenche(buffer, b, color, normalT1);
                preenche(buffer, b, color, normalT2);
                preenche(buffer, c, color, normalT2);
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

        //eixo x
        preenche(axes, new Vetor(-(parameters[0]+20), 0.0f, parameters[0]), new Vetor(1.0f, 1.0f, 0.0f, 1.0f), new Vetor(-1.0f, 1.0f, 1.0f));
        preenche(axes, new Vetor((parameters[0]+20), 0.0f, parameters[0]), new Vetor(1.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));

        //eixo y
        preenche(axes, new Vetor(0.0f, -(parameters[0]+20), parameters[0]), new Vetor(0.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, -1.0f, 1.0f));
        preenche(axes, new Vetor(0.0f, parameters[0]+20, parameters[0]), new Vetor(0.0f, 1.0f, 0.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));

        //eixo z
        preenche(axes, new Vetor(0.0f, 0.0f, -(parameters[0]+20)+parameters[0]), new Vetor(0.0f, 0.0f, 1.0f, 1.0f), new Vetor(1.0f, 1.0f, -1.0f));
        preenche(axes, new Vetor(0.0f, 0.0f, parameters[0]+20+parameters[0]), new Vetor(0.0f, 0.0f, 1.0f, 1.0f), new Vetor(1.0f, 1.0f, 1.0f));
        axes.position(0);
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
        super.draw(glUnused);

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