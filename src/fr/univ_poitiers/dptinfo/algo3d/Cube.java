package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Portal Valentin
 */
public class Cube{
    
    private VBO glposbuffer;
    private VBO glelementtribuffer;
    private VBO gltexturebuffer;
    private VBO glnormalebuffer;
    
    private Drawable drawable;

    public Cube(GL2 gl,float lenght){
        int[] buffers = new int[4]; // Besoin d’un buffer sur la carte graphique               
        gl.glGenBuffers(4, buffers, 0); // Allocation du buffer
        
        float[] vertexpos = new float[]{
            //haut
            lenght/2,0,lenght/2,
            lenght/2,0,-lenght/2,
            -lenght/2,0,lenght/2,
            -lenght/2,0,-lenght/2,

            //bas
            lenght/2,lenght,lenght/2,
            lenght/2,lenght,-lenght/2,
            -lenght/2,lenght,lenght/2,
            -lenght/2,lenght,-lenght/2,
            
            //droite
            lenght/2,0,lenght/2,
            lenght/2,0,-lenght/2,
            lenght/2,lenght,-lenght/2,
            lenght/2,lenght,lenght/2,
            
            //gauche
            -lenght/2,0,lenght/2,
            -lenght/2,0,-lenght/2,
            -lenght/2,lenght,-lenght/2,
            -lenght/2,lenght,lenght/2,

            //devant
            lenght/2,0,-lenght/2,
            -lenght/2,0,-lenght/2,
            lenght/2,lenght,-lenght/2,
            -lenght/2,lenght,-lenght/2,

            //derriere
            lenght/2,0,lenght/2,
            -lenght/2,0,lenght/2,
            lenght/2,lenght,lenght/2,
            -lenght/2,lenght,lenght/2,
        };

        short[] triangles = new short[]{
            0,2,1,
            3,1,2,
            
            4,5,6,
            7,6,5,
            
            0,4,2,
            2,4,6,
            
            2,6,3,
            3,6,7,
            
            3,7,1,
            1,7,5,
            
            1,5,0,
            0,5,4
        };
        
        float[] norm = new float[]{
            0,-1,0,
            0,-1,0,
            0,-1,0,
            0,-1,0,
            
            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0,
 
            -1,0,0,
            -1,0,0,
            -1,0,0,
            -1,0,0,
            
            1,0,0,
            1,0,0,
            1,0,0,
            1,0,0,
            
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1,
            
            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,-1,
        };
        
        float[] texture = new float[]{
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
        };
        
        glposbuffer = new VBO(gl, buffers[0]);
        glposbuffer.allocateBuffer(vertexpos, vertexpos.length);
        
        glelementtribuffer = new VBO(gl, buffers[1]);
        glelementtribuffer.allocateBuffer(triangles, triangles.length);
        
        gltexturebuffer = new VBO(gl, buffers[2]);
        gltexturebuffer.allocateBuffer(texture, texture.length);
        
        glnormalebuffer = new VBO(gl, buffers[3]);
        glnormalebuffer.allocateBuffer(norm, norm.length);
        
        drawable =new Drawable(glnormalebuffer, gltexturebuffer, glposbuffer, glelementtribuffer);
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,float[] color){
        this.drawable.draw(gl, shaders, color, null);
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture,float [] color){
        this.drawable.draw(gl, shaders, color, texture);
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture){
        this.drawable.draw(gl, shaders, null, texture);
    }
    
}
