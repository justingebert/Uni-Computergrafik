package projekt;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Projekt extends AbstractOpenGLBase {

	private ShaderProgram shaderProgram;

	private int vaoId;

	public static void main(String[] args) {
		new Projekt().start("CG Projekt", 700, 700);
	}

	@Override
	protected void init() {
		shaderProgram = new ShaderProgram("projekt");
		glUseProgram(shaderProgram.getId());

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		float [] koord = new float[]{
				0.0f,0.0f,0.0f,
				1.0f,1.0f,1.0f,
				0.0f,1.0f,0.5f
		};

		float [] col = new float[]{
				1,0,0,
				0,1,0,
				0,0,1
		};

		//build array of Arrays and get ID -> VAO
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		//create Arrays for data -> VBO
		sendData(3,0,koord);
		sendData(3,1,col);


		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

	public void sendData(int size,int index, float [] arr){
		glBindBuffer(GL_ARRAY_BUFFER,glGenBuffers());
		glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
		glVertexAttribPointer(index,size,GL_FLOAT,false,0,0);
		glEnableVertexAttribArray(index);
	}

	@Override
	public void update() {
		// Transformation durchführen (Matrix anpassen)
		Matrix4 transform = new Matrix4();
		transform.scale(0.5f);
		transform.translate(-0.2f,0.0f, 0.0f);
		transform.rotateZ((float)Math.toRadians(10.0f));


		//TODO ??
		int loc = glGetUniformLocation(shaderProgram.getId(),"transformationsMatrix");

		glUniformMatrix4fv(loc, false, transform.getValuesAsArray());
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Matrix an Shader übertragen
		// VAOs zeichnen


		//TODO ??
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES,0,3);
	}

	@Override
	public void destroy() {
	}
}
