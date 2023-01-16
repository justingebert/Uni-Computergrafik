package projekt;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDrawElements;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

import java.util.Arrays;


public class Projekt extends AbstractOpenGLBase {

    //* ATTRIBUTES
    private ShaderProgram shaderProgram1,shaderProgram2,shaderProgram3;
    private Loader loader = new Loader();
    public Model sphere,donut,box,tetra;
    private int vaoId1,vaoId2,vaoId3,vaoId4;

    private float angle = 0;


    //* FUNCTIONS
    public float[] calcNormals(float[] points) {
        float[] normals = new float[points.length];

        float[] v1 = new float[3];
        float[] v2 = new float[3];
        float[] v3 = new float[3];
        for (int i = 0; i < points.length; i += 9) {
            v1[0] = points[i + 3] - points[i];
            v1[1] = points[i + 4] - points[i + 1];
            v1[2] = points[i + 5] - points[i + 2];

            v2[0] = points[i + 6] - points[i];
            v2[1] = points[i + 7] - points[i + 1];
            v2[2] = points[i + 8] - points[i + 2];

            v3[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
            v3[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
            v3[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);

            normals[i] = v3[0];
            normals[i + 3] = v3[0];
            normals[i + 6] = v3[0];

            normals[i + 1] = v3[1];
            normals[i + 4] = v3[1];
            normals[i + 7] = v3[1];

            normals[i + 2] = v3[2];
            normals[i + 5] = v3[2];
            normals[i + 8] = v3[2];
        }
        return normals;
    }

    public void sendData(int size, int index, float[] arr) {
        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers());
        glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);
    }

    public void sendTexture(Texture texture, ShaderProgram shaderProgram, int textureSlot,int pos, String varName){
        glUseProgram(shaderProgram.getId());
        glActiveTexture(textureSlot);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        int smplr = glGetUniformLocation(shaderProgram1.getId(), varName);
        glUniform1i(smplr, pos);
    }
    public void sendMatrix(ShaderProgram shaderProgram, Matrix4 matrix, String varName){
        glUseProgram(shaderProgram.getId());
        int loc = glGetUniformLocation(shaderProgram.getId(), varName);
        glUniformMatrix4fv(loc, false, matrix.getValuesAsArray());
    }
    public void sendVector(ShaderProgram shaderProgram, float[] vec, String varName){
        glUseProgram(shaderProgram.getId());
        int loc = glGetUniformLocation(shaderProgram.getId(), varName);
        glUniform3fv(loc, vec);
    }
    public void drawObject(Model model,ShaderProgram shaderProgram, int numOfAttributes){
        glBindVertexArray(model.getVoaID());
        glUseProgram(shaderProgram.getId());
        for(int i = 0;i<numOfAttributes;i++){
            glEnableVertexAttribArray(i);
        }
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
    }

    //* PROGRAMM FUNCTIONS
    @Override
    protected void init() {

        shaderProgram1 = new ShaderProgram("box");
        shaderProgram2 = new ShaderProgram("ps");
        shaderProgram3 = new ShaderProgram("t");

        Texture smallRed = new Texture("small.jpg");
        Texture stones = new Texture("3d-textures.jpg");

        Matrix4 projection = new Matrix4(0.3f, 50.0f);

        sphere = OBJLoader.loadOBJModel("sphere_projectedUVS", loader);
        vaoId1 = sphere.getVoaID();

        donut = OBJLoader.loadOBJModel("donoutSmartUV", loader);
        vaoId2 = donut.getVoaID();

        box = OBJLoader.loadOBJModel("box", loader);
        vaoId3 = box.getVoaID();

        //*region Manual DATA
        float[] koord = new float[]{
                -1.0f, +1.0f, -1.0f,    +1.0f, +1.0f, +1.0f,    +1.0f, -1.0f, -1.0f,
                +1.0f, +1.0f, +1.0f,    -1.0f, -1.0f, +1.0f,    +1.0f, -1.0f, -1.0f,
                -1.0f, +1.0f, -1.0f,    -1.0f, -1.0f, +1.0f,    +1.0f, +1.0f, +1.0f,
                -1.0f, +1.0f, -1.0f,    +1.0f, -1.0f, -1.0f,    -1.0f, -1.0f, +1.0f
        };


        float[] koordI = new float[]{
                +1.0f, +1.0f, +1.0f,    +1.0f, -1.0f, -1.0f,    -1.0f, +1.0f, -1.0f,     -1.0f, -1.0f, +1.0f
        };

        float [] normals = calcNormals(koord);

        float[] col = new float[]{
                1.0f, 0.0f, 0.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 1.0f,   1.0f, 1.0f, 0.0f

        };

        float[] uvs = new float[]{
                0.0f, 0.0f,     1.0f, 0.0f,     0.0f, 1.0f,
                0.0f, 0.0f,     1.0f, 0.0f,     0.0f, 1.0f,
                0.0f, 0.0f,     1.0f, 0.0f,     0.0f, 1.0f,
                0.0f, 0.0f,     1.0f, 0.0f,     0.0f, 1.0f
        };

        int[] indices = new int[]{
                3, 1, 2,
                1, 4, 2,
                3, 4, 1,
                3, 2, 4
        };

        int[] iC = Arrays.stream(indices).map(n -> n-1).toArray();
        //endregion
        tetra = loader.loadToVAOnoI(koord,normals,uvs,col);
        vaoId4 = tetra.getVoaID();

        float[] lightPos = new float[]{
                2.0f, 1.0f, 5.2f
        };

        sendTexture(stones, shaderProgram1,GL_TEXTURE0,0,"smplr");
        sendMatrix(shaderProgram1,projection,"projectionMatrix");
        sendVector(shaderProgram1,lightPos,"lightPosition");

        sendTexture(smallRed,shaderProgram2,GL_TEXTURE1,1,"smplr");
        sendMatrix(shaderProgram2,projection,"projectionMatrix");
        sendVector(shaderProgram2,lightPos,"lightPosition");

        glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
        glEnable(GL_CULL_FACE); // backface culling aktivieren
    }

    @Override
    public void update() {

        //TODO if rotX and then rotY => not correct

        angle += 0.01f;

        Matrix4 transform1 = new Matrix4();
        transform1.rotateY(angle);
        transform1.translate(0.0f, 0.0f, -4.0f);

        Matrix4 transform2 = new Matrix4(transform1);
        transform2.translate(0.0f, 0.0f, 4.0f);
        transform2.scale(1.3f);
        transform2.translate(0.0f, 0.0f, -4.0f);

        sendMatrix(shaderProgram1,transform1,"transformationsMatrix");
        sendMatrix(shaderProgram2,transform2,"transformationsMatrix");
    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClear(GL_COLOR_BUFFER_BIT);

        drawObject(sphere,shaderProgram1,3);
        drawObject(donut,shaderProgram2,3);
        drawObject(box,shaderProgram1,3);

        //* DRAW MANUALY CREATED OBJECT WITH ARRAYS BECASUE ITS DOESNT HAVE INDICES
      /*  glBindVertexArray(vaoId4);
        glUseProgram(shaderProgram1.getId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);*/
        //glEnableVertexAttribArray(3);
        //glDrawArrays(GL_TRIANGLES,0,box.getVertexCount());
    }

    @Override
    public void destroy() {
    }

    //* MAIN
    public static void main(String[] args) {
        new Projekt().start("CG Projekt", 700, 700);
    }
}
