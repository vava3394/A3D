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
    static final float sizePlayer = 1.6F;
    static final float ecart = 2F;
    private Vec3f posModel = new Vec3f(0,0,-14);
    private int indexModel=0;
    
    float speed = 0.0005f;
    /**
     * An angle used to animate the viewer
     */
    float anglex,angley,x=0,z=0,y=0,aux,angleRotationSphereAux;
    
    boolean OnOff = true;

    Room r;
    
    Sphere s;
    
    
    Model[] allModels = new Model[5];
    
    Piedestal piedestal;
    
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
            s = new Sphere(gl, 100, 100, 1);
            
            
            //Model
            allModels[0] = new Model(gl,Model.nameModel.Lara_Croft,new Vec3f(posModel.x,sizePlayer-0.5f,posModel.z),1.5d);//gl,nom du model,position,scale(si le model est trop grand ou trop petit)
            allModels[4] = new Model(gl,Model.nameModel.Doom,new Vec3f(posModel.x,sizePlayer-0.5f,posModel.z),4000d);
            allModels[3] = new Model(gl, Model.nameModel.Armadillo,new Vec3f(posModel.x,0.1f,posModel.z), 50d);
            allModels[2] = new Model(gl, Model.nameModel.dragon,new Vec3f(posModel.x,0.8f,posModel.z), 0.03d);
            allModels[1] = new Model(gl, Model.nameModel.bateau,new Vec3f(posModel.x,0.8f,posModel.z));
            
            //piedestal
            piedestal = new Piedestal(gl,0.8f,speed,this);//gl,taille du cube,vitesse de déplacement de l'objet mit en exposition, Scene
                       
            shaders  =renderer.getShaders();
            shaders.setNormalizing(true);
            shaders.setAmbiantLight(new float[]{.2f,.2f,.2f});
            
            creationShader(shaders);
            
            MainActivity.log("Initializing graphics");
            // Set the background frame color
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            // Allow back face culling !!
            gl.glEnable(GL2.GL_CULL_FACE);
            MainActivity.log("Graphics initialized");
    }

    
    private void creationShader(LightingShaders shaders){
        shaders.setLightColor(0,MyGLRenderer.white);
        shaders.setLightSpecular(0,new float[]{0.5f,0.5f,0.5f});
        shaders.setMaterialSpecular(0,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(0,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(0,1000);

        shaders.setLightColor(1,MyGLRenderer.green);
        shaders.setLightSpecular(1,new float[]{0.2f,0.5f,0.2f});
        shaders.setMaterialSpecular(1,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(1,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(1,1000);
        
        shaders.setLightColor(2,MyGLRenderer.orange);
        shaders.setLightSpecular(2,new float[]{0.2f,0.5f,0.2f});
        shaders.setMaterialSpecular(2,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(2,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(2,1000);
        
        shaders.setLightColor(3,MyGLRenderer.blue);
        shaders.setLightSpecular(3,new float[]{0.2f,0.5f,0.2f});
        shaders.setMaterialSpecular(3,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(3,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(3,1000);
    }

    /**
     * Make the scene evoluate, to produce an animation for instance
     * Here, only the viewer rotates
     */
    public void step()
    {
            aux+=0.01F;
            angleRotationSphereAux+=0.01F;
    }
    
    

    /**
     * Draw the current simulation state
     * @param renderer Renderer
     */
    public void draw(MyGLRenderer renderer)
    {
            // Get OpenGL context
            GL2 gl=renderer.getGL();
            Matrix4 modelviewmatrix=new Matrix4();

            MainActivity.log("Starting rendering");
            
            shaders.setViewPos(new float[]{x,y,z});
            shaders.setMaterialColor(MyGLRenderer.white);
        
        /*******************Light*******************/
            modelviewmatrix.loadIdentity();
            modelviewmatrix.rotate(anglex,-0.1F,0.F,0.0F);
            modelviewmatrix.rotate(angley,0.0F,-0.1F,0.0F);  
    
            float[] pos1 = {x-4,y,z-14,1};
            float[] pos2 = {x-4,y,z+4,1};
            float[] pos3 = {x,y,z,1};
            float[] pos4 = {x,y,z,1};

            
            shaders.setLightPosition(0,Outils.vec4MultMatrix4(pos1, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(1,Outils.vec4MultMatrix4(pos2, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(2,Outils.vec4MultMatrix4(pos3, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(3,Outils.vec4MultMatrix4(pos4, modelviewmatrix.getMatrix()));
            shaders.setLighting(OnOff);
        
            //flashLight
            //shaders.setLightPosition(0,Outils.vec4MultMatrix4(new float[]{0,0,0,1}, modelviewmatrix.getMatrix()));
            //shaders.setDirectionLight(0,new float[]{0,0,-1,1});
            
            //rotation rigolo
//            modelviewmatrix.rotate(angleRotationSphereAux,0.0F,-0.1F,0.0F);//rotation autour d'elle même    
//            shaders.setDirectionLight(0,Outils.vec4MultMatrix4(new float[]{0,0,-1,1}, modelviewmatrix.getMatrix()));
//            modelviewmatrix.rotate(angleRotationSphereAux*4,0.0F,0.1F,0.0F);//rotation autour d'elle même 
//            shaders.setDirectionLight(1,Outils.vec4MultMatrix4(new float[]{0,0,1,1}, modelviewmatrix.getMatrix()));
//            modelviewmatrix.rotate(angleRotationSphereAux,0.0F,0.1F,0.0F);//rotation autour d'elle même    
//            shaders.setDirectionLight(2,Outils.vec4MultMatrix4(new float[]{0,0,-1,1}, modelviewmatrix.getMatrix()));
//            modelviewmatrix.rotate(angleRotationSphereAux*8,0.0F,-0.1F,0.0F);//rotation autour d'elle même 
//            shaders.setDirectionLight(3,Outils.vec4MultMatrix4(new float[]{0,0,1,1}, modelviewmatrix.getMatrix()));
            
        /*******************DrawMirroir******************/
        
            drawMirroir(renderer);
            //drawMirroirWall(renderer);

        /*******************Draw******************/
        
            /***************Room***************/
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            r.draw(gl, shaders, texcelling, texfloor, Texwall,true); 
            
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            modelviewmatrix.translate(0f,0,-(lenght*ecart));
            modelviewmatrix.rotate((float) Math.PI,0F,0.1F,0.0F);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            r.draw(gl, shaders, texcelling, texfloor, Texwall,false);
            
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            allModels[indexModel].draw(gl, shaders, MyGLRenderer.gray,aux,this);
            
            /***************Piedestal***************/

            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,-4),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(3,0,-2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,0),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(3,0,2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,4),0.5f,s);

            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,-4),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(-3,0,-2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,0),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(-3,0,2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,4),0.5f,s);

            MainActivity.log("Rendering terminated.");
    }
    
    private void drawMirroir(MyGLRenderer renderer){
        Matrix4 modelviewmatrix=new Matrix4();
        GL2 gl=renderer.getGL();
        
        gl.glFrontFace(GL2.GL_CW);
        
        
        
        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.drawMirroir(gl, shaders, texfloor, Texwall);
        
        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        modelviewmatrix.translate(0f,0,-(lenght*ecart));
        modelviewmatrix.rotate((float) Math.PI,0F,0.1F,0.0F);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.drawMirroir(gl, shaders, texfloor, Texwall);
        
        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        allModels[indexModel].drawMirroir(gl, shaders, MyGLRenderer.gray,aux,this);

        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,-4),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(3,0,-2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,0),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(3,0,2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(3,0,4),0.5f,s);
        
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,-4),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(-3,0,-2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,0),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,new Vec3f(-3,0,2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,new Vec3f(-3,0,4),0.5f,s);
        
        gl.glFrontFace(GL2.GL_CCW);
    }
    
    void randomIndex() {
        indexModel = (indexModel+1)%3;
    }
}
