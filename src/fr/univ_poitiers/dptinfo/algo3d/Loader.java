package fr.univ_poitiers.dptinfo.algo3d;
import com.jogamp.opengl.GL2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 *
 * @author Portal Valentin
 */
public class Loader{ //chargement d'un fichier .obj
    
    protected final VBO glposbuffer;
    protected final VBO glelementtribuffer;
    protected final VBO glelementNormal;
    protected final VBO gltexturebuffer;
    
    protected Drawable drawable;
    
    public Loader(GL2 gl,String nameFile,double scale){
        
        int[] buffers = new int[4];
        gl.glGenBuffers(4, buffers, 0); // Allocation du buffer

        glposbuffer = new VBO(gl, buffers[0]);
        glelementtribuffer = new VBO(gl, buffers[1]);
        glelementNormal = new VBO(gl, buffers[2]);
        gltexturebuffer = new VBO(gl, buffers[3]);
        
        try{  
            //on utilise des arraylist car on connait pas d'avance le nombre de sommets et le nombre de triangles
            ArrayList<Double> vertexposAux = new ArrayList<>();
            ArrayList<Float> normalAux = new ArrayList<>();
            ArrayList<Integer> trianglesAux = new ArrayList<>();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(Loader.class.getResourceAsStream("./model/" + nameFile)));   
            String line;
            while((line = br.readLine()) != null)
            {
                String[] currentLine = line.split(" "); 
                if(line.startsWith("v ")){//si on lit un 'v' cela signifie qu'on va lire les coordonnées d'un vertexpos
                    if(line.startsWith("v  ")){//cas si 'v' est suivi de 2 espaces flemme de changer toutes les lines
                        vertexposAux.add(Double.parseDouble(currentLine[2])/scale);
                        vertexposAux.add(Double.parseDouble(currentLine[3])/scale);
                        vertexposAux.add(Double.parseDouble(currentLine[4])/scale);
                    }else{
                        vertexposAux.add(Double.parseDouble(currentLine[1])/scale);
                        vertexposAux.add(Double.parseDouble(currentLine[2])/scale);
                        vertexposAux.add(Double.parseDouble(currentLine[3])/scale);
                    }
                }
                else if(line.startsWith("f ")){//si on lit un 'f' cela signifie qu'on va lire les coordonnées d'un triangle
                    if(line.startsWith("f  ")){
                        if(line.contains("/")){//si la line possède un '/' cela signifie que le .obj possède des sommet pour la texture et une normale et alors un triangle posède plus position de sommet.
                            //on récupère seulement les premières position de sommet
                            String[] sommet1 = currentLine[2].split("/");
                            String[] sommet2 = currentLine[3].split("/");
                            String[] sommet3 = currentLine[4].split("/");
                            trianglesAux.add(Integer.parseInt(sommet1[0])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            trianglesAux.add(Integer.parseInt(sommet2[0])-1);
                            trianglesAux.add(Integer.parseInt(sommet3[0])-1);
                        }else{
                            trianglesAux.add(Integer.parseInt(currentLine[2])-1);
                            trianglesAux.add(Integer.parseInt(currentLine[3])-1);
                            trianglesAux.add(Integer.parseInt(currentLine[4])-1);
                        }
                    }else{
                        if(line.contains("/")){
                            String[] sommet1 = currentLine[1].split("/");
                            String[] sommet2 = currentLine[2].split("/");
                            String[] sommet3 = currentLine[3].split("/");
                            trianglesAux.add(Integer.parseInt(sommet1[0])-1);
                            trianglesAux.add(Integer.parseInt(sommet2[0])-1);
                            trianglesAux.add(Integer.parseInt(sommet3[0])-1);
                        }else{
                            trianglesAux.add(Integer.parseInt(currentLine[1])-1);
                            trianglesAux.add(Integer.parseInt(currentLine[2])-1);
                            trianglesAux.add(Integer.parseInt(currentLine[3])-1);
                        }
                    }
                }
            }
            
            //on convertie nos arrayList en un simple tableau
            double[] vertexpos = new double[vertexposAux.size()];
            for (int i = 0; i < vertexpos.length; i++) {
               vertexpos[i] = vertexposAux.get(i);
            }

            int[] triangles = new int[trianglesAux.size()];
            for (int i = 0; i < triangles.length; i++) {
               triangles[i] = trianglesAux.get(i);
            }
            
            double[] norm = Outils.calculNormal(vertexpos, triangles);
            
            
            float[] texture = new float[vertexpos.length];
           
            glposbuffer.allocateBuffer(vertexpos, vertexpos.length);
            
            glelementtribuffer.allocateBuffer(triangles, triangles.length);
              
            glelementNormal.allocateBuffer(norm, norm.length);
            
            gltexturebuffer.allocateBuffer(texture, texture.length);
            
            drawable = new Drawable(
                glelementNormal,
                gltexturebuffer, 
                glposbuffer, 
                glelementtribuffer,
                GL2.GL_DOUBLE,
                GL2.GL_FLOAT,
                GL2.GL_UNSIGNED_INT
            );
        }catch(IOException e){
            e.printStackTrace();
        }   
    }     
}
