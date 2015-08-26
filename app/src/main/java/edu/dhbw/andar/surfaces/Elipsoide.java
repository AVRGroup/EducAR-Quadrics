package edu.dhbw.andar.surfaces;

import android.util.Log;

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

    public Vetor cor;
	
public Elipsoide(int fatorNormal, int wireframe){
		
		elipsoideBuffer = new SurfaceBuffer(numCoord, 0);
		cor = new Vetor(1.0f, 0.0f, 0.0f);

		wireframecolor = wireframe;
		constroiElipsoide(fatorNormal);
		
	}
	
	public void constroiElipsoide(int fatorNormal){
		
		for(u = 0.0f; u < 2*Math.PI; u+=passoU){
			for(v = 0.0f; v < 2*Math.PI; v+= passoV){
				
				float x = coordX(v, u), y = coordY(v, u), z = coordZ(v, u);
				Vetor a = new Vetor(x, y, z);
				
				x = coordX(v + passoV, u);
				y = coordY(v + passoV, u);
				z = coordZ(v + passoV, u);
				Vetor b = new Vetor(x, y, z);
				
				x = coordX(v, u + passoU);
				y = coordY(v, u + passoU);
				z = coordZ(v, u+passoU);
				Vetor c = new Vetor(x, y, z);
				
				x = coordX(v+passoV, u+passoU);
				y = coordY(v+passoV, u+passoU);
				z = coordZ(v+passoV, u+passoU);
				Vetor d = new Vetor(x, y, z);

                /*elipsoideBuffer.preencheWireframe(a);
                elipsoideBuffer.preencheWireframe(b);

                elipsoideBuffer.preencheWireframe(b);
                elipsoideBuffer.preencheWireframe(d);

                elipsoideBuffer.preencheWireframe(d);
                elipsoideBuffer.preencheWireframe(c);

                elipsoideBuffer.preencheWireframe(c);
                elipsoideBuffer.preencheWireframe(a);*/

				//Normal para fora, paraboloide externo
				if(fatorNormal == 1){
					//Primeiro triangulo (inferior)
					elipsoideBuffer.preencheVertices(a);
                    elipsoideBuffer.preencheVertices(c);
                    elipsoideBuffer.preencheVertices(b);

					//Segundo triangulo (superior)
                    elipsoideBuffer.preencheVertices(b);
                    elipsoideBuffer.preencheVertices(c);
                    elipsoideBuffer.preencheVertices(d);

                    for (int i = 0; i < 6; i++){
                        elipsoideBuffer.preencheCores(cor);
                    }

				}
				
				//Normal do primeiro triangulo
				Vetor ab = new Vetor();			
				ab = ab.subtracao(b, a);
				
				Vetor bc = new Vetor();
				bc = bc.subtracao(c, a);
				
				Vetor normalT1 = new Vetor();
				normalT1 = bc.vetorial(ab);
				normalT1.normaliza();

                for(int j = 0; j < 3; j++) {
                    elipsoideBuffer.preencheNormais(normalT1, fatorNormal);
                }

				//Normal do segundo triangulo
				Vetor cb = new Vetor();			
				cb = cb.subtracao(c, d);
				
				Vetor bd = new Vetor();
				bd = bd.subtracao(b, d);
				
				Vetor normalT2 = new Vetor();
				normalT2 = bd.vetorial(cb);
				normalT2.normaliza();

                for(int j = 0; j < 3; j++){
					elipsoideBuffer.preencheNormais(normalT2, fatorNormal);
                }

            }
		}

        elipsoideBuffer.vertices.position(0);
		elipsoideBuffer.normais.position(0);
		elipsoideBuffer.cores.position(0);
		/*elipsoideBuffer.wireframe.position(0);*/
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
/*        if(wireframecolor == 0)*/
            return numCoord/3;
/*        return numCoordWire/3;*/
    }
}
