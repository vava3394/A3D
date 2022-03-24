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
 * @author vava3
 */
public class Cone{
    
    VBO glposbuffer;
    VBO glelementtribuffer;
    VBO glnormbuffer;

    Drawable drawable;
    
    public Cone(GL2 gl,float rayon,float hauteur,int div){
        
        int[] buffers = new int[4]; // Besoin d’un buffer sur la carte graphique   
        gl.glGenBuffers(4, buffers, 0); // Allocation du buffer
        
        int nbPos = (div+2)*3+3;//+2 pour les deux points du centre (celui du cercle et celui du pic du cone)
        int nbTriangle = div*div*3;//il y a div*divde triangle *3 pour les coordonnées des triangles
        
        int offset = 0;
        int offsetNorm=0;
        
        float step = (float) (2F*Math.PI/div);//découpage du cercle
        float Angle;
        
        float[] vertexpos = new float[nbPos];//creation tableau
        short[] triangles = new short[nbTriangle];//creation tableau
        
        //centre du cercle
        vertexpos[offset] = 0f;
        vertexpos[offset+=1] = hauteur/2f;
        vertexpos[offset+=1] = 0f;
        
        //découpage du cercle en plusieurs points
        for (int i = 0; i <= div; i++) {
            Angle = i * step;
            vertexpos[offset+=1] = (float) (rayon*Math.cos(Angle));
            vertexpos[offset+=1] = hauteur/2f;
            vertexpos[offset+=1] = (float) (rayon*Math.sin(Angle));
        }
        
        //pic du cone
        vertexpos[offset+=1] = 0f;
        vertexpos[offset+=1] = -hauteur/2f;
        vertexpos[offset+1] = 0f;
        
        
        offset=0;
        short pos = 0;//position sommet
        
        for (int i = 0; i <=div; i++) {
            triangles[offset] = 0;//correspond au centre du cercle
            triangles[offset+1] = pos;
            triangles[offset+2] = (short) (pos+1);
            
            offset+=3;
            
            triangles[offset] = (short) (nbPos/3);//correspond au pic du cone
            triangles[offset+1] = pos;
            triangles[offset+2] = (short) (pos+1);
            
            offset+=3;
            pos+=1;
            
            
        }
        
        float[] norm = new float[nbPos];
        
        float [] normCaluler = Outils.calculNormal(
                new float[] {vertexpos[0],vertexpos[1],vertexpos[2]}, 
                new float[] {vertexpos[3],vertexpos[4],vertexpos[5]},
                new float[] {vertexpos[6],vertexpos[7],vertexpos[8]}
        );
        
        for (int i = 0; i < div+1; i++) {
            norm[offsetNorm] = normCaluler[0];
            norm[offsetNorm+1] = normCaluler[1];
            norm[offsetNorm+2] = normCaluler[2];
            offsetNorm+=3;
        }
        offsetNorm+=3;

        
        glposbuffer = new VBO(gl, buffers[0]); 
        glposbuffer.allocateBuffer(vertexpos, vertexpos.length);
        
        glelementtribuffer = new VBO(gl, buffers[1]); 
        glelementtribuffer.allocateBuffer(triangles, triangles.length);
        
        glnormbuffer = new VBO(gl, buffers[2]);
        glnormbuffer.allocateBuffer(norm, norm.length);
        
        
        drawable = new Drawable(
            glnormbuffer,
            null, 
            glposbuffer, 
            glelementtribuffer
        );
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,float[] color){
        this.drawable.draw(gl, shaders, color, null);
    }

}
