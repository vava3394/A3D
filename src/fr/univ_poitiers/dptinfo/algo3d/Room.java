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
public class Room {
        
    private final VBO glposbuffer;
    private final VBO glelementtriFloorbuffer;
    private final VBO glelementtriCellingbuffer;
    private final VBO glelementtriWallsbuffer;
    private final VBO glelementNormal;
    private final VBO gltexturebuffer;
    
    private final Drawable drawableFloor;
    private final Drawable drawableCelling;
    private final Drawable drawableWall;
    
    
    public Room(final GL2 gl, float lenght,float wallsize){
        
        int[] buffers = new int[6]; // Besoin d’un buffer sur la carte graphique   
        gl.glGenBuffers(6, buffers, 0); // Allocation du buffer
        
        glposbuffer = new VBO(gl,buffers[0]); 
        glelementNormal = new VBO(gl,buffers[1]); 
        //tiangle floor
        glelementtriFloorbuffer = new VBO(gl,buffers[2]); 
        //tiangle celling
        glelementtriCellingbuffer = new VBO(gl,buffers[3]); 
        //tiangle walls
        glelementtriWallsbuffer = new VBO(gl,buffers[4]); 
        gltexturebuffer = new VBO(gl,buffers[5]); 
        
        int scaleHeightDoor = (int) (wallsize/2);
        float scaleWidthDoor = (lenght-1);
        
        float[] vertexpos = {
            -lenght,0F,-lenght,  //sommet 0
            -lenght,0F,lenght,  //sommet 1
            lenght,0F,-lenght,  //sommet 2
            lenght,0F,lenght,  //sommet 3 floor
            
            -lenght,wallsize,-lenght,  //sommet 4
            -lenght,wallsize,lenght,  //sommet 5
            lenght,wallsize,-lenght,  //sommet 6
            lenght,wallsize,lenght,  //sommet 7 celling
            
            -lenght,0F,lenght,  //sommet 8
            -lenght,0F,-lenght,  //sommet 9
            -lenght,wallsize,lenght,  //sommet 10
            -lenght,wallsize,-lenght,  //sommet 11 wall left
            
            lenght,0F,lenght,  //sommet 12
            -lenght,0F,lenght,  //sommet 13
             lenght,wallsize,lenght,  //sommet 14
            -lenght,wallsize,lenght,  //sommet 15 wall front
           
            
            lenght,0F,lenght,  //sommet 16
            lenght,0F,-lenght,  //sommet 17
            lenght,wallsize,lenght, //sommet 18   
            lenght,wallsize,-lenght,  //sommet 19 wall right 
            
            
            -lenght,0,-lenght, //20
            -lenght+scaleWidthDoor,0,-lenght,//21
            -lenght,wallsize-scaleHeightDoor,-lenght,//22
            -lenght+scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//23
            
            -lenght,wallsize-scaleHeightDoor,-lenght,//24
            -lenght+scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//25
            -lenght,wallsize,-lenght,//26
            -lenght+scaleWidthDoor,wallsize,-lenght,//27          
            
            lenght-scaleWidthDoor,0,-lenght,//28
            lenght,0,-lenght,//29
            lenght-scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//30
            lenght,wallsize-scaleHeightDoor,-lenght,//31
            
            lenght-scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//32
            lenght,wallsize-scaleHeightDoor,-lenght,//33
            lenght-scaleWidthDoor,wallsize,-lenght,//34
            lenght,wallsize,-lenght,//35

            -lenght+scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//36
            lenght-scaleWidthDoor,wallsize-scaleHeightDoor,-lenght,//37
            -lenght+scaleWidthDoor,wallsize,-lenght,//38
            lenght-scaleWidthDoor,wallsize,-lenght,//39 wall back
            
        };
        
        this.glposbuffer.allocateBuffer(vertexpos,vertexpos.length);
        
        short[] trianglesFloor = {
            1,3,0,
            0,3,2,//floor
            
        };
        
        this.glelementtriFloorbuffer.allocateBuffer(trianglesFloor,trianglesFloor.length);

        
        short[] trianglesCelling = {
            7,5,6,
            6,5,4,//ceiling
        };
        
        this.glelementtriCellingbuffer.allocateBuffer(trianglesCelling, trianglesCelling.length);
        
        short[] trianglesWalls = {
            8,9,10,
            10,9,11,//mur left
            12,13,14,
            14,13,15,//mur back
            16,18,17,
            17,18,19,//mur droit
            20,21,22,
            22,21,23,
            24,25,26,
            26,25,27,
            28,29,30,
            30,29,31,
            32,33,34,
            34,33,35,          
            36,37,38,
            38,37,39//mur de face avec porte
        };
        
        this.glelementtriWallsbuffer.allocateBuffer(trianglesWalls, trianglesWalls.length);
        
        float[] norm = {
            0, 1, 0, // Sol
            0, 1, 0, 
            0, 1, 0, 
            0, 1, 0,
            
            0, -1, 0, // Plafond
            0, -1, 0, 
            0, -1, 0, 
            0, -1, 0,
                
            1, 0, 0, // Gauche
            1, 0, 0, 
            1, 0, 0, 
            1, 0, 0,
            
            0, 0, -1, // Back
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            
            -1, 0, 0, // droite
            -1, 0, 0, 
            -1, 0, 0, 
            -1, 0, 0,
            
            0, 0, 1, // Front
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            
            0, 0, 1, // Front
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            
            0, 0, 1, // Front
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            
            0, 0, 1, // Front
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            
            0, 0, 1, // Front
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
        };
        
        this.glelementNormal.allocateBuffer(norm, norm.length);
        
        float[] texture ={
            0,0,
            1,0,
            0,1,
            1,1,
            
            0,0,
            1,0,
            0,1,
            1,1,
            
            
            1,0,
            0,0,
            
            1,1,
            0,1,

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
            
            0,0,
            1,0,
            0,1,
            1,1   
        };
  
        this.gltexturebuffer.allocateBuffer(texture, texture.length);
        
        this.drawableFloor = new Drawable(
            glelementNormal,
            gltexturebuffer, 
            glposbuffer, 
            glelementtriFloorbuffer
        );
        
        this.drawableCelling = new Drawable(
            glelementNormal,
            gltexturebuffer, 
            glposbuffer, 
            glelementtriCellingbuffer
        );
        
        this.drawableWall = new Drawable(
            glelementNormal,
            gltexturebuffer, 
            glposbuffer, 
            glelementtriWallsbuffer
        );
        
    }
   
