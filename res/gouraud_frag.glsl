precision mediump float;

uniform bool uIsTexture;
uniform sampler2D uTextureUnit;
uniform vec4 uMaterialColor;

// Only color is interpolated
varying vec4 vColor;
varying vec2 vTexCoord;

void main(void) {
        vec4 texture = vec4(1, 1, 1, 1);
        if (uIsTexture) {
            texture = texture2D(uTextureUnit,vTexCoord);
        }
	gl_FragColor = vColor*texture;
        gl_FragColor.a = uMaterialColor.a;
}
