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
public interface InterfaceDraw {
    
    public void setPosition(final GL2 gl,final LightingShaders shaders,Vec3f pos,Scene scene);
    
    public void setPositionMirroir(final GL2 gl,final LightingShaders shaders,Vec3f pos,Scene scene);
    
    public void draw(final GL2 gl,final LightingShaders shaders,float[] color);
    
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture,float [] color);
    
    public void draw(final GL2 gl,final LightingShaders shaders,Texture texture);
    
}