    public void draw (GL2 gl, LightingShaders shaders, float[] colorFloor, float[] colorCelling, float[] colorWall, boolean isMirroirFloor){
        if(isMirroirFloor){
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
            colorFloor[3] = 0.5f;
        }       
        this.drawableFloor.draw(gl, shaders, colorFloor,null);
        gl.glDisable(GL2.GL_BLEND);
        this.drawableCelling.draw(gl, shaders, colorFloor,null);
        this.drawableWall.draw(gl, shaders, colorFloor,null);
    }
    
    public void draw (GL2 gl, LightingShaders shaders, Texture textureFloor, Texture textureCelling, Texture textureWall,boolean isMirroirFloor){
        float[] colorFloor = new float[]{1,1,1,1};
        if(isMirroirFloor){
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
            colorFloor[3] = 0.7f;
        } 
        this.drawableFloor.draw(gl, shaders, colorFloor,textureFloor);
        gl.glDisable(GL2.GL_BLEND);
        
        this.drawableCelling.draw(gl, shaders, null,textureCelling);
        this.drawableWall.draw(gl, shaders, null,textureWall);
    }
    
    public void drawMirroir(GL2 gl, LightingShaders shaders, Texture textureCelling, Texture textureWall){
        this.drawableCelling.draw(gl, shaders, null,textureCelling);
        this.drawableWall.draw(gl, shaders, null,textureWall);
    }
    
}
