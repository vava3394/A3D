/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;

/**
 *
 * @author vava3
 */
public class Shadow {
    
    int depthMapFBO;
    int depthMap;
    
    public Shadow(GL2 gl){
        int[] buffersFBO = new int[1];
        int[] buffers = new int[1];
        
        gl.glGenFramebuffers(1,buffersFBO, 0);
        gl.glGenTextures(1, buffers,0);
        
        depthMapFBO = buffersFBO[0];
        depthMap = buffers[0];
        
        int SHADOW_WIDTH = 1024;
        int SHADOW_HEIGHT = 1024;

        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, depthMap);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, 
                     SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, null);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        
        
    }
}
