package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class ParaboloideWireframe {
	
	public FloatBuffer vertices;
	public FloatBuffer normais;
	public  FloatBuffer wire;
	public final int slices = 32;
	public final int stacks = 32;
	public final int numCoord = slices*stacks*3*8;
	public final float A = 3.0f, B = 3.0f, C = 0.1f;
	public float theta = 0.0f;
	public float alpha = 0.0f;
	public final float passoT = (float) ((2*Math.PI)/slices);
	public final float passoA = (float) ((2*Math.PI)/stacks);
	public final float erro = 0.05f;
	

	public ParaboloideWireframe(int fatorNormal) {
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoord*4);
		
		constroiParaboloide(fatorNormal);		
		
	}
	
	public void constroiParaboloide(int fatorNormal){
		
		for(theta = 0.0f; theta < 2*Math.PI-passoT+erro; theta+= passoT){
			for(alpha = 0.0f; alpha < 2*Math.PI-passoA+erro; alpha+= passoA){
				
				float x = coordX(alpha, theta), y = coordY(alpha, theta), z = (x*x+y*y)*C;
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(alpha, theta+passoT);
				y = coordY(alpha, theta+passoT);
				z = (x*x+y*y)*C;
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(alpha+passoA, theta);
				y = coordY(alpha+passoA, theta);
				z = (x*x+y*y)*C;
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(alpha+passoA, theta+passoT);
				y = coordY(alpha+passoA, theta+passoT);
				z = (x*x+y*y)*C;
				Vetor d = new Vetor(x, y, z);
				
				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					vertices.put(a.x);
					vertices.put(a.y);
					vertices.put(a.z);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);
					
					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);
					
					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);
					
					vertices.put(a.x);
					vertices.put(a.y);
					vertices.put(a.z);
					
					for(int l = 1; l < 9; l++){
						wire.put(1.0f);
						wire.put(1.0f);
						wire.put(1.0f);
					}
				}
				
//				Log.e("vetorX", String.valueOf(ab.getX()));
//				Log.e("vetorY", String.valueOf(ab.getY()));
//				Log.e("vetorZ", String.valueOf(ab.getZ()));
				
				
				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = new Vetor();
				normalT1 = ab.vetorial(bc);
				normalT1.normaliza();
				
				for(int k = 1; k < 9; k++){
					normais.put(normalT1.x*fatorNormal);
					normais.put(normalT1.y*fatorNormal);
					normais.put(normalT1.z*fatorNormal);
				}				
			}
		}				
		vertices.position(0);
		normais.position(0);
		wire.position(0);
		
	}
	
	public float coordX(float alpha, float theta){
		return (float) (A*alpha*Math.cos(theta));
				
	}
	
	public float coordY(float alpha, float theta){
		return (float) (B*alpha*Math.sin(theta));
				
	}
	
	public static FloatBuffer allocateFloatBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
	}

	public FloatBuffer getVertices() {
		return vertices;
	}

	public FloatBuffer getNormals() {
		return normais;
	}
		
	public FloatBuffer getWire() {
		return wire;
	}
	
	public int getNumIndices(){
		return numCoord/3;
	}

}
