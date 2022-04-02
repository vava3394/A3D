precision mediump float;

#define NR_POINT_LIGHTS 1

uniform bool uLighting;
uniform bool uNormalizing;
uniform bool uIsTexture;

uniform sampler2D uTextureUnit;

uniform mat3 uNormalMatrix;
uniform vec4 uAmbiantLight;
uniform vec4 uMaterialColor;
uniform vec3 uViewPos;

struct PointLight{
    vec4 uLightColor;
    vec4 uMaterialSpecular;
    vec4 uLightSpecular;
    float uConstantAttenuation;
    float uLinearAttenuation;
    float uQuadraticAttenuation;
    float uMaterialShininess;
    vec3 uLightPos;
};

uniform PointLight uPointLights [NR_POINT_LIGHTS];

// Only color is interpolated
varying vec2 vTexCoord;
varying vec3 vVertexNormal;
varying vec4 vVertexPosition;

vec4 calcLight(PointLight light, vec3 normal, vec4 texture,vec3 fragPos, vec3 ViewDir, vec4 Ambiant){
    //vecteur directeur lumière
    vec3 lightdir=normalize(light.uLightPos - fragPos);


    //speculaire
    vec3 halfwayDir = normalize(lightdir+ViewDir);
    vec4 specular = light.uLightSpecular * pow(max(dot(normal, halfwayDir),0.0),light.uMaterialShininess)* light.uMaterialSpecular;

    //diffusion
    float weight = max(dot(normal, lightdir),0.0);
    vec4 diffuse = weight*light.uLightColor;
    
    vec4 result = (uMaterialColor*texture)*(Ambiant+diffuse)+ specular;
    result.a=uMaterialColor.a;
    return result;
}

void main(void) {
    vec4 texture = vec4(1, 1, 1, 1);
    if (uIsTexture) {
        texture = texture2D(uTextureUnit,vTexCoord);
    }

    if(uLighting){
        //normal
        vec3 normal = uNormalMatrix * vVertexNormal;
        if (uNormalizing) normal=normalize(normal);

        vec3 ViewDir=normalize(uViewPos - vVertexPosition.xyz);
        
        vec4 result = vec4(0,0,0,0);
        for(int i = 0; i < NR_POINT_LIGHTS; i++)
            result += calcLight(uPointLights[i], normal, texture,vVertexPosition.xyz, ViewDir,uAmbiantLight);
        

        gl_FragColor = result;
        gl_FragColor.a = uMaterialColor.a;

    }else{
        gl_FragColor = (uMaterialColor*texture);
        gl_FragColor.a=uMaterialColor.a;
    }

}