precision mediump float;
uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
attribute vec4 aPosition;
attribute vec4 a_Color;
attribute vec3 a_Normal;
varying vec4 v_Color;
vec3 specularLightIntensity = vec3(0.8, 0.8, 0.8);
float shininess = 20.0;
varying vec3 N;
varying vec3 v;
void main(void){
	vec3 u_LightPos = vec3(0.0 , 0.0 , 0.0);
	vec3 modelViewLight = vec3(uMVMatrix * vec4(u_LightPos, 0.0));
	vec3 modelViewVertex = vec3(uMVMatrix * aPosition);
	vec3 modelViewNormal = vec3(uMVMatrix * vec4(a_Normal, 0.0));
	float distance = length(modelViewLight - modelViewVertex);
	// Get a lighting direction vector from the light to the vertex.
	vec3 lightVector = normalize(modelViewLight - modelViewVertex);
   // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
   // pointing in the same direction then it will get max illumination.
	float diffuse = max(dot(modelViewNormal, lightVector), 0.1);
   // Attenuate the light based on distance.
   //diffuse = diffuse * (10.0 / (1.0 + (0.25 * distance)));
    v = vec3(uMVMatrix * aPosition);
    N = modelViewNormal;
	vec3 L = normalize(modelViewLight.xyz - v);
    vec3 E = normalize(-v); // we are in Eye Coordinates, so EyePos is (0.0, 0.0, 0.0)
	vec3 R = normalize(-reflect(L,N));
    // calculate Specular Term:
    vec3 Ispec = specularLightIntensity * pow(max(dot(R,E),0.0),0.3* shininess);
    Ispec = clamp(Ispec, 0.0, 1.0);
   // Multiply the color by the illumination level. It will be interpolated across the triangle.
	v_Color = (a_Color * diffuse) /*+ Ispec*/;
	v_Color[0] = v_Color[0]+Ispec[0];
	v_Color[1] = v_Color[1]+Ispec[1];
	v_Color[2] = v_Color[2]+Ispec[2];
	mat4 mvpmatrix = uPMatrix * uMVMatrix;
   // gl_Position is a special variable used to store the final position.
   // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = mvpmatrix * aPosition;
}