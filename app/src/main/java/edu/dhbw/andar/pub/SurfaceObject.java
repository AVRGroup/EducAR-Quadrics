package edu.dhbw.andar.pub;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARGLES20Renderer;
import edu.dhbw.andar.util.GraphicsUtil;

/**
 * Created by Lidiane on 26/08/2015.
 */
public abstract class SurfaceObject extends ARObject{
    protected AndARGLES20Renderer mRenderer;
    protected int myProgram;
    protected int myProgram2;
    protected int simpleProgram;
    protected int muMVMatrixHandle;
    protected int muPMatrixHandle;

    /** This will be used to pass in model position information. */
    protected int mPositionHandle;

    /** This will be used to pass in model color information. */
    protected int mColorHandle;

    /** This will be used to pass in model normal information. */
    protected int mNormalHandle;

    protected int mWirePosHandle;
    protected int mWireColorHandle;
    protected int mWireNormHandle;

    protected Vetor cor, color;

    protected float[] parameters = new float[3];

    protected int index;

    protected int max_progress;

    protected static final int POSITION_DATA_SIZE = 3;
    protected static final int COLOR_DATA_SIZE = 4;
    protected static final int NORMAL_DATA_SIZE = 3;
    protected static final int BYTES_PER_FLOAT = 4;

    protected final int stride = (POSITION_DATA_SIZE + COLOR_DATA_SIZE + NORMAL_DATA_SIZE)*BYTES_PER_FLOAT;//(coords por vertices + coords por cor)*bytes por floats

    protected FloatBuffer buffer, wirebuffer;
    protected final int buffers[] = new int[1];
    protected final int wirebuffers[] = new int[1];
    protected int capacity, wirecapacity;

    public SurfaceObject(String name, String patternName, double markerWidth, double[] markerCenter, AndARGLES20Renderer renderer) {
        super(name, patternName, markerWidth, markerCenter);
        mRenderer = renderer;
        myProgram = 0;
        myProgram2 = 0;
        simpleProgram = 0;
        muMVMatrixHandle = 0;
        muPMatrixHandle = 0;

        max_progress = 0;
        index = 0;

        cor = new Vetor(1.0f, 1.0f, 1.0f, 1.0f);
        color = new Vetor(1.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Compile and load a vertex and fragment program for this object
     * @param vspath Path relative to the "assets" directory which denotes location of the vertex shader
     * @param fspath Path relative to the "assets" directory which denotes location of the fragment shader
     */
    public void setProgram( String vspath, String fspath, int mode)
    {
        if (mode == 1){
            // Load and compile the program, grab the attribute for transformation matrix
            myProgram = GraphicsUtil.loadProgram(mRenderer.activity, vspath, fspath);
            muMVMatrixHandle = GLES20.glGetUniformLocation(myProgram, "uMVMatrix");
            GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uMVMatrix");
            if (muMVMatrixHandle == -1) {
                throw new RuntimeException("Requested shader does not have a uniform named uMVMatrix");
            }
            muPMatrixHandle = GLES20.glGetUniformLocation(myProgram, "uPMatrix");
            GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uPMatrix");
            if (muPMatrixHandle == -1) {
                throw new RuntimeException("Requested shader does not have a uniform named uPMatrix");
            }
        }
        else{
            // Load and compile the program, grab the attribute for transformation matrix
            myProgram2 = GraphicsUtil.loadProgram(mRenderer.activity, vspath, fspath);
            muMVMatrixHandle = GLES20.glGetUniformLocation(myProgram2, "uMVMatrix");
            GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uMVMatrix");
            if (muMVMatrixHandle == -1) {
                throw new RuntimeException("Requested shader does not have a uniform named uMVMatrix");
            }
            muPMatrixHandle = GLES20.glGetUniformLocation(myProgram2, "uPMatrix");
            GraphicsUtil.checkGlError("ARGLES20Object glGetUniformLocation uPMatrix");
            if (muPMatrixHandle == -1) {
                throw new RuntimeException("Requested shader does not have a uniform named uPMatrix");
            }
        }

    }

    /**
     * Set the shader program files for this object
     */
    public String vertexProgramPath(int mode) {
        if (mode == 1)
            return "shaders/meuVertexShader.vs";
        else
            return "shaders/simpleShader.vs";
    }

    public String fragmentProgramPath(int mode) {
        if (mode == 1)
            return "shaders/meuFragmentShader.fs";
        else
            return "shaders/simpleShader.fs";
    }

    /**
     * Initialize the shader and transform matrix attributes
     * @param glUnused an unused 1.0 gl context
     */
    @Override
    public void init( GL10 glUnused ) {
        setProgram( vertexProgramPath(1), fragmentProgramPath(1), 1);
        setProgram(vertexProgramPath(0), fragmentProgramPath(0), 0);

        mPositionHandle = GLES20.glGetAttribLocation(myProgram, "aPosition");
        GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
        if (mPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        mColorHandle = GLES20.glGetAttribLocation(myProgram, "a_Color");
        GraphicsUtil.checkGlError("glGetAttribLocation aColor");
        if (mColorHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aColor");
        }

        mNormalHandle = GLES20.glGetAttribLocation(myProgram, "a_Normal");
        GraphicsUtil.checkGlError("glGetAttribLocation aNormal");
        if (mNormalHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aNormal");
        }

        mWirePosHandle = GLES20.glGetAttribLocation(myProgram2, "aPosition");
        GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
        if (mWirePosHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        /** CRIAÇÃO DOS BUFFERS **/
        GLES20.glGenBuffers(1, buffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.capacity() * BYTES_PER_FLOAT, buffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glGenBuffers(1, wirebuffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, wirebuffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, wirebuffer.capacity() * BYTES_PER_FLOAT, wirebuffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Allow the program to draw without dealing with transformations
     * @param glUnused an unused 1.0 gl context
     */
    @Override
    public abstract void draw( GL10 glUnused );

    protected void preenche(FloatBuffer buffer , Vetor pos, Vetor cor, Vetor normal){
        buffer.put(pos.getX());
        buffer.put(pos.getY());
        buffer.put(pos.getZ());
        buffer.put(cor.getX());
        buffer.put(cor.getY());
        buffer.put(cor.getZ());
        buffer.put(cor.getAlpha());
        buffer.put(normal.getX());
        buffer.put(normal.getY());
        buffer.put(normal.getZ());
    }

    protected static FloatBuffer allocateFloatBuffer(int capacity){
        ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }
}
