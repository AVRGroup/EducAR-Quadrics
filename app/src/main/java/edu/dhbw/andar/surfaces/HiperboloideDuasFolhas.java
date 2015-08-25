package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import edu.dhbw.andar.pub.Vetor;


public class HiperboloideDuasFolhas {

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
		
	public final int numCoord = (slices+1)*(stacks+1)*3*6;
	public final int numCoordWire = (slices+1)*(stacks+1)*3*8;
	
	public final float A = 5.0f, B = 5.0f, C = 5.0f;

	public final int wireframecolor;
	

	public HiperboloideDuasFolhas(int fatorNormal, int wireframe) {
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		cores=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoordWire*4);
		wireframecolor = wireframe;
		
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

				wire.put(a.x);
				wire.put(a.y);
				wire.put(a.z);

				wire.put(b.x);
				wire.put(b.y);
				wire.put(b.z);

				wire.put(b.x);
				wire.put(b.y);
				wire.put(b.z);

				wire.put(d.x);
				wire.put(d.y);
				wire.put(d.z);

				wire.put(d.x);
				wire.put(d.y);
				wire.put(d.z);

				wire.put(c.x);
				wire.put(c.y);
				wire.put(c.z);

				wire.put(c.x);
				wire.put(c.y);
				wire.put(c.z);

				wire.put(a.x);
				wire.put(a.y);
				wire.put(a.z);
				
				//Normal para dentro, paraboloide interno
				if(fatorNormal == -1){
					//Primeiro triangulo (inferior)
					vertices.put(a.x);
					vertices.put(a.y);
					vertices.put(a.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);

					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);

					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
				}
				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					//Primeiro triangulo (inferior)
					vertices.put(a.x);
					vertices.put(a.y);
					vertices.put(a.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
				}
				
//				Log.e("vetorX", String.valueOf(ab.getX()));
//				Log.e("vetorY", String.valueOf(ab.getY()));
//				Log.e("vetorZ", String.valueOf(ab.getZ()));
				
				//Normal para dentro, paraboloide interno
				if(fatorNormal == -1){
					//Segundo triangulo (superior)
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
				}
				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					//Segundo triangulo (superior)
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);

					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);

					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
				}

				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(a, b);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(b, c);
				
				Vetor normalT1 = new Vetor();
				normalT1 = ab.vetorial(bc);
				normalT1.normaliza();
				
				normais.put(normalT1.x*fatorNormal);
				normais.put(normalT1.y*fatorNormal);
				normais.put(normalT1.z*fatorNormal);
				
				normais.put(normalT1.x*fatorNormal);
				normais.put(normalT1.y*fatorNormal);
				normais.put(normalT1.z*fatorNormal);
				
				normais.put(normalT1.x*fatorNormal);
				normais.put(normalT1.y*fatorNormal);
				normais.put(normalT1.z*fatorNormal);
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, b);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = new Vetor();
				normalT2 = cb.vetorial(bd);
				normalT2.normaliza();
				
				normais.put(normalT2.x*fatorNormal);
				normais.put(normalT2.y*fatorNormal);
				normais.put(normalT2.z*fatorNormal);
				
				normais.put(normalT2.x*fatorNormal);
				normais.put(normalT2.y*fatorNormal);
				normais.put(normalT2.z*fatorNormal);
				
				normais.put(normalT2.x*fatorNormal);
				normais.put(normalT2.y*fatorNormal);
				normais.put(normalT2.z*fatorNormal);
	
			}
		}
	
		vertices.position(0);
		normais.position(0);
		cores.position(0);
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
	
	public FloatBuffer getCores() {
			return cores;
	}
	
	public FloatBuffer getWire() {
		return wire;
	}
	
	public int getNumIndices(){
		if(wireframecolor == 0)
			return numCoord/3;
		return numCoordWire/3;
	}
}
