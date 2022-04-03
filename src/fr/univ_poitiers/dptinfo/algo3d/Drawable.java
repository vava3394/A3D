/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Portal Valentin
 */
public class Drawable {
    
    private VBO norm;
    private VBO textures;
    private VBO pos;
    private VBO triangles;
    
    private int typePosNorm;
    private int typeTriangle;
    private int typeTexture;
    
    public Drawable(VBO norm,VBO textures,VBO pos,VBO triangles){
        this.norm = norm;
        this.textures = textures;
        this.pos = pos;
        this.triangles = triangles;
        this.typePosNorm = GL2.GL_FLOAT;
        this.typeTexture = GL2.GL_FLOAT;
        this.typeTriangle = GL2.GL_UNSIGNED_SHORT;
    }
    
    public Drawable(VBO norm,VBO textures,VBO pos,VBO triangles,int typePosNorm,int typeTexture,int typeTriangle){
        this.norm = norm;
        this.textures = textures;
        this.pos = pos;
        this.triangles = triangles;
        this.typePosNorm = typePosNorm;
        this.typeTexture = typeTexture;
        this.typeTriangle = typeTriangle;
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,final float[] colors,final Texture texture){
        
        if(colors == null){
            shaders.setMaterialColor(MyGLRenderer.white);
        }else{
            shaders.setMaterialColor(colors);
        }
        
        if(texture != null){
            shaders.setIsTexture(true);
            
            gl.glActiveTexture(GL2.GL_TEXTURE0); // Activation de l’unité 0 (attention au paramètre)
            texture.bind(gl);
            // associer la texture à l’unité courante (objet fourni par loadTexture)
            shaders.setTextureUnit(0); 
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,textures.getBuffer());
            // texcoordbuffer est le buffer de la carte graphique
            //contenant les coordonnées de texture des sommets
            shaders.setTextureCoordsPointer(2, this.typeTexture);
            // aTexCoord est l’attribut des shaders dédié aux cooordonnées de texture des sommets
        }else{
            shaders.setIsTexture(false);
        }
          
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,norm.getBuffer());
        shaders.setNormalsPointer(3,this.typePosNorm);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,pos.getBuffer());
        shaders.setPositionsPointer(3,this.typePosNorm);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, triangles.getBuffer());
        gl.glDrawElements(GL2.GL_TRIANGLES, triangles.getSize(),this.typeTriangle, 0);
        
        
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0); 
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,0); 

    }
    
}
