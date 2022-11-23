package a2;

import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe2 extends AbstractOpenGLBase {

	public static void main(String[] args) {
		new Aufgabe2().start("CG Aufgabe 2", 700, 700);
	}

	@Override
	protected void init() {
		// folgende Zeile läd automatisch "aufgabe2_v.glsl" (vertex) und "aufgabe2_f.glsl" (fragment)
		ShaderProgram shaderProgram = new ShaderProgram("aufgabe2");
		glUseProgram(shaderProgram.getId());

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		float [] koord = new float[]{
			0.0f,0.0f,
			1.0f,1.0f,
			0.0f,1.0f
		};

		float [] col = new float[]{
				1,0,0,
				0,1,0,
				0,0,1
		};

		int vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		sendData(2,0,koord);

		sendData(3,1,col);
	}

	public void sendData(int size,int index, float [] arr){
		glBindBuffer(GL_ARRAY_BUFFER,glGenBuffers());
		glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
		glVertexAttribPointer(index,size,GL_FLOAT,false,0,0);
		glEnableVertexAttribArray(index);
	}

	@Override
	public void update() {
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT); // Zeichenfläche leeren
		glDrawArrays(GL_TRIANGLES,0,3);
		// hier vorher erzeugte VAOs zeichnen
	}

	@Override
	public void destroy() {
	}
}
