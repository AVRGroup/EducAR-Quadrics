package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class HiperboloideDuasWireframe {
	
	public FloatBuffer vertices;
	public FloatBuffer normais;
	public  FloatBuffer cores;
	public FloatBuffer wire;
	public final int slices = 30;
	public final int stacks = 30;

	public final float passoU = (float) (2*Math.PI/slices);
	public final float passoT = (float) ((10.0f)/stacks);
	
	public float u = 0.0f;
	public float t = -3.0f;
		
	public final int numCoord = (slices+1)*(stacks+1)*3*8;
	
	public final float A = 5.0f, B = 5.0f, C = 5.0f;
	

	public HiperboloideDuasWireframe(int fatorNormal) {
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoord*4);
		
		constroiHiperboloide(fatorNormal);		
		
	}
	
	public void constroiHiperboloide(int fatorNormal){
		
		for(t = -5.0f; t <= 5.0f; t+= passoT){
			for(u = 0.0f; u <= 2*Math.PI; u+=passoU){
				
				float x = coordX(t, u), y = coordY(t, u), z = coordZ(t, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(t+passoT, u);
				y = coordY(t+passoT, u);
				z = coordZ(t+passoT, u);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(t, u+passoU);
				y = coordY(t, u+passoU);
				z = coordZ(t, u+passoU);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(t+passoT, u+passoU);
				y = coordY(t+passoT, u+passoU);
				z = coordZ(t+passoT, u+passoU);
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
				normalT1 = bc.vetorial(ab);
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
	
	public float coordX(float t, float u){
		return (float) (A*Math.cos(u)*Math.sqrt((t*t)-1));
				
	}
	
	public float coordY(float t, float u){
		return (float) (A*Math.sin(u)*Math.sqrt((t*t)-1));
				
	}
	
	public float coordZ(float t, float u){
		return (float) (25.0f+(C*t));				
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
