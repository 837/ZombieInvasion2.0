#version 330

in vec2 UV;


uniform sampler2D mapSampler;//tex1
uniform vec2 renderTargetSize;

out vec4 result;

float GetShadowDistanceH(vec2 TexCoord, float displacementV);
float GetShadowDistanceV(vec2 TexCoord, float displacementV);


void main(){
  //TODO this is kindof a workaround, because drawing back to a slick image flips the texture vertically
  //TODO because slicks coordinate origin doesn't agree with openGL
  vec2 nUV = vec2(UV.x,1-UV.y);

	// distance of this pixel from the center
  float distance = length(nUV - vec2(0.5));
  distance *= renderTargetSize.x;
	//apply a 2-pixel bias
	distance = distance -2f;

	//distance stored in the shadow map
	float shadowMapDistance;

	//coords in [-1,1]
	float nY = 2.0f*( nUV.y - 0.5f);
  float nX = 2.0f*( nUV.x - 0.5f);

	//use these to determine which quadrant we are in
	if(abs(nY)<abs(nX))
	{ //left or right segment
		shadowMapDistance = GetShadowDistanceH(nUV,0);
	}
	else
	{ //upper or lower segment
	  shadowMapDistance = GetShadowDistanceV(nUV,0);
	}
	//if distance to this pixel is lower than distance from shadowMap,
	//then we are not in shadow
	float light = distance < shadowMapDistance? 1:0;
	result = vec4(light,0,length(nUV-0.5f),1);
}


float GetShadowDistanceH(vec2 TexCoord, float displacementV)
{
		float u = TexCoord.x;
		float v = TexCoord.y;

		u = abs(u-0.5f) * 2;
		v = v * 2 - 1;
	  float v0 = v/u;
		v0+=displacementV;
		v0 = (v0 + 1) / 2;

		vec2 newCoords = vec2(1-TexCoord.x,1-v0);
		//horizontal info was stored in the Red component
		return texture2D(mapSampler, newCoords).r;
}

float GetShadowDistanceV(vec2 TexCoord, float displacementV)
{
		float u = TexCoord.y;
		float v = TexCoord.x;

		u = abs(u-0.5f) * 2;
		v = v * 2 - 1;
		float v0 = v/u;
		v0+=displacementV;
		v0 = (v0 + 1) / 2;

		vec2 newCoords = vec2(TexCoord.y,v0);
		//vertical info was stored in the Green component
		return texture2D(mapSampler, newCoords).g;
}