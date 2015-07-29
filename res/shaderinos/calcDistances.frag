#version 330;
/*
* calculates and outputs the distance from the center of shadowCastersTexture for each
* non transparent pixel
* saves distance in the red channel of output
*
*/

// texture coordinates
in vec2 UV;

uniform sampler2D shadowCastersTexture;
//uniform vec2 texturesize;

layout(location = 0) out vec4 computedDist;



void main(){
    vec4 color = texture2D(shadowCastersTexture,UV).rgba;
    float dist = color.a > 0.3f?length(UV - 0.5f):1.0f;
    //dist *= texturesize.x
    computedDist = vec4(dist,0,0,1);
}