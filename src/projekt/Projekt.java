package projekt;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDrawElements;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;


public class Projekt extends AbstractOpenGLBase {

    //* ATTRIBUTES
    private ShaderProgram shaderProgramBOX;
    private ShaderProgram shaderProgramPS;

    private int vaoId;
    private int vaoId2;

    private float angle = 0;

    public Loader loader = new Loader();
    public Loader loader2 = new Loader();

    public Model box;
    public Model platonicSolid;

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

    //* PROGRAMM FUNCTIONS
    @Override
    protected void init() {

        shaderProgramBOX = new ShaderProgram("box");
        shaderProgramPS = new ShaderProgram("ps");

        Texture smallRed = new Texture("small.jpg");
        Texture stones = new Texture("3d-textures.jpg");

        box = OBJLoader.loadOBJModel("boxTriangulatedUV", loader);
        vaoId = box.getVoaID();

        platonicSolid = OBJLoader.loadOBJModel("ps", loader);
        vaoId2 = platonicSolid.getVoaID();


        Matrix4 projection = new Matrix4(0.3f, 5.0f);

        float[] lightPos = new float[]{
                2.0f, 1.0f, 5.2f
        };


        //glBindVertexArray(vaoId);
        glUseProgram(shaderProgramBOX.getId());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, stones.getId());
        int smplrT = glGetUniformLocation(shaderProgramBOX.getId(), "smplr");
        glUniform1i(smplrT, 0);
        int locPM = glGetUniformLocation(shaderProgramBOX.getId(), "projectionMatrix");
        glUniformMatrix4fv(locPM, false, projection.getValuesAsArray());
        int loc = glGetUniformLocation(shaderProgramBOX.getId(), "lightPosition");
        glUniform3fv(loc, lightPos);


        glUseProgram(shaderProgramPS.getId());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, smallRed.getId());
        int smplrP = glGetUniformLocation(shaderProgramPS.getId(), "smplr");
        glUniform1i(smplrP, 1);
        int locPM2 = glGetUniformLocation(shaderProgramPS.getId(), "projectionMatrix");
        glUniformMatrix4fv(locPM2, false, projection.getValuesAsArray());
        //Licht Position an vertex shader ubergeben
        int loc2 = glGetUniformLocation(shaderProgramPS.getId(), "lightPosition");
        glUniform3fv(loc2, lightPos);
	/*
			float [] koord = new float[]{
					-1.0f,+1.0f,-1.0f, +1.0f,+1.0f,+1.0f, +1.0f,-1.0f,-1.0f, // C A B
					+1.0f,+1.0f,+1.0f, -1.0f,-1.0f,+1.0f, +1.0f,-1.0f,-1.0f, // A D B
					-1.0f,+1.0f,-1.0f, -1.0f,-1.0f,+1.0f, +1.0f,+1.0f,+1.0f, // C D A
					-1.0f,+1.0f,-1.0f, +1.0f,-1.0f,-1.0f, -1.0f,-1.0f,+1.0f  // C B D
			};

			//float [] normals = calcNormals(koord);

			float [] col = new float[]{
					1.0f,0.0f,0.0f, 0.0f,1.0f,0.0f, 0.0f,0.0f,1.0f,
					0.0f,1.0f,0.0f, 1.0f,1.0f,0.0f, 0.0f,0.0f,1.0f,
					1.0f,0.0f,0.0f, 1.0f,1.0f,0.0f, 0.0f,1.0f,0.0f,
					1.0f,0.0f,0.0f, 0.0f,0.0f,1.0f, 1.0f,1.0f,0.0f

			};

			float [] uvs = new float[]{
					0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f,
					0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f,
					0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f,
					0.0f,0.0f, 1.0f,0.0f, 0.0f,1.0f
			};

			int [] indices = new int[]{
				3,1,2,
				1,4,2,
				3,4,1,
				3,2,4
			}

	 */

        //build array of Arrays and get ID -> VAO

        //create Arrays for data -> VBO
        //sendData(3,0,koord);
        //sendData(3,1,col);
        //sendData(3,2,normals);
        //sendData(2,3,uvs);


        glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
        glEnable(GL_CULL_FACE); // backface culling aktivieren
    }

    @Override
    public void update() {
        // Transformation durchführen (Matrix anpassen)
        angle += 0.01f;

        Matrix4 transform = new Matrix4();
        transform.rotateZ(angle);
        transform.rotateY(angle);
        transform.translate(-0.5f, 0.0f, -5.0f);
        transform.scale(0.5f);

        Matrix4 transformBox = new Matrix4();
        transformBox.rotateY(angle);
        transformBox.translate(-0.5f, 0.0f, -5.0f);
        transformBox.scale(0.3f);

        glUseProgram(shaderProgramBOX.getId());
        int loc = glGetUniformLocation(shaderProgramBOX.getId(), "transformationsMatrix");
        glUniformMatrix4fv(loc, false, transformBox.getValuesAsArray());

        //glBindVertexArray(vaoId2);
        glUseProgram(shaderProgramPS.getId());
        int loc2 = glGetUniformLocation(shaderProgramPS.getId(), "transformationsMatrix");
        glUniformMatrix4fv(loc2, false, transform.getValuesAsArray());
        //transform matrix an vertex shader ubergeben

        //glUniformMatrix4fv(loc2, false, projection.getValuesAsArray());
    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClear(GL_COLOR_BUFFER_BIT);

		//glDrawArrays(GL_TRIANGLES,0,12);*/

        glBindVertexArray(vaoId2);
        glUseProgram(shaderProgramPS.getId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, platonicSolid.getVertexCount(), GL_UNSIGNED_INT, 0);

        glBindVertexArray(vaoId);
        glUseProgram(shaderProgramBOX.getId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, box.getVertexCount(), GL_UNSIGNED_INT, 0);
    }

    @Override
    public void destroy() {
    }

    //* MAIN
    public static void main(String[] args) {
        new Projekt().start("CG Projekt", 700, 700);
    }
}
