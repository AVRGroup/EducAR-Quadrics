package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Paraboloide {	
	
	public FloatBuffer vertices;
	public FloatBuffer normais;
	public  FloatBuffer cores;
	public  FloatBuffer wire;
	public final int slices = 32;
	public final int stacks = 32;
	public final int numCoord = slices*stacks*18;
	public final int numCoordWire = slices*stacks*3*8;
	public final float A = 3.0f, B = 3.0f, C = 0.1f;
	public float theta = 0.0f;
	public float alpha = 0.0f;
	public final float passoT = (float) ((2*Math.PI)/slices);
	public final float passoA = (float) ((2*Math.PI)/stacks);
	public final float erro = 0.05f;

	public final int wireframecolor;
	

	public Paraboloide(int fatorNormal, int wireframe) {
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		cores=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoordWire * 4);
		wireframecolor = wireframe;

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
					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);

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
	
	public FloatBuffer getCores() {
		if(wireframecolor == 0)
			return cores;
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