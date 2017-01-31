#version 330
/*
* calculates and outputs the distance from the center of shadowCastersTexture for each
* non transparent pixel
* saves distance in the red channel of output
*
*/
#define minOpacity 0.7
#define minAlpha 1f - minOpacity

in vec2 UV;

uniform sampler2D shadowCastersTexture;
uniform vec2 textureDimension;

out vec4 distanceTexel;

void main(){

    distanceTexel = texture2D(shadowCastersTexture,UV);

    //distance from center of the screen (light's position)
    vec2 centerToPixel = UV - vec2(0.5);
    // only pixels with opacity of more than 0.7 cast shadows
    float dist = distanceTexel.a > minAlpha?length(centerToPixel):1.0f;
    dist *= textureDimension.x;

    distanceTexel = vec4(dist,0,0,1);
}