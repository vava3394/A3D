/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.texture.Texture;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Portal Valentin
 */
public class Sphere implements InterfaceDraw{
    
    private Map<FloatBuffer,Short> auxilliaireSub;
    
    private VBO glposbuffer;
    private VBO glelementtribuffer;
    private VBO glnormalbuffer;
    private VBO gltexturebuffer; 
    
    private Drawable drawable;
    
    float[] vertexpos;
    short[] triangles;
    float[] texture;
    
    private int offsetVertex = 0;
    private int offsetTriangle = 0;
    private short offsetPos = 0;
    
    public Sphere(final GL2 gl, float nbLongitude, float nbLatitude,float rayon){
        
        int[] buffers = new int[4]; // Besoin d’un buffer sur la carte graphique   
        gl.glGenBuffers(4, buffers, 0); // Allocation du buffer
        
        this.auxilliaireSub = new HashMap<>();
       
        int nbPos = (int) ((nbLatitude)*(nbLongitude)*3*2);
        
        int offset=0;
        
        vertexpos = new float[nbPos];
        
        texture = new float[(int) ((nbLatitude)*(nbLongitude)*2*2)];
        
        int nbTriangle =(int) ((nbLatitude+1)*(nbLongitude+1)*2)*3;
        
        triangles = new short[nbTriangle];
        
        float x,y,z,xy;
        
        float LongStep = (float) (2F*Math.PI/nbLongitude);
        float latStep = (float) (Math.PI/nbLatitude);
        
        float longAngle, latAngle;
        
        for(int i=0;i<=nbLatitude;i++){
            
            latAngle = (float) (-Math.PI/2+i*latStep);
            xy = (float) (rayon*Math.cos(latAngle));
            z = (float) (rayon*Math.sin(latAngle));
            
            for(int j=0;j<nbLongitude;j++){
                longAngle = j * LongStep;
                
                x = (float) (xy * Math.cos(longAngle));             // r * cos(u) * cos(v)
                y = (float) (xy * Math.sin(longAngle));             // r * cos(u) * sin(v)
                vertexpos[offset] = x;
                vertexpos[offset+1] = y;
                vertexpos[offset+2] = z;
                offset += 3;
            }
        }
        
        
        texture = new float[(vertexpos.length/3)*2];
        for (int i = 0, n = -1; i < vertexpos.length; i += 3) {
            x = vertexpos[i];
            y = vertexpos[i + 1];
            z = vertexpos[i + 2];

            double theta = (float) Math.asin(z);
            double phi = (float) Math.atan2(y, x);

            texture[++n] = 0.5f +(float) (phi / (2 * Math.PI));
            texture[++n] = 0.5f +(float) (theta / Math.PI);
        }
        
        offset = 0;
        short poslati = 0;
        short poslong = (short) (nbLongitude);
        
        for(int i=0;i<(int)nbLatitude;i++){
            for(int j=0; j<(int)nbLongitude;j++){
                triangles[offset] = poslati;
                triangles[offset+1]= (short) (poslati+1);
                triangles[offset+2]= poslong;//1er triangle
                triangles[offset+3] = poslong;
                triangles[offset+4]= (short) (poslati+1);
                triangles[offset+5]=(short) (poslong+1);//2eme triangle
                //carré
                
                poslati++;
                poslong++;
                offset+=6;
            }
        }
        
        float[] norm = new float[vertexpos.length];
        for (int i = 0; i < vertexpos.length; i++) {
            norm[i] = vertexpos[i];
        }
        
        this.glposbuffer = new VBO(gl, buffers[0]);
        this.glposbuffer.allocateBuffer(vertexpos, vertexpos.length);
        
        this.glnormalbuffer = new VBO(gl, buffers[1]);
        this.glnormalbuffer.allocateBuffer(norm, norm.length);
        
        this.glelementtribuffer = new VBO(gl, buffers[2]);
        this.glelementtribuffer.allocateBuffer(triangles, triangles.length);
        
        this.gltexturebuffer = new VBO(gl, buffers[3]);
        this.gltexturebuffer.allocateBuffer(texture, texture.length);

        this.drawable = new Drawable(glnormalbuffer, gltexturebuffer, glposbuffer, glelementtribuffer);
        
    }
    
