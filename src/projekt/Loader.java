package projekt;

import org.lwjgl.BufferUtils;

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

    public RawModel loadToVAO(float [] positions,int [] indices) {
        int vaoID = createVAO();
        bindIndeciesBuffer(indices);
        storeDataInAttributeList(0,positions);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    private int createVAO(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    //store data in attribute List
    private void storeDataInAttributeList(int attributeNumber, float [] data){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        //GL_STATIC_DRAW becasue data ist static - eg alembic animation not static
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);

        //put vbo into vao
        glVertexAttribPointer(attributeNumber,3,GL_FLOAT,false,0,0);

        //??
        glEnableVertexAttribArray(vboID);

        //unbind currentt abo
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO(){
        glBindVertexArray(0);
    }

    private void bindIndeciesBuffer(int [] indices){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);
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
