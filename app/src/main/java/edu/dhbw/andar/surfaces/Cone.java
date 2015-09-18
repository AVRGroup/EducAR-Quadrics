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

    public final int numCoord = (slices+1)*(stacks+1)*3*6;
    public final int numCoordWire = (slices+1)*(stacks+1)*3*8;

    public Cone(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter, renderer);

        coneInt = new SurfaceBuffer(numCoord, 0, -1);
        coneExt = new SurfaceBuffer(numCoord, 0, 1);
        coneWire = new SurfaceBuffer(numCoordWire, 1, 1);

        constroiCilindro();
    }

    public void constroiCilindro(){

        for(u = -25.0f; u < 25.0f; u+=passoU){
            for(v = 0.0f; v < 2*Math.PI; v+= passoV){

                float x = coordX(v, u), y = coordY(v, u), z = coordZ(u);
                Vetor a = new Vetor(x, y, z);

                x = coordX(v + passoV, u);
                y = coordY(v + passoV, u);
                z = coordZ(u);
                Vetor b = new Vetor(x, y, z);

                x = coordX(v, u + passoU);
                y = coordY(v, u + passoU);
                z = coordZ(u+passoU);
                Vetor c = new Vetor(x, y, z);

                x = coordX(v+passoV, u+passoU);
                y = coordY(v+passoV, u+passoU);
                z = coordZ(u+passoU);
                Vetor d = new Vetor(x, y, z);

                coneWire.preencheVertices(a);
                coneWire.preencheVertices(b);

                coneWire.preencheVertices(b);
                coneWire.preencheVertices(d);

                coneWire.preencheVertices(d);
                coneWire.preencheVertices(c);

                coneWire.preencheVertices(c);
                coneWire.preencheVertices(a);

                //Normal para fora, paraboloide externo
                //Primeiro triangulo (inferior)
                coneExt.preencheVertices(a);
                coneExt.preencheVertices(b);
                coneExt.preencheVertices(c);

                //Segundo triangulo (superior)
                coneExt.preencheVertices(c);
                coneExt.preencheVertices(b);
                coneExt.preencheVertices(d);

                //Normal para dentro, paraboloide interno
                //Primeiro triangulo (inferior)
                coneInt.preencheVertices(a);
                coneInt.preencheVertices(c);
                coneInt.preencheVertices(b);

                //Segundo triangulo (superior)
                coneInt.preencheVertices(d);
                coneInt.preencheVertices(b);
                coneInt.preencheVertices(c);

                for (int i = 0; i < 6; i++){
                    coneExt.preencheCores(cor);
                    coneInt.preencheCores(cor);
                }

                //Normal do primeiro triangulo
                Vetor ab = new Vetor();
                ab = ab.subtracao(a, b);

                Vetor bc = new Vetor();
                bc = bc.subtracao(b, c);

                Vetor normalT1 = ab.vetorial(bc);
                normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    coneExt.preencheNormais(normalT1);
                    coneInt.preencheNormais(normalT1);
                }

                //Normal do segundo triangulo
                Vetor cb = new Vetor();
                cb = cb.subtracao(c, b);

                Vetor bd = new Vetor();
                bd = bd.subtracao(b, d);

                Vetor normalT2 = cb.vetorial(bd);
                normalT2.normaliza();

                for(int j = 0; j < 3; j++) {
                    coneExt.preencheNormais(normalT2);
                    coneInt.preencheNormais(normalT2);
                }
            }
        }

        coneExt.vertices.position(0);
        coneInt.vertices.position(0);
        coneExt.normais.position(0);
        coneInt.normais.position(0);
        coneExt.cores.position(0);
        coneInt.cores.position(0);
        coneWire.vertices.position(0);
    }

    public float coordX(float v, float u){
        return (float) (u*Math.sin(v));
    }

    public float coordY(float v, float u){
        return (float) (u*Math.cos(v));
    }

    public float coordZ(float u){
        return 25.0f+u;
    }

    @Override
    public float getParameter(){
        return this.u;
    }

    @Override
    public void setParameter(float progress){
        this.u = progress;
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

        /** CONE EXTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, coneExt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //Atencao para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, coneExt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, coneExt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha cone externo
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coneExt.getNumIndices());

        /** CONE INTERNO **/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, coneInt.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        //aten??o para o contador das cores, aqui defini cores sem o alpha, diferente do cubo, por isso 3
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
                0, coneInt.getCores());
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, true,
                0, coneInt.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coneInt.getNumIndices());

        // Ensure we're using the program we need
        GLES20.glUseProgram(myProgram2);

        if( glCameraMatrixBuffer != null) {
            // Transform to where the marker is
            GLES20.glUniformMatrix4fv(muMVMatrixHandle, 1, false, glMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muMVMatrixHandle");
            GLES20.glUniformMatrix4fv(muPMatrixHandle, 1, false, glCameraMatrix, 0);
            GraphicsUtil.checkGlError("glUniformMatrix4fv muPMatrixHandle");
        }

        /** CONE WIREFRAME**/
        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, coneWire.getVertices()); // 3 = Size of the position data in elements.

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glLineWidth(2.0f);

        // Desenha elipsoide
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, coneWire.getNumIndices());
    }
}
