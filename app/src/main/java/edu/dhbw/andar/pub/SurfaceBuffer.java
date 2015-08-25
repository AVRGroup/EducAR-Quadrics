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
    public FloatBuffer wireframe;

    public SurfaceBuffer(int numCoord, int numCoordWire){
        vertices=allocateFloatBuffer(numCoord*4);
        normais=allocateFloatBuffer(numCoord*4);
        cores=allocateFloatBuffer(numCoord*4);
        wireframe=allocateFloatBuffer(numCoordWire*4);
    }

    public static FloatBuffer allocateFloatBuffer(int capacity){
        ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }

    public FloatBuffer getVertices() {
        return vertices;
    }

    public FloatBuffer getCores() {
        return cores;
    }

    public FloatBuffer getWire(){
        return wireframe;
    }

    public FloatBuffer getNormals() {
        return normais;
    }
}