    public Sphere(final GL2 gl, int sub){
        
        int[] buffers = new int[4]; // Besoin d’un buffer sur la carte graphique   
        gl.glGenBuffers(4, buffers, 0); // Allocation du buffer
        
        this.auxilliaireSub = new HashMap<>();
        
        int nbPoint=3; // nombre de point pour 1 triangle
        int nbTriangles=1; // nombre de triangle pour 1 face
        
        for(int i=sub; i>0;i--){ //pour chaque subdivision
            nbPoint+=i*Math.pow(3, i); // on ajoute (3 exposant subdivision) * subdivision
            nbTriangles*=4; // une face a maintenant 4 triangle en plus pour chaque triangle
        }
   
        nbPoint*=8;//8 triangles pour un octaèdre
        nbPoint*=3;// 3 coordonnées pour un point
        
        nbTriangles*=8;//8 triangles pour un octaèdre
        nbTriangles*=3;//3 coordonnées pour un triangle
        
        vertexpos = new float[nbPoint];//creation tableau
        triangles = new short[nbTriangles];//creation tableau
        
        float [][]s=new float [3][];
        
        s[0] = new float[] {1f,0,0};
        s[1] = new float[] {0,1f,0};
        s[2] = new float[] {0,0,1f};
        subdivision(s, sub);
        s[0] = new float[] {0,0,1f};
        s[1] = new float[] {0,1f,0};
        s[2] = new float[] {-1f,0,0};
        subdivision(s, sub);
        s[0] = new float[] {-1f,0,0};
        s[1] = new float[] {0,1f,0};
        s[2] = new float[] {0,0,-1f};
        subdivision(s, sub);
        s[0] = new float[] {0,0,-1f};
        s[1] = new float[] {0,1f,0};
        s[2] = new float[] {1f,0,0};
        subdivision(s, sub);
        /************************************/
        s[0] = new float[] {0,-1f,0};
        s[1] = new float[] {1f,0,0};
        s[2] = new float[] {0,0,1f};
        subdivision(s, sub);
        s[0] = new float[] {0,-1f,0};
        s[1] = new float[] {0,0,1f};
        s[2] = new float[] {-1f,0,0};
        subdivision(s, sub);
        s[0] = new float[] {0,-1f,0};
        s[1] = new float[] {-1f,0,0};
        s[2] = new float[] {0,0,-1f};
        subdivision(s, sub);
        s[0] = new float[] {0,-1f,0};
        s[1] = new float[] {0,0,-1f};
        s[2] = new float[] {1f,0,0};
        subdivision(s, sub);
        
        float[] norm = new float[vertexpos.length];
        norm = vertexpos;
        
        texture = new float[(vertexpos.length/3)*2];
        for (int i = 0, n = -1; i < vertexpos.length; i += 3) {
            float x = vertexpos[i];
            float y = vertexpos[i + 1];
            float z = vertexpos[i + 2];

            double theta = (float) Math.asin(z);
            double phi = (float) Math.atan2(y, x);

            texture[++n] = 0.5f +(float) (phi / (2 * Math.PI));
            texture[++n] = 0.5f +(float) (theta / Math.PI);
        }
        /*int texCursor = 0;
        for (int i = 0; i < vertexpos.length; i+= 3) {
            texture[texCursor] = Math.abs(vertexpos[i+2]+1)/2;
            texture[texCursor+1] = Math.abs(vertexpos[i+1]+1)/2;
            texCursor += 2;
        }*/
        
        
        this.glposbuffer = new VBO(gl, buffers[0]);
        this.glposbuffer.allocateBuffer(vertexpos, vertexpos.length);
        
        this.glnormalbuffer = new VBO(gl, buffers[1]);
        this.glnormalbuffer.allocateBuffer(norm, norm.length);
        
        this.glelementtribuffer = new VBO(gl, buffers[2]);
        this.glelementtribuffer.allocateBuffer(triangles, triangles.length);
        
        this.gltexturebuffer = new VBO(gl, buffers[3]);
        this.gltexturebuffer.allocateBuffer(texture, texture.length);
        
        this.auxilliaireSub.clear();
        
        this.drawable = new Drawable(glnormalbuffer, gltexturebuffer, glposbuffer, glelementtribuffer);
    }
    
