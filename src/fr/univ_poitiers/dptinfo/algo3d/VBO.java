package fr.univ_poitiers.dptinfo.algo3d;

import static com.jogamp.common.nio.Buffers.SIZEOF_INT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;


/**
 *
 * @author vava3
 * 
 **/
 
public class VBO {
    private int glbuffer;
    private GL2 gl;
    private int size;
    
    public VBO(GL2 gl, int buffer){
        this.gl = gl;
        this.glbuffer=buffer;
    }

    public int getBuffer(){
        return this.glbuffer;
    }
    
    public int getSize(){
        return this.size;
    }
    
    public void allocateBuffer(short[] tab, int size){
        this.size = size;
        ByteBuffer bytebuf = ByteBuffer.allocateDirect(tab.length * Short.BYTES);
        bytebuf.order(ByteOrder.nativeOrder()); // LITTLE_ENDIAN ou BIG_ENDIAN ?
        ShortBuffer buffer = bytebuf.asShortBuffer();
        buffer.put(tab);
        
        buffer.position(0); // Retour à la position 0 pour l’exploitation ultérieure du buffer
    
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glbuffer); // Activation du buffer alloué
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tab.length * Short.BYTES,
        buffer, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }
    
    public void allocateBuffer(double[] tab,int size){
        this.size = size;
        ByteBuffer bytebuf = ByteBuffer.allocateDirect(tab.length * Double.BYTES);
        bytebuf.order(ByteOrder.nativeOrder()); // LITTLE_ENDIAN ou BIG_ENDIAN ?
        DoubleBuffer buffer = bytebuf.asDoubleBuffer();
        buffer.put(tab);
        
        buffer.position(0); // Retour à la position 0 pour l’exploitation ultérieure du buffer
    
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glbuffer); // Activation du buffer alloué
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tab.length * Double.BYTES,
        buffer, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }
    
    public void allocateBuffer(float[] tab,int size){
        this.size = size;
        ByteBuffer bytebuf = ByteBuffer.allocateDirect(tab.length * Float.BYTES);
        bytebuf.order(ByteOrder.nativeOrder()); // LITTLE_ENDIAN ou BIG_ENDIAN ?
        FloatBuffer buffer = bytebuf.asFloatBuffer();
        buffer.put(tab);
        
        buffer.position(0); // Retour à la position 0 pour l’exploitation ultérieure du buffer
    
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glbuffer); // Activation du buffer alloué
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tab.length * Float.BYTES,
        buffer, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }
    
    public void allocateBuffer(int[] tab,int size){
        this.size = size;
        ByteBuffer bytebuf = ByteBuffer.allocateDirect(tab.length * Integer.BYTES);
        bytebuf.order(ByteOrder.nativeOrder()); // LITTLE_ENDIAN ou BIG_ENDIAN ?
        IntBuffer buffer = bytebuf.asIntBuffer();
        buffer.put(tab);
        
        buffer.position(0); // Retour à la position 0 pour l’exploitation ultérieure du buffer
    
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glbuffer); // Activation du buffer alloué
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tab.length * Integer.BYTES,
        buffer, GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }
}

