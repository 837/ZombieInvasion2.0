#version 330

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec4 position;

// Output data ; will be interpolated for each fragment.
out vec2 UV;

// global constants for whole mesh
//uniform vec3 ex;

void main(){
	gl_Position =  position;
	UV = (position.xy+vec2(1,1))/2.0;
}
