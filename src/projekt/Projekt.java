package projekt;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDrawElements;
import static org.lwjgl.glfw.GLFW.*;



import java.util.Arrays;


public class Projekt extends AbstractOpenGLBase {

    //* ATTRIBUTES
    private ShaderProgram shaderProgram1,shaderProgram2,shaderProgram3,shaderProgram4;
    private Loader loader = new Loader();
    public Model sphere,donut,box,tetra;
    private int vaoId1,vaoId2,vaoId3,vaoId4;
    private float angle = 0;
    private float move = 0;
    private int locked = GL_FALSE;
    private float alpha=0.00f,beta=70.0f;


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
        int smplr = glGetUniformLocation(shaderProgram.getId(), varName);
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
    public void drawArrayObject(Model model,ShaderProgram shaderProgram,int numOfAttributes){
        glBindVertexArray(model.getVoaID());
        glUseProgram(shaderProgram.getId());
        for(int i = 0;i<numOfAttributes;i++){
            glEnableVertexAttribArray(i);
        }
        glDrawArrays(GL_TRIANGLES,0,model.getVertexCount());
    }
    public void key_callback(long window, int key, int scancode, int action, int mods){
        if (action != GLFW_PRESS) return;
        switch (key){
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window,true);
                break;
            case GLFW_KEY_LEFT:
                alpha += 0.05f;
                break;
            case GLFW_KEY_RIGHT:
                alpha -= 0.05f;
                break;
            case GLFW_KEY_UP:
                beta -= 5.0f;
                break;
            case GLFW_KEY_DOWN:
                beta += 5.0f;
                break;
        }
    }
    //* PROGRAMM FUNCTIONS
    @Override
    protected void init() {

        long window = getWindow();
        //glfwSetMouseButtonCallback(window,k)
        glfwSetKeyCallback(window,this::key_callback);

        shaderProgram1 = new ShaderProgram("one");
        shaderProgram2 = new ShaderProgram("two");
        shaderProgram3 = new ShaderProgram("three");
        shaderProgram4 = new ShaderProgram("four");



        Texture stones = new Texture("3d-textures.jpg",2,GL_LINEAR,GL_LINEAR_MIPMAP_LINEAR);
        Texture texLowRes = new Texture("checkerBW.jpg",0,GL_NEAREST,GL_NEAREST_MIPMAP_NEAREST);
        Texture texLowResLinear = new Texture("checkerBW.jpg",2,GL_LINEAR,GL_LINEAR_MIPMAP_LINEAR);
        Texture hexa = new Texture("ConcreteBlocksPavingHexagon006_COL_4K.png");
        Texture hexaN = new Texture("ConcreteBlocksPavingHexagon006_NRM_1K.png");


        Matrix4 projection = new Matrix4(0.3f, 50.0f);

        sphere = OBJLoader.loadOBJModel("sphere_dvatuv", loader);
        vaoId1 = sphere.getVoaID();

        donut = OBJLoader.loadOBJModel("donutUV", loader);
        vaoId2 = donut.getVoaID();

        box = OBJLoader.loadOBJModel("box_double_v_at_seam", loader);
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

        sendTexture(hexa, shaderProgram1,GL_TEXTURE0,0,"smplr");
        sendTexture(hexaN, shaderProgram1,GL_TEXTURE1,1,"smplrN");
        sendMatrix(shaderProgram1,projection,"projectionMatrix");
        sendVector(shaderProgram1,lightPos,"lightPosition");

        sendTexture(texLowRes,shaderProgram2,GL_TEXTURE2,2,"smplr");
        sendMatrix(shaderProgram2,projection,"projectionMatrix");
        sendVector(shaderProgram2,lightPos,"lightPosition");

        sendTexture(texLowResLinear,shaderProgram3,GL_TEXTURE3,3,"smplr");
        sendMatrix(shaderProgram3,projection,"projectionMatrix");
        sendVector(shaderProgram3,lightPos,"lightPosition");

        sendTexture(stones,shaderProgram4,GL_TEXTURE4,4,"smplr");
        sendMatrix(shaderProgram4,projection,"projectionMatrix");
        sendVector(shaderProgram4,lightPos,"lightPosition");



        glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
        glEnable(GL_CULL_FACE); // backface culling aktivieren
    }

    @Override
    public void update() {

        //TODO if rotX and then rotY => not correct

        angle += 0.01f;
        move += 0.03f;

        Matrix4 transform1 = new Matrix4();
        transform1.rotateY(angle);
        transform1.translate(0.0f, 0.0f, -3.5f);

        Matrix4 transform2 = new Matrix4();
        transform2.rotateY(alpha*5);
        transform2.scale(1.5f);
        transform2.translate(0.0f, -1.5f, -4.0f);

        Matrix4 transform3 = new Matrix4(transform1);
        transform3.translate(0.0f, 0.0f, 3.5f);
        transform3.rotateY(-angle);
        transform3.translate(0.0f, 0.0f, -3.5f);
        transform3.rotateY(angle);

        transform3.scale(0.7F);
        transform3.translate(-0.0f, 0.5f, -4.0f);
        //transform3.rotateY(angle*2);

        Matrix4 transform4 = new Matrix4();
        transform4.scale(0.5f);
        transform4.rotateZ(angle);
        transform4.translate(0.0f,2.5f,-5.0f);
        transform4.translate(0.0f,0.0f,(float)Math.cos(move)*2);


        sendMatrix(shaderProgram1,transform1,"transformationsMatrix");
        sendMatrix(shaderProgram2,transform2,"transformationsMatrix");
        sendMatrix(shaderProgram3,transform3,"transformationsMatrix");
        sendMatrix(shaderProgram4,transform4,"transformationsMatrix");
    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClear(GL_COLOR_BUFFER_BIT);

        drawObject(sphere,shaderProgram1,3);
        drawObject(donut,shaderProgram2,3);
        drawObject(box,shaderProgram4,3);

        //* DRAW MANUALY CREATED OBJECT WITH ARRAYS BECASUE ITS DOESNT HAVE INDICES
        drawArrayObject(tetra,shaderProgram3,4);
    }

    @Override
    public void destroy() {
    }

    //* MAIN
    public static void main(String[] args) {
        new Projekt().start("CG Projekt", 700, 700);
    }
}
