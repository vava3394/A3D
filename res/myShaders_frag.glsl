precision mediump float;

#define NR_POINT_LIGHTS 4

struct Material{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

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

uniform bool uLighting;
uniform bool uNormalizing;
uniform bool uIsTexture;
uniform bool uUseFlashLight;

uniform sampler2D uTextureUnit;

uniform mat3 uNormalMatrix;
uniform vec4 uAmbiantLight;
uniform vec4 uMaterialColor;
uniform vec3 uViewPos;

uniform PointLight uFlashMaterial;
uniform vec3 uDirectionFlashLight;
uniform float uCutOff;
uniform float uOuterCutOff;

uniform PointLight uPointLights [NR_POINT_LIGHTS];

// Only color is interpolated
varying vec2 vTexCoord;
varying vec3 vVertexNormal;
varying vec4 vVertexPosition;

Material calcLight(PointLight light, vec3 normal, vec4 texture,vec3 fragPos, vec3 ViewDir, vec4 ambiant){
    //vecteur directeur lumière
    vec3 lightdir=normalize(light.uLightPos - fragPos);


    //speculaire
    vec3 halfwayDir = normalize(lightdir+ViewDir);
    vec4 specular = light.uLightSpecular * pow(max(dot(normal, halfwayDir),0.0),light.uMaterialShininess)* light.uMaterialSpecular;

    //diffusion
    float weight = max(dot(normal, lightdir),0.0);
    vec4 diffuse = weight*light.uLightColor;

    //attenuation via distance
    float distance = length(light.uLightPos - fragPos);
    float attenuation = 1.0/(light.uConstantAttenuation + light.uLinearAttenuation 
                             * distance + light.uQuadraticAttenuation * (distance*distance));
    ambiant *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    return new Material(ambiant,diffuse,specular);
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
        Material m;

        if(uUseFlashLight){
            m = calcLight(uFlashMaterial, normal, texture,vVertexPosition.xyz, ViewDir,uAmbiantLight);
            //flashLight
            float theta = dot(normalize(uFlashMaterial.uLightPos - vVertexPosition.xyz),normalize(-uDirectionFlashLight));
            float epsilon   = uCutOff - uOuterCutOff;
            float intensity = clamp((theta - uOuterCutOff) / epsilon, 0.0, 1.0);

            m.ambient *= intensity;
            m.diffuse *= intensity;
            m.specular *= intensity;
            result += (uMaterialColor*texture)*(m.ambient+m.diffuse)+ m.specular;
        }

        for(int i = 0; i < NR_POINT_LIGHTS; i++){
            m = calcLight(uPointLights[i], normal, texture,vVertexPosition.xyz, ViewDir,uAmbiantLight);
            result += (uMaterialColor*texture)*(m.ambient+m.diffuse)+ m.specular;
        }

        gl_FragColor = result;
        gl_FragColor.a = uMaterialColor.a;

    }else{
        gl_FragColor = (uMaterialColor*texture);
        gl_FragColor.a=uMaterialColor.a;
    }

}