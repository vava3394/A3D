package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Portal Valentin
 */


public class Model extends Loader{

    public static enum nameModel {Lara_Croft,Doom,caco,Armadillo,dragon,bateau,cube};//enum de tous les noms de fichiers .obj du projet 

    private Vec3f pos;

    
    public Model(final GL2 gl, nameModel nameFile, Vec3f pos,double scale){
        super(gl,nameFile.toString()+".obj",scale);//on construit le model à partir d'un fichier .obj 
        this.pos = pos;
    }
    
    public Model(final GL2 gl, nameModel nameFile, Vec3f pos ){
        super(gl,nameFile.toString()+".obj",1);
        this.pos = pos;
    }
    
    private void rotation(final LightingShaders shaders,float rotation, Scene scene){
        Matrix4 m = new Matrix4();

        Outils.setMatrixZero(m, scene.anglex, scene.angley, scene.x, scene.y, scene.z);
        m.translate(pos.x,pos.y,pos.z);
        
        m.rotate(rotation*2f,0F,0.1F,0F);//tourne autour de lui même
        shaders.setModelViewMatrix(m.getMatrix());
    }

    public void draw(final GL2 gl,final LightingShaders shaders,float[] color,float rotation, Scene scene){   
        rotation(shaders, rotation, scene);
        drawable.draw(gl, shaders, color, null);
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture,float rotation, Scene scene){   
        rotation(shaders, rotation, scene);
        drawable.draw(gl, shaders, null, texture);
    }
    
    public void drawMirroir(final GL2 gl,final LightingShaders shaders,float[] color,float rotation, Scene scene){   
        Matrix4 m = new Matrix4();

        Outils.setMatrixZeroScale(m, scene.anglex, scene.angley, scene.x, scene.y, scene.z);
        m.translate(pos.x,0,pos.z);
        
        m.rotate(rotation*2f,0F,0.1F,0F);//tourne autour de lui même
        shaders.setModelViewMatrix(m.getMatrix());
        
        drawable.draw(gl, shaders, color, null);
    }

}
