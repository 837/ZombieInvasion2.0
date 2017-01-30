#version 330

layout(location = 0) in vec3 vertex_position;
layout(location = 1) in vec2 vertex_UV;

uniform mat4 mvp;

// Output data ; will be interpolated for each fragment.
out vec2 UV;


void main(){

      gl_Position = mvp* vec4(vertex_position,1);
      UV = vertex_UV;
}
