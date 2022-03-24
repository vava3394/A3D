package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Matrix4;

/**
 *
 * @author Portal Valentin
 */
public class Piedestal {
    Cube cube;
    
    float y,min,max,step;
    
    float scale;
    
    Scene scene;
    
    public Piedestal(GL2 gl,float lenght,float step,Scene scene){
        this.cube = new Cube(gl,lenght);
        this.scale = 0.5f;
        this.min = lenght+scale;
        this.max = min+scale;
        this.y = min;
        this.step = step;
        this.scene = scene;
    }
    
    public void draw(final GL2 gl,final LightingShaders shaders,Matrix4 m,float[] color,Vec3f pos,float scale,Sphere obj){

        m.translate(pos.x,0,pos.z);
        
        shaders.setModelViewMatrix(m.getMatrix());
        cube.draw(gl, shaders, MyGLRenderer.lightgray);
        
        if(y>max){
            step = -step;
        }else if(y<min){
            step = -step;
        }
        y+=step;
        m.translate(0,y,0);
        m.rotate(scene.angleRotationSphereAux*2,0.0F,0.1F,0.0F);//rotation autour d'elle même
        m.scale(scale, scale, scale);
        shaders.setModelViewMatrix(m.getMatrix());
        obj.draw(gl, shaders, color);
    }
    
}
