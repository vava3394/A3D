package fr.univ_poitiers.dptinfo.algo3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Matrix4;
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
    
    float speed = 0.0003f;
    /**
     * An angle used to animate the viewer
     */
    float anglex,angley,x=0,z=0,y=0,aux,angleRotationSphereAux;
    
    boolean OnOffLight = true,OnOffFlash = false;

    Room r;
    
    Sphere s;
    
    Model arach,waterMelon,bunny;
    
    Model[] allModels = new Model[5];
    Texture[] textureModels = new Texture[6];
    
    Piedestal piedestal;
    
    LightingShaders shaders;
    
    Texture Texwall,texfloorM,texfloor,texcelling,texball,texArach,texWaterMelon,texBunny;
    
    
    /**
     * Constructor : build each wall, the floor and the ceiling as quads
     */
    public Scene(){}


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer){
            GL2 gl=renderer.getGL();
            gl.glDepthFunc(GL2.GL_LESS);
            gl.glEnable(GL2.GL_DEPTH_TEST);
            
            Texwall = renderer.loadTexture(gl, "wall.jpg");
            texball = renderer.loadTexture(gl, "beachball.jpg");
            texfloor = renderer.loadTexture(gl, "tiles1.jpg");
            texfloorM = renderer.loadTexture(gl, "tiles2.jpg");
            texcelling = renderer.loadTexture(gl, "ceiling.jpg");
            texArach = renderer.loadTexture(gl, "D_2.jpg");
            texWaterMelon = renderer.loadTexture(gl, "WatermelonAlbedo.jpg");
            texBunny = renderer.loadTexture(gl, "bunny.jpg");
            
            //room
            r = new Room(gl,lenght,wallsize);
            
            //sphere
            s = new Sphere(gl,5);

            //Model
            arach = new Model(gl, Model.nameModel.arach,new Vec3f(posModel.x,0,posModel.z),10);
            waterMelon = new Model(gl, Model.nameModel.Watermelon,new Vec3f(posModel.x,0,posModel.z),10);
            bunny = new Model(gl, Model.nameModel.bunny,new Vec3f(posModel.x,0,posModel.z),1);
            allModels[0] = waterMelon;
            allModels[1] = new Model(gl,Model.nameModel.Lara_Croft,new Vec3f(posModel.x,0,posModel.z),1.5d);//gl,nom du model,position,scale(si le model est trop grand ou trop petit)
            allModels[2] = new Model(gl,Model.nameModel.DoomguyDeathHead,new Vec3f(posModel.x,0,posModel.z),500f);
            allModels[3] = new Model(gl, Model.nameModel.dragon,new Vec3f(posModel.x,0,posModel.z), 0.03d);
            allModels[4] = arach;
            
            
            textureModels[4] = texArach;
            textureModels[2] = renderer.loadTexture(gl, "doomhelmet.jpg");;
            textureModels[3] =null;
            textureModels[1] =null;
            textureModels[0] =texWaterMelon;
            
            
            //piedestal
            piedestal = new Piedestal(gl,0.8f,speed,this);//gl,taille du cube,vitesse de déplacement de l'objet mit en exposition, Scene
                       
            shaders  =renderer.getShaders();
            shaders.setNormalizing(true);
            shaders.setAmbiantLight(new float[]{.2f,.2f,.2f});
            shaders.setViewPos(new float[]{0,0,0});
            
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
        shaders.setLightSpecular(1,MyGLRenderer.green);
        shaders.setMaterialSpecular(1,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(1,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(1,1000);
        
        shaders.setLightColor(2,MyGLRenderer.orange);
        shaders.setLightSpecular(2,MyGLRenderer.orange);
        shaders.setMaterialSpecular(2,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(2,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(2,1000);
        
        shaders.setLightColor(3,MyGLRenderer.blue);
        shaders.setLightSpecular(3,MyGLRenderer.blue);
        shaders.setMaterialSpecular(3,new float[]{1f,1f,1f});
        shaders.setLightAttenuation(3,1.0f, .09f, 0.032f);
        shaders.setMaterialShininess(3,1000);

    }

    /**
     * Make the scene evoluate, to produce an animation for instance
     * Here, only the viewer rotates
     */
    public void step(){
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
 
            shaders.setMaterialColor(MyGLRenderer.white);
        
        /*******************Light*******************/
            modelviewmatrix.loadIdentity();
            modelviewmatrix.rotate(anglex,-0.1F,0.F,0.0F);
            modelviewmatrix.rotate(angley,0.0F,-0.1F,0.0F);  
    
            float[] posLight1 = {x+4,y,z-4,1};
            float[] posLight2 = {x-4,y,z+4,1};
            float[] posLight3 = {x-4,y,z-4,1};
            float[] posLight4 = {x+4,y,z+4,1};

            
            shaders.setLightPosition(0,Outils.vec4MultMatrix4(posLight1, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(1,Outils.vec4MultMatrix4(posLight2, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(2,Outils.vec4MultMatrix4(posLight3, modelviewmatrix.getMatrix()));
            shaders.setLightPosition(3,Outils.vec4MultMatrix4(posLight4, modelviewmatrix.getMatrix()));
            
            shaders.setLighting(OnOffLight);
            shaders.setUseFlashLight(OnOffFlash);

            //rotation rigolo pour SpotLightShaders
            modelviewmatrix.rotate(angleRotationSphereAux,0.0F,-0.1F,0.0F);//rotation autour d'elle même    
            shaders.setDirectionLight(0,Outils.vec4MultMatrix4(new float[]{0,0,-1,1}, modelviewmatrix.getMatrix()));
            modelviewmatrix.rotate(angleRotationSphereAux*4,0.0F,0.1F,0.0F);//rotation autour d'elle même 
            shaders.setDirectionLight(1,Outils.vec4MultMatrix4(new float[]{0,0,1,1}, modelviewmatrix.getMatrix()));
            modelviewmatrix.rotate(angleRotationSphereAux,0.0F,0.1F,0.0F);//rotation autour d'elle même    
            shaders.setDirectionLight(2,Outils.vec4MultMatrix4(new float[]{0,0,-1,1}, modelviewmatrix.getMatrix()));
            modelviewmatrix.rotate(angleRotationSphereAux*8,0.0F,-0.1F,0.0F);//rotation autour d'elle même 
            shaders.setDirectionLight(3,Outils.vec4MultMatrix4(new float[]{0,0,1,1}, modelviewmatrix.getMatrix()));
        /*******************DrawMirroir******************/
        
            drawMirroir(renderer);

        /*******************Draw******************/
        
            /***************Room***************/
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            r.draw(gl, shaders, texfloorM, texcelling, Texwall,true); 
            
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            modelviewmatrix.translate(0f,0,-(lenght*ecart));
            modelviewmatrix.rotate((float) Math.PI,0F,0.1F,0.0F);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            r.draw(gl, shaders, texfloor, texcelling, Texwall,false);
            
            /***************Model***************/
            allModels[indexModel].setIsRotate(true);
            allModels[indexModel].setPosition(gl, shaders, null, this);
            allModels[indexModel].draw(gl, shaders, textureModels[indexModel]);
            
            arach.setIsRotate(false);
            arach.setPosition(gl, shaders, new Vec3f(-5.5f, 0, -10), this);
            arach.draw(gl, shaders, texArach);
            
            Outils.setMatrixZero(modelviewmatrix, anglex, angley, x, y, z);
            modelviewmatrix.rotate(-0.3f,0F,0.1F,0.0F);
            modelviewmatrix.translate(-2.5f,0,-6.2f);
            shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
            bunny.draw(gl, shaders, texBunny);
            
            /***************Piedestal***************/

            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,texball,new Vec3f(3,0,-4),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(3,0,-2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(3,0,0),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(3,0,2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(3,0,4),0.5f,s);

            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,-4),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(-3,0,-2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,0),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(-3,0,2),0.5f,s);
            piedestal.draw(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,4),0.5f,s);

            MainActivity.log("Rendering terminated.");
    }
    
    private void drawMirroir(MyGLRenderer renderer){
        Matrix4 modelviewmatrix=new Matrix4();
        GL2 gl=renderer.getGL();
        
        gl.glFrontFace(GL2.GL_CW);

        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.drawMirroir(gl, shaders, texcelling, Texwall);
        
        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        modelviewmatrix.translate(0f,0,-(lenght*ecart));
        modelviewmatrix.rotate((float) Math.PI,0F,0.1F,0.0F);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        r.drawMirroir(gl, shaders, texcelling, Texwall);
        
        allModels[indexModel].setIsRotate(true);
        allModels[indexModel].setPositionMirroir(gl, shaders, null, this);
        allModels[indexModel].draw(gl, shaders, textureModels[indexModel]);

        arach.setIsRotate(false);
        arach.setPositionMirroir(gl, shaders, new Vec3f(-5.5f, 0, -10), this);
        arach.draw(gl, shaders, texArach);

        Outils.setMatrixZeroScale(modelviewmatrix, anglex, angley, x, y, z);
        modelviewmatrix.rotate(-0.3f,0F,0.1F,0.0F);
        modelviewmatrix.translate(-2.5f,0,-6.2f);
        shaders.setModelViewMatrix(modelviewmatrix.getMatrix());
        bunny.draw(gl, shaders, texBunny);

        piedestal.drawMirroir(gl, shaders,modelviewmatrix,MyGLRenderer.randColor,texball,new Vec3f(3,0,-4),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(3,0,-2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(3,0,0),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(3,0,2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(3,0,4),0.5f,s);
        
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,-4),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(-3,0,-2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,0),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.magenta,null,new Vec3f(-3,0,2),0.5f,s);
        piedestal.drawMirroir(gl, shaders,modelviewmatrix, MyGLRenderer.randColor,null,new Vec3f(-3,0,4),0.5f,s);
        
        gl.glFrontFace(GL2.GL_CCW);
    }
    
    void randomIndex() {
        indexModel = (indexModel+1)%allModels.length;
    }
}
