package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ParabHiperbolicoWire {

	public FloatBuffer vertices;
	public FloatBuffer normais;
	public  FloatBuffer cores;
	public FloatBuffer wire;
	public final int slices = 20;
	public final int stacks = 20;
	public final int numCoord = slices*stacks*3*8;
	
	public final float A = 8.0f, B = 8.0f;

	public final float passoU = (float) ((8.0f)/slices);
	public final float passoV = (float) ((8.0f)/stacks);
	
	public float u = -1.0f;
	public float v = -1.0f;
	
	public ParabHiperbolicoWire(int fatorNormal){
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoord*4);
		
		constroiParaboloideHiperbolico(fatorNormal);
		
	}
	
public void constroiParaboloideHiperbolico(int fatorNormal){
		
		for(u = -4.0f; u < 4.0f; u+= passoU){
			for(v = -4.0f; v < 4.0f; v+= passoV){
								
				float x = coordX(u, v), y = coordY(u, v), z = coordZ(u, v);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(u, v+passoV);
				y = coordY(u, v+passoV);
				z = coordZ(u, v+passoV);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(u+passoU, v);
				y = coordY(u+passoU, v);
				z = coordZ(u+passoU, v);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(u+passoU, v+passoV);
				y = coordY(u+passoU, v+passoV);
				z = coordZ(u+passoU, v+passoV);
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

	public float coordX(float u, float v){
		return (float) (A*u);
				
	}
	
	public float coordY(float u, float v){
		return (float) (B*v);
				
	}
	
	public float coordZ(float u, float v){
		return (float) (16.0f + (u*u)-(v*v));
				
	}
	
	
	public static FloatBuffer allocateFloatBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
	}
	
	public FloatBuffer getVertices() {
		return vertices;
	}
	
	public FloatBuffer getWire(){
		return wire;
	}

	public FloatBuffer getNormals() {
		return normais;
	}
	
	public int getNumIndices(){
		return numCoord/3;
	}
}
