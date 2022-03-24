/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.math.Matrix4;

/**
 *
 * @author vava3
 */
public class Outils {
   
    public static float[] vec4MultMatrix4(float []vec, float[] mat){
        
        return new float[]{
            (vec[0]*mat[0]) + (vec[1]*mat[4]) + (vec[2]*mat[8]) + (vec[3]*mat[12]),
            (vec[0]*mat[1]) + (vec[1]*mat[5]) + (vec[2]*mat[9]) + (vec[3]*mat[13]),
            (vec[0]*mat[2]) + (vec[1]*mat[6]) + (vec[2]*mat[10]) + (vec[3]*mat[14]),
            (vec[0]*mat[3]) + (vec[1]*mat[7]) + (vec[2]*mat[11]) + (vec[3]*mat[15]),
        };
    }
    
    public static float[] calculNormal(float[]a,float[] b,float[] c){
        float[] v = new float[]{b[0]-a[0],b[1]-a[1],b[2]-a[2]};
        float[] u = new float[]{c[0]-a[0],c[1]-a[1],c[2]-a[2]};
        float[] Vnorm = new float[]{
            u[1]*v[2]-u[2]*v[1],
            u[2]*v[0]-u[0]*v[2],
            u[0]*v[1]-u[1]*v[0]
        };
        
        float norm = (float) Math.sqrt(Vnorm[0]*Vnorm[0]+Vnorm[1]*Vnorm[1]+Vnorm[2]*Vnorm[2]);
        
        return new float[]{Vnorm[0]/norm,Vnorm[1]/norm,Vnorm[2]/norm};
    }
    
    public static void setMatrixZero(Matrix4 m,float anglex,float angley,float x,float y,float z){
        m.loadIdentity();
        m.rotate(anglex,-0.1F,0.F,0.0F);
        m.rotate(angley,0.0F,-0.1F,0.0F);
        m.translate(x,y-1.6F,z);
    }
    
    public static void setMatrixZeroScale(Matrix4 m,float anglex,float angley,float x,float y,float z){
        m.loadIdentity();
        
        m.rotate(anglex,-0.1F,0.F,0.0F);
        m.rotate(angley,0.0F,-0.1F,0.0F);
        m.translate(x,y-1.6F,z);
        m.scale(1, -1, 1);
    }
}
