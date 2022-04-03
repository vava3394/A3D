/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.math.Matrix4;

/**
 *
 * @author Portal Valentin
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
    
    public static float[] calculNormal(float[] vertexpos, short[] triangles){
        float[] normals = new float[vertexpos.length];

        for (int i = 0; i < triangles.length; i += 3) {
            Vec3f A = new Vec3f(vertexpos[3 * triangles[i]], vertexpos[3 * triangles[i] + 1], vertexpos[3 * triangles[i] + 2]);
            Vec3f B = new Vec3f(vertexpos[3 * triangles[i + 1]], vertexpos[3 * triangles[i + 1] + 1], vertexpos[3 * triangles[i + 1] + 2]);
            Vec3f C = new Vec3f(vertexpos[3 * triangles[i + 2]], vertexpos[3 * triangles[i + 2] + 1], vertexpos[3 * triangles[i + 2] + 2]);

            Vec3f X = new Vec3f();
            Vec3f Y = new Vec3f();

            X.setSub(B, A);
            Y.setSub(C, A);

            Vec3f vec3f = new Vec3f();
            vec3f.setCrossProduct(X, Y);
            vec3f.normalize();

            for (int j = 0; j < 3; j++) {
                normals[3 * triangles[i + j]] = vec3f.x;
                normals[3 * triangles[i + j] + 1] = vec3f.y;
                normals[3 * triangles[i + j] + 2] = vec3f.z;
            }
        }
        
        return normals;
    }
    
    public static double[] calculNormal(double[] vertexpos, int[] triangles){
        double[] normals = new double[vertexpos.length];

        for (int i = 0; i < triangles.length; i += 3) {
            Vec3d A = new Vec3d(vertexpos[3 * triangles[i]], vertexpos[3 * triangles[i] + 1], vertexpos[3 * triangles[i] + 2]);
            Vec3d B = new Vec3d(vertexpos[3 * triangles[i + 1]], vertexpos[3 * triangles[i + 1] + 1], vertexpos[3 * triangles[i + 1] + 2]);
            Vec3d C = new Vec3d(vertexpos[3 * triangles[i + 2]], vertexpos[3 * triangles[i + 2] + 1], vertexpos[3 * triangles[i + 2] + 2]);

            Vec3d X = new Vec3d();
            Vec3d Y = new Vec3d();

            X.setSub(B, A);
            Y.setSub(C, A);

            Vec3d vec3d = new Vec3d();
            vec3d.setCrossProduct(X, Y);
            vec3d.normalize();

            for (int j = 0; j < 3; j++) {
                normals[3 * triangles[i + j]] = vec3d.x;
                normals[3 * triangles[i + j] + 1] = vec3d.y;
                normals[3 * triangles[i + j] + 2] = vec3d.z;
            }
        }
        
        return normals;
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
