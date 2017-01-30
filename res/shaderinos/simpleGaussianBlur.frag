#version 330
#define resolution 512f
#define minBlur = 0f
#define maxBlur = 5f

precision highp float;

in vec2 UV;

//texture blue component contains distance from center :)
uniform sampler2D sampler;
uniform vec2 dir;

out vec4 color;


void main() {

    //radius of the blur depends on the distance from the center
    float radius = texture2D(sampler,UV).b;
    float inShadow = 1 - texture2D(sampler,UV).r;

    //linearly interpolate the amount of blur
    float blur = mix(0f,5f,radius)/resolution;

    //the direction of the blur
    //(1.0, 0.0) -> x-axis blur
    //(0.0, 1.0) -> y-axis blur
    float hstep = dir.x;
    float vstep = dir.y;

    //apply blurring, using a 13-tap filter with predefined gaussian weights
    vec4 sum = vec4(0.0);
    sum += texture2D(sampler, vec2(UV.s - 6.0*blur*hstep, UV.t - 6.0*blur*vstep)) * 0.002216;
    sum += texture2D(sampler, vec2(UV.s - 5.0*blur*hstep, UV.t - 5.0*blur*vstep)) * 0.008764;
    sum += texture2D(sampler, vec2(UV.s - 4.0*blur*hstep, UV.t - 4.0*blur*vstep)) * 0.026995;
    sum += texture2D(sampler, vec2(UV.s - 3.0*blur*hstep, UV.t - 3.0*blur*vstep)) * 0.064759;
    sum += texture2D(sampler, vec2(UV.s - 2.0*blur*hstep, UV.t - 2.0*blur*vstep)) * 0.120985;
    sum += texture2D(sampler, vec2(UV.s - 1.0*blur*hstep, UV.t - 1.0*blur*vstep)) * 0.176033;
    sum += texture2D(sampler, vec2(UV.s, UV.t)) * 0.199471;
    sum += texture2D(sampler, vec2(UV.s + 1.0*blur*hstep, UV.t + 1.0*blur*vstep)) * 0.176033;
    sum += texture2D(sampler, vec2(UV.s + 2.0*blur*hstep, UV.t + 2.0*blur*vstep)) * 0.120985;
    sum += texture2D(sampler, vec2(UV.s + 3.0*blur*hstep, UV.t + 3.0*blur*vstep)) * 0.064759;
    sum += texture2D(sampler, vec2(UV.s + 4.0*blur*hstep, UV.t + 4.0*blur*vstep)) * 0.026995;
    sum += texture2D(sampler, vec2(UV.s + 5.0*blur*hstep, UV.t + 5.0*blur*vstep)) * 0.008764;
    sum += texture2D(sampler, vec2(UV.s + 6.0*blur*hstep, UV.t + 6.0*blur*vstep)) * 0.002216;


    //discard alpha for our simple demo, multiply by vertex color and return
    color = vec4(vec3(sum.r), inShadow);
}