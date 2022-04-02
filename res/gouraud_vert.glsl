#define NR_POINT_LIGHTS 1

// Matrices
    uniform mat4 uModelViewMatrix;
    uniform mat4 uProjectionMatrix;
    uniform mat3 uNormalMatrix;

// Light source definition
    uniform vec4 uAmbiantLight;
    uniform bool uLighting;
struct PointLight{
    vec4 uLightColor;
    vec3 uLightPos;
};

uniform PointLight uPointLights [NR_POINT_LIGHTS];

// Material definition
    uniform bool uNormalizing;
    uniform vec4 uMaterialColor;

// vertex attributes
    attribute vec3 aVertexPosition;
    attribute vec3 aVertexNormal;

// Interpolated data
    varying vec4 vColor;
    varying vec2 vTexCoord;

attribute vec2 aTexCoord;

void main(void) {
        vTexCoord = aTexCoord;
	vec4 pos=uModelViewMatrix*vec4(aVertexPosition, 1.0);
	if (uLighting)
	{
          vec3 normal = uNormalMatrix * aVertexNormal;
	  if (uNormalizing) normal=normalize(normal);
	  vec3 lightdir=normalize(uPointLights[0].uLightPos-pos.xyz);
	  float weight = max(dot(normal, lightdir),0.0);
	  vColor = uMaterialColor*(uAmbiantLight+weight*uPointLights[0].uLightColor);
        }
	else vColor = uMaterialColor;
	gl_Position= uProjectionMatrix*pos;
}
