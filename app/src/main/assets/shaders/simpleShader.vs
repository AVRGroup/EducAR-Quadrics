precision mediump float;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
attribute vec4 aPosition;
attribute vec4 a_Color;
attribute vec3 a_Normal;
varying vec4 v_Color;

void main(void){

	mat4 mvpmatrix = uPMatrix * uMVMatrix;
	
	v_Color = a_Color;
	
	gl_Position = mvpmatrix * aPosition;
}
