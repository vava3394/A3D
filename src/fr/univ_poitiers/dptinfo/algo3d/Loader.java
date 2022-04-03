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
            ArrayList<Double> vertexposStockage = new ArrayList<>();//stocke les position des sommets
            ArrayList<Double> normalStockage= new ArrayList<>();//stocke les position des normales
            ArrayList<Float> textureStockage = new ArrayList<>();//stocke les position des textures
            ArrayList<Integer> trianglesStockage = new ArrayList<>();//stocke les sommets des triangles
            ArrayList<Integer> normalPosInTriangle = new ArrayList<>();//stocke l'indices des normales
            ArrayList<Integer> texturePosInTriangle = new ArrayList<>();//stocke l'indices des textures
            
            BufferedReader br = new BufferedReader(new InputStreamReader(Loader.class.getResourceAsStream("./model/" + nameFile)));   
            String line;
            while((line = br.readLine()) != null)
            {
                String[] currentLine = line.split(" "); 
                if(line.startsWith("v ")){//si on lit un 'v' cela signifie qu'on va lire les coordonnées d'un vertexpos
                    if(line.startsWith("v  ")){//cas si 'v' est suivi de 2 espaces flemme de changer toutes les lines
                        vertexposStockage.add(Double.parseDouble(currentLine[2])/scale);
                        vertexposStockage.add(Double.parseDouble(currentLine[3])/scale);
                        vertexposStockage.add(Double.parseDouble(currentLine[4])/scale);
                    }else{
                        vertexposStockage.add(Double.parseDouble(currentLine[1])/scale);
                        vertexposStockage.add(Double.parseDouble(currentLine[2])/scale);
                        vertexposStockage.add(Double.parseDouble(currentLine[3])/scale);
                    }
                }
                else if(line.startsWith("vn ")){//si on lit un 'v' cela signifie qu'on va lire les coordonnées d'un vertexpos
                    if(line.startsWith("vn  ")){//cas si 'v' est suivi de 2 espaces flemme de changer toutes les lines
                        normalStockage.add(Double.parseDouble(currentLine[2]));
                        normalStockage.add(Double.parseDouble(currentLine[3]));
                        normalStockage.add(Double.parseDouble(currentLine[4]));
                    }else{
                        normalStockage.add(Double.parseDouble(currentLine[1]));
                        normalStockage.add(Double.parseDouble(currentLine[2]));
                        normalStockage.add(Double.parseDouble(currentLine[3]));
                    }
                }
                else if(line.startsWith("vt ")){//si on lit un 'v' cela signifie qu'on va lire les coordonnées d'un vertexpos
                    if(line.startsWith("vt  ")){//cas si 'v' est suivi de 2 espaces flemme de changer toutes les lines
                        textureStockage.add(Float.parseFloat(currentLine[2]));
                        textureStockage.add(Float.parseFloat(currentLine[3]));
                    }else{
                        textureStockage.add(Float.parseFloat(currentLine[1]));
                        textureStockage.add(Float.parseFloat(currentLine[2]));
                    }
                }
                else if(line.startsWith("f ")){//si on lit un 'f' cela signifie qu'on va lire les coordonnées d'un triangle
                    if(line.startsWith("f  ")){
                        if(line.contains("/")){//si la line possède un '/' cela signifie que le .obj possède des sommet pour la texture et une normale et alors un triangle posède plus position de sommet.
                            //on récupère seulement les premières position de sommet
                            String[] sommet1 = currentLine[2].split("/");
                            String[] sommet2 = currentLine[3].split("/");
                            String[] sommet3 = currentLine[4].split("/");
                            String[] sommet4 = currentLine[4].split("/");
                            trianglesStockage.add(Integer.parseInt(sommet1[0])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            trianglesStockage.add(Integer.parseInt(sommet2[0])-1);
                            trianglesStockage.add(Integer.parseInt(sommet3[0])-1);
                            normalPosInTriangle.add(Integer.parseInt(sommet1[2])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            normalPosInTriangle.add(Integer.parseInt(sommet2[2])-1);
                            normalPosInTriangle.add(Integer.parseInt(sommet3[2])-1);
                            texturePosInTriangle.add(Integer.parseInt(sommet1[1])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            texturePosInTriangle.add(Integer.parseInt(sommet2[1])-1);
                            texturePosInTriangle.add(Integer.parseInt(sommet3[1])-1);
                            //parfois dans les .obj apres un f on peut avoir plus de 3 blocs d'indices
                            if(currentLine.length>4){
                                String[] aux = currentLine[4].split("/");
                                trianglesStockage.add(Integer.parseInt(sommet1[0])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                                trianglesStockage.add(Integer.parseInt(sommet3[0])-1);
                                trianglesStockage.add(Integer.parseInt(aux[0])-1);
                                normalPosInTriangle.add(Integer.parseInt(sommet1[2])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                                normalPosInTriangle.add(Integer.parseInt(sommet3[2])-1);
                                normalPosInTriangle.add(Integer.parseInt(aux[2])-1);
                                texturePosInTriangle.add(Integer.parseInt(sommet1[1])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                                texturePosInTriangle.add(Integer.parseInt(sommet3[1])-1);
                                texturePosInTriangle.add(Integer.parseInt(aux[1])-1);
                            }
                        }else{
                            trianglesStockage.add(Integer.parseInt(currentLine[2])-1);
                            trianglesStockage.add(Integer.parseInt(currentLine[3])-1);
                            trianglesStockage.add(Integer.parseInt(currentLine[4])-1);
                        }
                    }else{
                        if(line.contains("/")){
                            String[] sommet1 = currentLine[1].split("/");
                            String[] sommet2 = currentLine[2].split("/");
                            String[] sommet3 = currentLine[3].split("/");                          
                            trianglesStockage.add(Integer.parseInt(sommet1[0])-1);
                            trianglesStockage.add(Integer.parseInt(sommet2[0])-1);
                            trianglesStockage.add(Integer.parseInt(sommet3[0])-1);
                            normalPosInTriangle.add(Integer.parseInt(sommet1[2])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            normalPosInTriangle.add(Integer.parseInt(sommet2[2])-1);
                            normalPosInTriangle.add(Integer.parseInt(sommet3[2])-1);
                            texturePosInTriangle.add(Integer.parseInt(sommet1[1])-1);//-1 car notre tableau commence a 0 alors que dans un .obj le premier vertexpos commence a 1
                            texturePosInTriangle.add(Integer.parseInt(sommet2[1])-1);
                            texturePosInTriangle.add(Integer.parseInt(sommet3[1])-1);
                            //parfois dans les .obj apres un f on peut avoir plus de 3 blocs d'indices
                            if(currentLine.length>4){
                                String[] aux = currentLine[4].split("/");
                                trianglesStockage.add(Integer.parseInt(sommet1[0])-1);
                                trianglesStockage.add(Integer.parseInt(sommet3[0])-1);
                                trianglesStockage.add(Integer.parseInt(aux[0])-1);
                                normalPosInTriangle.add(Integer.parseInt(sommet1[2])-1);
                                normalPosInTriangle.add(Integer.parseInt(sommet3[2])-1);
                                normalPosInTriangle.add(Integer.parseInt(aux[2])-1);
                                texturePosInTriangle.add(Integer.parseInt(sommet1[1])-1);
                                texturePosInTriangle.add(Integer.parseInt(sommet3[1])-1);
                                texturePosInTriangle.add(Integer.parseInt(aux[1])-1);
                            }
                        }else{
                            trianglesStockage.add(Integer.parseInt(currentLine[1])-1);
                            trianglesStockage.add(Integer.parseInt(currentLine[2])-1);
                            trianglesStockage.add(Integer.parseInt(currentLine[3])-1);
                        }
                    }
                }
            }
            
            //on convertie nos arrayList en un simple tableau
            double[] vertexpos = new double[vertexposStockage.size()];
            for (int i = 0; i < vertexpos.length; i++) {
               vertexpos[i] = vertexposStockage.get(i);
            }

            int[] triangles = new int[trianglesStockage.size()];
            for (int i = 0; i < triangles.length; i++) {
               triangles[i] = trianglesStockage.get(i);
            }
            
            //il y a trois coordonnées de normale pour chaque triangles
            double[] norm = new double[triangles.length*3];
            for (int i = 0; i < triangles.length; i++) {//pour chaque le triangle
                //triangle[i]*3 donne le premier l'indice qui correspond à la première coordonné du vertex il faut alors que la normale soit à la même indice
                int pos = triangles[i]*3;
                int posN = normalPosInTriangle.get(i)*3;//on récupère les indices des normale
                norm[pos]=normalStockage.get(posN);//on récupère la valeur de la normale dans l'array de stockage a l'indice posN
                norm[pos+1]=normalStockage.get(posN+1);
                norm[pos+2]=normalStockage.get(posN+2);
            }
            
            //identique que pour les normales sauf qu'il y que deux coordonnées
            float[] texture = new float[triangles.length*2];
            for (int i = 0; i < triangles.length; i++) {
                int pos = triangles[i]*2;
                int posN = texturePosInTriangle.get(i)*2;
                texture[pos]=textureStockage.get(posN);
                texture[pos+1]=textureStockage.get(posN+1);
            }

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
