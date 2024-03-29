package projekt;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

//loads 3D models with aattribbutes into memory
public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();

    public Model loadToVAO(float [] positions,float [] normals, float[] uvs, int [] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,3,normals);
        storeDataInAttributeList(2,2,uvs);
        unbindVAO();
        return new Model(vaoID,indices.length);
    }

    public Model loadToVAOnoI(float [] positions,float [] normals, float[] uvs,float[] color) {
        int vaoID = createVAO();
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,3,normals);
        storeDataInAttributeList(2,2,uvs);
        storeDataInAttributeList(3,3,color);
        unbindVAO();
        return new Model(vaoID,positions.length/3);
    }

    public Model loadToVAOwCOl(float [] positions,float [] normals, float[] uvs,float[] color,int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,3,normals);
        storeDataInAttributeList(2,2,uvs);
        storeDataInAttributeList(3,3,color);
        unbindVAO();
        return new Model(vaoID,indices.length);
    }

    private int createVAO(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    //store data in attribute List
    private void storeDataInAttributeList(int attributeNumber,int dimensions, float [] data){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);

        //GL_STATIC_DRAW becasue data ist static - eg alembic animation not static
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        //put vbo into vao
        glVertexAttribPointer(attributeNumber,dimensions,GL_FLOAT,false,0,0);

        //??
        glEnableVertexAttribArray(vboID);

        //unbind currentt abo
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO(){
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int [] indices){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vboID);
        IntBuffer buffer = storeDataInIntBUffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
    }

    public void cleanUP(){
        for(int vao:vaos){
            glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            glDeleteBuffers(vbo);
        }
    }


    //do i need this?
    private FloatBuffer storeDataInFloatBuffer(float [] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        //flip the buffer from write to read mode
        buffer.flip();
        return buffer;
    }

    private IntBuffer storeDataInIntBUffer(int [] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);

        buffer.flip();
        return buffer;
    }

}
