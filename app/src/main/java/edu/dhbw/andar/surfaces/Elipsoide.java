package edu.dhbw.andar.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import edu.dhbw.andar.pub.SurfaceBuffer;
import edu.dhbw.andar.pub.Vetor;


public class Elipsoide {
	
	public SurfaceBuffer elipsoideBuffer;
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
		
		elipsoideBuffer = new SurfaceBuffer(numCoord, numCoordWire);

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

				elipsoideBuffer.wireframe.put(a.x);
                elipsoideBuffer.wireframe.put(a.y);
                elipsoideBuffer.wireframe.put(a.z);

                elipsoideBuffer.wireframe.put(b.x);
                elipsoideBuffer.wireframe.put(b.y);
                elipsoideBuffer.wireframe.put(b.z);

                elipsoideBuffer.wireframe.put(b.x);
                elipsoideBuffer.wireframe.put(b.y);
                elipsoideBuffer.wireframe.put(b.z);

                elipsoideBuffer.wireframe.put(d.x);
                elipsoideBuffer.wireframe.put(d.y);
                elipsoideBuffer.wireframe.put(d.z);

                elipsoideBuffer.wireframe.put(d.x);
                elipsoideBuffer.wireframe.put(d.y);
                elipsoideBuffer.wireframe.put(d.z);

                elipsoideBuffer.wireframe.put(c.x);
                elipsoideBuffer.wireframe.put(c.y);
                elipsoideBuffer.wireframe.put(c.z);

                elipsoideBuffer.wireframe.put(c.x);
                elipsoideBuffer.wireframe.put(c.y);
                elipsoideBuffer.wireframe.put(c.z);

                elipsoideBuffer.wireframe.put(a.x);
                elipsoideBuffer.wireframe.put(a.y);
                elipsoideBuffer.wireframe.put(a.z);

				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					//Primeiro triangulo (inferior)
                    elipsoideBuffer.vertices.put(a.x);
                    elipsoideBuffer.vertices.put(a.y);
                    elipsoideBuffer.vertices.put(a.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);

                    elipsoideBuffer.vertices.put(c.x);
                    elipsoideBuffer.vertices.put(c.y);
                    elipsoideBuffer.vertices.put(c.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);

                    elipsoideBuffer.vertices.put(b.x);
                    elipsoideBuffer.vertices.put(b.y);
                    elipsoideBuffer.vertices.put(b.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);

					//Segundo triangulo (superior)
                    elipsoideBuffer.vertices.put(b.x);
                    elipsoideBuffer.vertices.put(b.y);
                    elipsoideBuffer.vertices.put(b.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);

                    elipsoideBuffer.vertices.put(c.x);
                    elipsoideBuffer.vertices.put(c.y);
                    elipsoideBuffer.vertices.put(c.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);

                    elipsoideBuffer.vertices.put(d.x);
                    elipsoideBuffer.vertices.put(d.y);
                    elipsoideBuffer.vertices.put(d.z);

                    elipsoideBuffer.cores.put(1.0f);
                    elipsoideBuffer.cores.put(0.0f);
                    elipsoideBuffer.cores.put(0.0f);
				}
				
				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(b, a);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(c, a);
				
				Vetor normalT1 = new Vetor();
				normalT1 = bc.vetorial(ab);
				normalT1.normaliza();

                elipsoideBuffer.normais.put(normalT1.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.z*fatorNormal);

                elipsoideBuffer.normais.put(normalT1.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.z*fatorNormal);

                elipsoideBuffer.normais.put(normalT1.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT1.z*fatorNormal);
				
				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, d);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = new Vetor();
				normalT2 = bd.vetorial(cb);
				normalT2.normaliza();

                elipsoideBuffer.normais.put(normalT2.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.z*fatorNormal);

                elipsoideBuffer.normais.put(normalT2.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.z*fatorNormal);

                elipsoideBuffer.normais.put(normalT2.x*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.y*fatorNormal);
                elipsoideBuffer.normais.put(normalT2.z*fatorNormal);
	
			}
		}

        elipsoideBuffer.vertices.position(0);
		elipsoideBuffer.normais.position(0);
		elipsoideBuffer.cores.position(0);
		elipsoideBuffer.wireframe.position(0);
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

    public int getNumIndices(){
        if(wireframecolor == 0)
            return numCoord/3;
        return numCoordWire/3;
    }

}
