/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_poitiers.dptinfo.algo3d;

/**
 *
 * @author vava3
 */
public class FlashLightShaders extends LightingShaders{
    /**
	 * Constructor : compile shaders and group them in a GLES program,
	 * and set the various GLSL variables
	 * @param renderer
	 */
	public FlashLightShaders(final MyGLRenderer renderer)
	{
		super(renderer);
                //flashLight
                this.setCutOffFlashLight((float) Math.cos(Math.toRadians(12.5f)));
                this.setOuterCutOffFlashLight((float) Math.cos(Math.toRadians(17.5f)));
	}
	/**
	 * Method to create shaders.
	 * @return program id created after compiling and linking shader programs
	 */
	@Override
	public int createProgram()
	{
		return initializeShadersFromResources(gl,"flashlight_vert.glsl","flashlight_frag.glsl");
	}
}
