package edu.dhbw.andar.pub;

import android.opengl.GLES20;

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
    protected float[] mMVMatrix = new float[16]; // ModelView Matrix
    protected float[] mPMatrix = new float[16]; // Projection Matrix

    protected final int buffers[] = new int[1];

    /** This will be used to pass in model position information. */
    protected int mPositionHandle;

    /** This will be used to pass in model color information. */
    protected int mColorHandle;

    /** This will be used to pass in model normal information. */
    protected int mNormalHandle;

    protected Vetor cor;

    protected float[] parameters = new float[3];

    protected int index;

    protected int max_progress;

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

        cor = new Vetor(1.0f, 0.0f, 0.0f);

        GLES20.glGenBuffers(1, buffers, 0);
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
        setProgram( vertexProgramPath(0), fragmentProgramPath(0), 0);

        mPositionHandle = GLES20.glGetAttribLocation(myProgram, "aPosition");
        GraphicsUtil.checkGlError("glGetAttribLocation aPosition");
        if (mPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        mColorHandle = GLES20.glGetAttribLocation(myProgram, "a_Color");

        mNormalHandle = GLES20.glGetAttribLocation(myProgram, "a_Normal");
    }

    /**
     * Allow the program to draw without dealing with transformations
     * @param glUnused an unused 1.0 gl context
     */
    @Override
    public abstract void draw( GL10 glUnused );
}
