#version 330

in vec2 UV;
uniform sampler2D inputSampler;

out vec4 distortedTexel;

void main(){

	//translate u and v from [0,1] into [-1 , 1] domain
	float u0 = UV.s * 2.0f - 1.0f;
	float v0 = UV.t * 2.0f - 1.0f;

	//then, as u0 approaches 0 (the center), v should also approach 0
	v0 = v0 * abs(u0);
	//convert back from [-1,1] domain to [0,1] domain
	v0 = (v0 + 1.0f) / 2.0f;

	vec2 newCoords = vec2(UV.s,v0);

	float horizontal = texture2D(inputSampler, newCoords).r;
	float vertical = texture2D(inputSampler,newCoords.ts).r;

	//read for both horizontal and vertical direction and store them in separate channels
	distortedTexel  = vec4(horizontal,vertical ,0,1);
}