/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

import static fr.univ_poitiers.dptinfo.algo3d.BasicShaders.initializeShadersFromResources;

/**
 *
 * @author vava3
 */
public class MyLightingShaders extends LightingShaders{
        /**
	 * Constructor : compile shaders and group them in a GLES program,
	 * and set the various GLSL variables
	 * @param renderer
	 */
	public MyLightingShaders(final MyGLRenderer renderer)
	{
            super(renderer);
            //on set la flashLight au départ mais elle peut être modfier plustard (c'est juste pour être sur que les paramètre de la flash sont réglés)
            this.setFlashLightMaterial(
                new float[]{1f,1f,1f},
                1000,
                MyGLRenderer.white,
                MyGLRenderer.white,
                1.0f,
                .09f,
                0.032f,
                new float[]{0,0,0,1}
            );

            this.setCutOffFlashLight((float) Math.cos(Math.toRadians(12.5f)));
            this.setOuterCutOffFlashLight((float) Math.cos(Math.toRadians(17.5f)));
            this.setDirectionFlashLight(new float[]{0,0,-1,1});
	}
	/**
	 * Method to create shaders.
	 * @return program id created after compiling and linking shader programs
	 */
	@Override
	public int createProgram()
	{
		return initializeShadersFromResources(gl,"myShaders_vert.glsl","myShaders_frag.glsl");
	}
    
}