    private void setTriangle(float[][] s){
        FloatBuffer f0 = FloatBuffer.wrap(s[0]);
        FloatBuffer f1 = FloatBuffer.wrap(s[1]);
        FloatBuffer f2 = FloatBuffer.wrap(s[2]);
        
        if(!this.auxilliaireSub.containsKey(f2)){
            vertexpos[offsetVertex] = s[2][0];
            vertexpos[offsetVertex+1] = s[2][1];
            vertexpos[offsetVertex+2] = s[2][2];
            offsetVertex+=3;
            triangles[offsetTriangle]=(short)(offsetPos);
            this.auxilliaireSub.put(f2, offsetPos);
            offsetPos++;
        }else{
            triangles[offsetTriangle]=this.auxilliaireSub.get(f2);
        }

        if(!this.auxilliaireSub.containsKey(f0)){
            vertexpos[offsetVertex] = s[0][0];
            vertexpos[offsetVertex+1] = s[0][1];
            vertexpos[offsetVertex+2] = s[0][2];
            offsetVertex+=3;
            triangles[offsetTriangle+1]=(short)(offsetPos);
            this.auxilliaireSub.put(f0, offsetPos);
            offsetPos++;
        }else{
            triangles[offsetTriangle+1]=this.auxilliaireSub.get(f0);
        }

        if(!this.auxilliaireSub.containsKey(f1)){
            vertexpos[offsetVertex] = s[1][0];
            vertexpos[offsetVertex+1] = s[1][1];
            vertexpos[offsetVertex+2] = s[1][2]; 
            offsetVertex+=3;
            triangles[offsetTriangle+2]=(short)(offsetPos);
            this.auxilliaireSub.put(f1, offsetPos);
            offsetPos++;
        }else{
            triangles[offsetTriangle+2]=this.auxilliaireSub.get(f1);
        }
        offsetTriangle+=3;
    }
    
   
    private void subdivision(float[][] s,int sub){
        if(sub!=0){
            //calcule des millieu des droites qui forme le triangle
            float[] s0 = {(s[1][0]+s[0][0])/2f,(s[1][1]+s[0][1])/2f,(s[1][2]+s[0][2])/2f};
            float[] s1 = {(s[1][0]+s[2][0])/2f,(s[1][1]+s[2][1])/2f,(s[1][2]+s[2][2])/2f};
            float[] s2 = {(s[2][0]+s[0][0])/2f,(s[2][1]+s[0][1])/2f,(s[2][2]+s[0][2])/2f};
            
            //calcule de la norme
            float normS0 = (float) Math.sqrt(s0[0]*s0[0]+s0[1]*s0[1]+s0[2]*s0[2]) ;
            float normS1 = (float) Math.sqrt(s1[0]*s1[0]+s1[1]*s1[1]+s1[2]*s1[2]) ;
            float normS2 = (float) Math.sqrt(s2[0]*s2[0]+s2[1]*s2[1]+s2[2]*s2[2]) ;
            
            s0[0] = s0[0]/normS0;
            s0[1] = s0[1]/normS0;
            s0[2] = s0[2]/normS0;
            
            s1[0] = s1[0]/normS1;
            s1[1] = s1[1]/normS1;
            s1[2] = s1[2]/normS1;
            
            s2[0] = s2[0]/normS2;
            s2[1] = s2[1]/normS2;
            s2[2] = s2[2]/normS2;
            
            //variable permettant d'évité de sauvegarder les coordonnées des points dans le tableau vertexpos
            float [][]sAux=new float [3][];
            
            //definition du triangle du millieu
            sAux[0] = s0;
            sAux[1] = s1;
            sAux[2] = s2;
            //on le subdivise
            subdivision(sAux, sub-1);
            
            //définition du triangle du haut
            sAux[0] = s0;
            sAux[1] = s[1];
            sAux[2] = s1;
            //on le subdivise
            subdivision(sAux, sub-1);
            
            //définition du triangle de droite
            sAux[0] = s[0];
            sAux[1] = s0;
            sAux[2] = s2;
            //on le subdivise
            subdivision(sAux, sub-1);//triangles de droite
            
            //définition du triangle de gauche
            sAux[0] = s2;
            sAux[1] = s1;
            sAux[2] = s[2];
            //on le subdivise
            subdivision(sAux, sub-1);
        }else{
            setTriangle(s);
        }
    }
       
    @Override
    public void draw(final GL2 gl,final LightingShaders shaders,float[] color){
        this.drawable.draw(gl, shaders, color, null);
    }
    
    @Override
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture,float [] color){
        this.drawable.draw(gl, shaders, color, texture);
    }
    

    @Override
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture){
        this.drawable.draw(gl, shaders, null, texture);
    }

    @Override
    public void setPosition(GL2 gl, LightingShaders shaders, Vec3f pos,Scene scene) {
        Matrix4 m = new Matrix4();
        
        Outils.setMatrixZero(m, scene.anglex, scene.angley, scene.x, scene.y, scene.z);
        m.translate(pos.x, pos.y, pos.z);
        
        shaders.setModelViewMatrix(m.getMatrix());
    }

    @Override
    public void setPositionMirroir(GL2 gl, LightingShaders shaders, Vec3f pos,Scene scene) {
        Matrix4 m = new Matrix4();
        
        Outils.setMatrixZeroScale(m, scene.anglex, scene.angley, scene.x, scene.y, scene.z);
        m.translate(pos.x, pos.y, pos.z);
        
        shaders.setModelViewMatrix(m.getMatrix());
    }
}
