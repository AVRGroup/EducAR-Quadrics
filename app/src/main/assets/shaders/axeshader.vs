precision mediump float;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
uniform vec4 a_Color;
attribute vec4 aPosition;

void main(void) {
	mat4 mvpmatrix = uPMatrix * uMVMatrix;
	gl_Position = mvpmatrix * aPosition;
}