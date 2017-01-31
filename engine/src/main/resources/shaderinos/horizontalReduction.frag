#version 330

in vec2 UV; //tex0

uniform sampler2D inputSampler;
uniform vec2 sourceDimensions;

out vec4 minValue;

void main(){

    vec2 color = texture(inputSampler, UV).rg;
    vec2 colorR = texture(inputSampler,UV + vec2(sourceDimensions.x,0)).rg;


    vec2 result = min(color,colorR);
    minValue = vec4(result,0,1);
}