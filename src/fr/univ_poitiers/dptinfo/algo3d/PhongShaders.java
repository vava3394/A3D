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
public class PhongShaders extends LightingShaders{
    /**
	 * Constructor : compile shaders and group them in a GLES program,
	 * and set the various GLSL variables
	 * @param renderer
	 */
	public PhongShaders(final MyGLRenderer renderer)
	{
		super(renderer);
	}
	/**
	 * Method to create shaders.
	 * @return program id created after compiling and linking shader programs
	 */
	@Override
	public int createProgram()
	{
		return initializeShadersFromResources(gl,"phong_vert.glsl","phong_frag.glsl");
	}
}
