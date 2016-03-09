package edu.dhbw.andar.pub;

public class Vetor {
	
	public float x0, y0, z0, x, y, z, alpha;
	
	public Vetor(float x, float y, float z){
		x0 = 0.0f;
		y0 = 0.0f;
		z0 = 0.0f;
		
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vetor(float x, float y, float z, float alpha){
		x0 = 0.0f;
		y0 = 0.0f;
		z0 = 0.0f;

		this.x = x;
		this.y = y;
		this.z = z;
		this.alpha = alpha;
	}
	
	public Vetor(Vetor v){
		x0 = v.x0;
		y0 = v.y0;
		z0 = v.z0;
		
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
		
	public Vetor(){
		x0 = 0.0f;
		y0 = 0.0f;
		z0 = 0.0f;
		
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	public void setOrigem(float x0, float y0, float z0){
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
	}
	
	public Vetor soma(Vetor v){
		
		Vetor ret = new Vetor(x+v.x, y+v.y, z+v.z);
		
		return ret;
	}
	
	public Vetor subtracao(Vetor v, Vetor w){
		
		Vetor ret = new Vetor(w.x-v.x, w.y-v.y, w.z-v.z);
		
		return ret;
	}
	
	public float escalar(Vetor v){
		float ret = (x*v.x)+(y*v.y)+(z*v.z);
		
		return ret;
	}
	
	public Vetor vetorial(Vetor v){
		Vetor ret = new Vetor();
	
		ret.x= ((this.y*v.z) - (v.y*this.z));	
	    ret.y= ((v.x*this.z) - (this.x*v.z));	
	    ret.z= ((v.y*this.x) - (this.y*v.x));
	    
	    return ret;	
	}
	
	public void normaliza(){

	    double norma = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
	    this.x = (float) (x/norma);
	    this.y = (float) (y/norma);
	    this.z = (float) (z/norma);
	}
	
	public float getX(){
		return this.x0+this.x;
	}
	
	public float getY(){
		return this.y0+this.y;
	}
	
	public float getZ(){
		return this.z0+this.z;
	}

	public float getAlpha(){ return this.alpha; }
	
	public void imprimeVetor(){
		System.out.println("x " + this.x + "y" + this.y + "z" + this.z);
	}

}
