package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import edu.dhbw.andar.pub.Vetor;


public class Elipsoide {
	
	public FloatBuffer vertices;
	public FloatBuffer normais;
	public FloatBuffer cores;
	public FloatBuffer wire;
	public final int slices = 40;
	public final int stacks = 40;

	public final float passoU = (float) ((2*Math.PI)/stacks);
	public final float passoV = (float) ((2*Math.PI)/slices);
	
	public float u = 0.0f;
	public float v = 0.0f;
		
	public final int numCoord = (slices+1)*(stacks+1)*3*6;
	public final int numCoordWire = (slices+1)*(stacks+1)*3*8;

	public final int wireframecolor;
	
public Elipsoide(int fatorNormal, int wireframe){
		
		vertices=allocateFloatBuffer(numCoord*4);
		normais=allocateFloatBuffer(numCoord*4);
		cores=allocateFloatBuffer(numCoord*4);
		wire=allocateFloatBuffer(numCoordWire*4);
		wireframecolor = wireframe;
		
		constroiElipsoide(fatorNormal);
		
	}
	
	public void constroiElipsoide(int fatorNormal){
		
		for(u = 0.0f; u < 2*Math.PI; u+=passoU){
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
				
				/*//Normal para dentro, paraboloide interno
				if(fatorNormal == -1){
					//Primeiro triangulo (inferior)
					vertices.put(a.x);
					vertices.put(a.y);
					vertices.put(a.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
				}*/
				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
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
				
	//			Log.e("vetorX", String.valueOf(ab.getX()));
	//			Log.e("vetorY", String.valueOf(ab.getY()));
	//			Log.e("vetorZ", String.valueOf(ab.getZ()));
				
				/*//Normal para dentro, paraboloide interno
				if(fatorNormal == -1){
					//Segundo triangulo (superior)
					vertices.put(d.x);
					vertices.put(d.y);
					vertices.put(d.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
					
					vertices.put(b.x);
					vertices.put(b.y);
					vertices.put(b.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
					
					vertices.put(c.x);
					vertices.put(c.y);
					vertices.put(c.z);
					//cores.put(1.0f);
					cores.put(1.0f);
					cores.put(0.0f);
					cores.put(0.0f);
					
					wire.put(1.0f);
					wire.put(1.0f);
					wire.put(1.0f);
				}*/
				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
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
				
				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(b, a);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(c, a);
				
				Vetor normalT1 = new Vetor();
				normalT1 = bc.vetorial(ab);
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
				cb = cb.subtracao(c, d);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = new Vetor();
				normalT2 = bd.vetorial(cb);
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

	public float coordX(float v, float u){
		return (float) (30*Math.cos(u));
				
	}
	
	public float coordY(float v, float u){
		return (float) (20*Math.sin(u)*Math.cos(v));
				
	}
	
	public float coordZ(float v, float u){
		return (float) (20.0f+(20*Math.sin(u)*Math.sin(v)));				
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
		return wire;
	}

	public FloatBuffer getNormals() {
		return normais;
	}
	
	public int getNumIndices(){
		if(wireframecolor == 0)
			return numCoord/3;
		return numCoordWire/3;
	}

}
