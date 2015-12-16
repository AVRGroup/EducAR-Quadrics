package edu.dhbw.andar.pub;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Lidiane on 25/08/2015.
 */
public class SurfaceBuffer {

    public FloatBuffer vertices;
    public FloatBuffer normais;
    public FloatBuffer cores;
    public int numCoord;
    public int numCoordCor;
    public int fatorNormal;
    public int modeloWireframe;

    public SurfaceBuffer(int numCoord, int numCoordCor, int modeloWireframe, int fatorNormal){
        //Necessario ter uma variavel na classe para o metodo getNumIndices()
        this.numCoord = numCoord;
        this.fatorNormal = fatorNormal;
        this.modeloWireframe = modeloWireframe;
        this.numCoordCor = numCoordCor;
        //Verifica se esta sendo construido um modelo solido ou wireframe
        if(modeloWireframe == 0){
            vertices=allocateFloatBuffer(numCoord*4);
            normais=allocateFloatBuffer(numCoord*4);
            cores=allocateFloatBuffer(numCoord*4);
        }else{
            //Modelo wireframe so tem vertices, entao nao precisa dos outros buffers
            vertices=allocateFloatBuffer(numCoord*4);
            normais=null;
            cores=null;
        }

    }

    public static FloatBuffer allocateFloatBuffer(int capacity){
        ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }
    public void preencheVertices(Vetor ponto){
        this.vertices.put(ponto.x);
        this.vertices.put(ponto.y);
        this.vertices.put(ponto.z);
    }

    public void preencheCores(Vetor cor){
        this.cores.put(cor.x);
        this.cores.put(cor.y);
        this.cores.put(cor.z);
        //this.cores.put(1.0f);
    }

    public void preencheNormais(Vetor normal) {
        this.normais.put((normal.x*fatorNormal));
        this.normais.put((normal.y*fatorNormal));
        this.normais.put((normal.z*fatorNormal));
    }

    public FloatBuffer getVertices() {
        return this.vertices;
    }

    public FloatBuffer getCores() {
        return this.cores;
    }

    public FloatBuffer getNormals() {
        return this.normais;
    }

    public int getNumIndices(){
        return numCoord/3;
    }

    public void clearBuffers(){
        vertices = allocateFloatBuffer(numCoord*4);
        if(modeloWireframe == 0) {
            normais = allocateFloatBuffer(numCoord * 4);
            cores = allocateFloatBuffer(numCoord * 4);
        }
    }
}
