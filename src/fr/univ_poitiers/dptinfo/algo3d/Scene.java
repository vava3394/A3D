package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Class to represent the scene. It includes all the objects to display, in this case a room
 * @author Philippe Meseure
 * @version 1.0
 */
public class Scene
{
    /**
     * A constant for the size of the wall
     */
    static final float wallsize=3F;
    static final float lenght=7.F;
    /**
     * An angle used to animate the viewer
     */
    float anglex,angley,x=0,z=0,y=0,aux;
    
    boolean OnOff = true;

    Room r;
    
    Sphere s;
    
    LightingShaders shaders;
    
    Texture Texwall,texfloor,texcelling,texbasketball;

    
    /**
     * Constructor : build each wall, the floor and the ceiling as quads
     */
    public Scene(){}


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer)
    {
            GL2 gl=renderer.getGL();
            gl.glDepthFunc(GL2.GL_LESS);
            gl.glEnable(GL2.GL_DEPTH_TEST);
            
            Texwall = renderer.loadTexture(gl, "wall.jpg");
            texbasketball = renderer.loadTexture(gl, "basketball.jpg");
            texfloor = renderer.loadTexture(gl, "ceiling.jpg");
            texcelling = renderer.loadTexture(gl, "tiles2.jpg");
            
            //room
            r = new Room(gl,lenght,wallsize);
            
            //sphere
            s = new Sphere(gl, 10, 10, 1);
            
            
            
            shaders  =renderer.getShaders();
            shaders.setNormalizing(true);
            shaders.setAmbiantLight(new float[]{.2f,.2f,.2f});
            shaders.setLightColor(new float[]{.9f,.9f,.9f});
            shaders.setLightSpecular(new float[]{0.5f,0.5f,0.5f});
            shaders.setMaterialSpecular(new float[]{1f,1f,1f});
            shaders.setLightAttenuation(1.0f, .09f, 0.032f);
            shaders.setMaterialShininess(1000);
            
            MainActivity.log("Initializing graphics");
            // Set the background frame color
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            // Allow back face culling !!
            //gl.glEnable(GL2.GL_CULL_FACE);
            MainActivity.log("Graphics initialized");
    }


    /**
     * Make the scene evoluate, to produce an animation for instance
     * Here, only the viewer rotates
     */
    public void step()
    {
            aux+=0.01F;
    }
    
    private void setMatrixZero(Matrix4 m){
        m.loadIdentity();
        m.rotate(anglex,-0.1F,0.F,0.0F);
        m.rotate(angley,0.0F,-0.1F,0.0F);
        m.translate(x,y-1.6F,z);
    }
   
    private float[] vec4MultMatrix4(float []vec, float[] mat){
        
        return new float[]{
            (vec[0]*mat[0]) + (vec[1]*mat[4]) + (vec[2]*mat[8]) + (vec[3]*mat[12]),
            (vec[0]*mat[1]) + (vec[1]*mat[5]) + (vec[2]*mat[9]) + (vec[3]*mat[13]),
            (vec[0]*mat[2]) + (vec[1]*mat[6]) + (vec[2]*mat[10]) + (vec[3]*mat[14]),
            (vec[0]*mat[3]) + (vec[1]*mat[7]) + (vec[2]*mat[11]) + (vec[3]*mat[15]),
        };
    }

    /**
     * Draw the current simulation state
     * @param renderer Renderer
     */
    public void draw(MyGLRenderer renderer)
    {
        // Get OpenGL context
        GL2 gl=renderer.getGL();
        
        shaders.setViewPos(new float[]{x,y,z});
        shaders.setMaterialColor(MyGLRenderer.white);
        
        Matrix4 modelviewmatrix=new Matrix4();

        MainActivity.log("Starting rendering");

        modelviewmatrix.loadIdentity();
        
        // Get shader to send uniform data
        
        
        // Place viewer in the right position and orientation
        

        /*******************ROOM******************/
        
        modelviewmatrix.rotate(anglex,-0.1F,0.F,0.0F);
        modelviewmatrix.rotate(angley,0.0F,-0.1F,0.0F);     
        
        float[] pos = {x,y,z,1};
        shaders.setLightPosition(vec4MultMatrix4(pos, modelviewmatrix.getMatrix()));
        shaders.setLighting(OnOff);
        
        setMatrixZero(modelviewmatrix);
        
        modelviewmatrix.scale(1, -1, 1);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.drawMirroir(gl, shaders, texcelling, Texwall);
        modelviewmatrix.translate(0, 1.6f, -4);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        s.draw(gl, shaders, Texwall);

        
        
        setMatrixZero(modelviewmatrix);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.draw(gl, shaders, texfloor, texcelling, Texwall);
        modelviewmatrix.translate(0, 1.6f, -4);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        s.draw(gl, shaders, Texwall);

        MainActivity.log("Rendering terminated.");
    }
}
