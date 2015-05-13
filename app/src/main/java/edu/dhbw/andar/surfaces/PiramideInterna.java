package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class PiramideInterna {
	
	public static FloatBuffer verticesb;
	public static FloatBuffer normalsb;
	public FloatBuffer cores;
	
	Vetor a = new Vetor(0f, 0f, 0f);
	Vetor b = new Vetor(20.0f, -20.0f, 30.0f);
	Vetor c = new Vetor(-20.0f, -20.0f, 30.0f);
	Vetor d = new Vetor(-20.0f, 20.0f, 30.0f);
	Vetor e = new Vetor(20.0f, 20.0f, 30.0f);
	
	Vetor ab, ac, ad, ae;
	Vetor normal1, normal2, normal3, normal4;
	
	public PiramideInterna(){
		ab = new Vetor();
		ac = new Vetor();
		ad = new Vetor();
		ae = new Vetor();
		normal1 = new Vetor();
		normal2 = new Vetor();
		normal3 = new Vetor();
		normal4 = new Vetor();
		loadVertices();
		loadNormals();
	}
			
//	public static float vertices[] = {
//			
//			0f, 0f, 0f,//a
//			20.0f, -20.0f, 20.0f,//b
//			-20.0f, -20.0f, 20.0f,//c
//					
//			0f, 0f, 0f,//a
//			20.0f, 20.0f, 20.0f,//e
//			20.0f, -20.0f, 20.0f,//b
//			
//			0f, 0f, 0f,//a
//			-20.0f, 20.0f, 20.0f,//d
//			20.0f, 20.0f, 20.0f,//e
//			
//			0f, 0f, 0f,//a
//			-20.0f, 20.0f, 20.0f,//d
//			-20.0f, -20.0f, 20.0f,//c		
//		
//	};
	
	private FloatBuffer loadNormals(){
		normalsb=allocateFloatBuffer(36*4);
		
		ab = ab.subtracao(b, a);
		ac = ac.subtracao(a, c);
		ad = ad.subtracao(a, d);
		ae = ae.subtracao(a, e);
		
		normal1 = ab.vetorial(ac);
		normal2 = ae.vetorial(ab);
		normal3 = ad.vetorial(ae);
		normal4 = ac.vetorial(ad);
		
		//a
//		normalsb.put(0);
//		normalsb.put(0);
//		normalsb.put(-1.0f);
		normalsb.put(normal1.x*-1);
		normalsb.put(normal1.y*-1);
		normalsb.put(normal1.z*-1);
		
		//b
		normalsb.put(normal1.x*-1);
		normalsb.put(normal1.y*-1);
		normalsb.put(normal1.z*-1);
		
		//c
		normalsb.put(normal1.x*-1);
		normalsb.put(normal1.y*-1);
		normalsb.put(normal1.z*-1);
		
		//a
//		normalsb.put(0);
//		normalsb.put(0);
//		normalsb.put(-1.0f);
		normalsb.put(normal2.x*-1);
		normalsb.put(normal2.y*-1);
		normalsb.put(normal2.z*-1);
		
		//e
		normalsb.put(normal2.x*-1);
		normalsb.put(normal2.y*-1);
		normalsb.put(normal2.z*-1);
		
		//b
		normalsb.put(normal2.x*-1);
		normalsb.put(normal2.y*-1);
		normalsb.put(normal2.z*-1);
		
		//a				
//		normalsb.put(0);
//		normalsb.put(0);
//		normalsb.put(-1.0f);
		normalsb.put(normal3.x*-1);
		normalsb.put(normal3.y*-1);
		normalsb.put(normal3.z*-1);		
		
		//d
		normalsb.put(normal3.x*-1);
		normalsb.put(normal3.y*-1);
		normalsb.put(normal3.z*-1);
		
		//e
		normalsb.put(normal3.x*-1);
		normalsb.put(normal3.y*-1);
		normalsb.put(normal3.z*-1);
		
		//a				
//		normalsb.put(0);
//		normalsb.put(0);
//		normalsb.put(-1.0f);
		normalsb.put(normal4.x*-1);
		normalsb.put(normal4.y*-1);
		normalsb.put(normal4.z*-1);
		
		//d
		normalsb.put(normal4.x*-1);
		normalsb.put(normal4.y*-1);
		normalsb.put(normal4.z*-1);
		
		//c
		normalsb.put(normal4.x*-1);
		normalsb.put(normal4.y*-1);
		normalsb.put(normal4.z*-1);
		
		normalsb.position(0);
		return normalsb;
	}

	private FloatBuffer loadVertices(){
		
		verticesb=allocateFloatBuffer(36*4);
		cores=allocateFloatBuffer(36*4);
		
		verticesb.put(a.x);
		verticesb.put(a.y);
		verticesb.put(a.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(b.x);
		verticesb.put(b.y);
		verticesb.put(b.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(c.x);
		verticesb.put(c.y);
		verticesb.put(c.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		
		verticesb.put(a.x);
		verticesb.put(a.y);
		verticesb.put(a.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(e.x);
		verticesb.put(e.y);
		verticesb.put(e.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(b.x);
		verticesb.put(b.y);
		verticesb.put(b.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		
		verticesb.put(a.x);
		verticesb.put(a.y);
		verticesb.put(a.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(d.x);
		verticesb.put(d.y);
		verticesb.put(d.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(e.x);
		verticesb.put(e.y);
		verticesb.put(e.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);		
		
		verticesb.put(a.x);
		verticesb.put(a.y);
		verticesb.put(a.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(c.x);
		verticesb.put(c.y);
		verticesb.put(c.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);
		
		verticesb.put(d.x);
		verticesb.put(d.y);
		verticesb.put(d.z);
		cores.put(1.0f);
		cores.put(0.0f);
		cores.put(0.0f);		
		
		verticesb.position(0);
		cores.position(0);
		return verticesb;
	}
	
	public FloatBuffer getVertices() {
		return verticesb;
	}

	public FloatBuffer getNormals() {
		return normalsb;
	}
	
	public FloatBuffer getCores() {
		return cores;
	}
	
	public int getNumIndices(){
		return 12;
	}
	
	public final void draw(GL10 gl) {
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsb);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesb);
	    
	    gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

	    gl.glPointSize(10.0f);
	    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 12);

	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
	
	public static FloatBuffer allocateFloatBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
	}
	

}
