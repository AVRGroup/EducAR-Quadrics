precision mediump float;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
attribute vec4 aPosition;
attribute vec3 a_Normal;

void main(void){

	mat4 mvpmatrix = uPMatrix * uMVMatrix;

	gl_Position = mvpmatrix * aPosition;
}
