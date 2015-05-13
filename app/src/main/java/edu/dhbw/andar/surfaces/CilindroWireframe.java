package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class CilindroWireframe {
	
	public FloatBuffer vertices;
	public FloatBuffer normais;
	public  FloatBuffer cores;
	public FloatBuffer wire;
	public final int slices = 30;
	public final int stacks = 30;

	public final float passoU = (float) ((50.0f)/stacks);
	public final float passoV = (float) ((2*Math.PI)/slices);
	
	public float u = -2.0f;
	public float v = 0.0f;
		
	public final int numCoord = (slices+1)*(stacks+1)*3*8;
	
	public CilindroWireframe(int fatorNormal) {
			
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoord*4);
		
		constroiCilindro(fatorNormal);
	}
	
	public void constroiCilindro(int fatorNormal){
		
		for(u = -25.0f; u < 25.0f; u+=passoU){
			for(v = 0.0f; v < 2*Math.PI; v+= passoV){
				
				float x = coordX(v, u), y = coordY(v, u), z = coordZ(v, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(v+passoV, u);
				y = coordY(v+passoV, u);
				z = coordZ(v+passoV, u);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(v, u+passoU);
				y = coordY(v, u+passoU);
				z = coordZ(v, u+passoU);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(v+passoV, u+passoU);
				y = coordY(v+passoV, u+passoU);
				z = coordZ(v+passoV, u+passoU);
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
	
	public float coordX(float v, float u){
		return (float) (u*Math.sin(v));
				
	}
	
	public float coordY(float v, float u){
		return (float) (u*Math.cos(v));
				
	}
	
	public float coordZ(float v, float u){
		return (float) 25.0f+u;
				
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
	
	public FloatBuffer getWire(){
		return wire;
	}
	
	public int getNumIndices(){
		return numCoord/3;
	}

}
