#version 330

in vec2 UV;
out vec4 outputColor;

//TODO try to filter everything except red

void main()
{
	outputColor = vec4(1.0, 0,0,1);
}