package projekt;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

public class Projekt extends AbstractOpenGLBase {

	private ShaderProgram shaderProgramMaterial;
	private ShaderProgram shaderProgramTexture;


	private int vaoId;

	private float angle = 0;

	public float [] normals(float[] points){
		float [] normals = new float[points.length];

		float [] v1 = new float[3];
		float [] v2 = new float[3];
		float [] v3 = new float[3];
		for(int i = 0;i<points.length;i+=9){
			v1[0] = points[i+3]-points[i];
			v1[1] = points[i+4]-points[i+1];
			v1[2] = points[i+5]-points[i+2];

			v2[0] = points[i+6]-points[i];
			v2[1] = points[i+7]-points[i+1];
			v2[2] = points[i+8]-points[i+2];

			v3[0] = (v1[1]*v2[2])-(v1[2]*v2[1]);
			v3[1] = (v1[2]*v2[0])-(v1[0]*v2[2]);
			v3[2] = (v1[0]*v2[1])-(v1[1]*v2[0]);

			normals[i] = v3[0];
			normals[i+3] = v3[0];
			normals[i+6] = v3[0];

			normals[i+1] = v3[1];
			normals[i+4] = v3[1];
			normals[i+7] = v3[1];

			normals[i+2] = v3[2];
			normals[i+5] = v3[2];
			normals[i+8] = v3[2];
		}
		return normals;
	}

	public static void main(String[] args) {
		new Projekt().start("CG Projekt", 700, 700);
	}

	@Override
	protected void init() {
		//TODO add scond one with etxtures
		shaderProgramMaterial = new ShaderProgram("projekt");
		shaderProgramTexture = new ShaderProgram("texture");
		glUseProgram(shaderProgramMaterial.getId());


		Texture t = new Texture("3d-textures.jpg");
		Texture t2 = new Texture("small.jpg");
		//Texture t2 = new Texture();
		glBindTexture(GL_TEXTURE_2D,t.getId()); //textur waehlen
		glBindTexture(GL_TEXTURE_2D,t2.getId());



		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		/*float [] koord = new float[]{
				-0.0f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				-0.3f,0.2f,-0.5f,
		};*/
		//TODO projektionsmatrix init und mit tranecken verrechnen links
		float [] koord = new float[]{
				-1.0f,+1.0f,-1.0f, +1.0f,+1.0f,+1.0f, +1.0f,-1.0f,-1.0f, // C A B
				+1.0f,+1.0f,+1.0f, -1.0f,-1.0f,+1.0f, +1.0f,-1.0f,-1.0f, // A D B
				-1.0f,+1.0f,-1.0f, -1.0f,-1.0f,+1.0f, +1.0f,+1.0f,+1.0f, // C D A
				-1.0f,+1.0f,-1.0f, +1.0f,-1.0f,-1.0f, -1.0f,-1.0f,+1.0f  // C B D
		};

		float [] normals = normals(koord);

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



		float [] lightPos = new float[]{
				2.0f,1.0f,5.2f
		};

		//build array of Arrays and get ID -> VAO
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		//create Arrays for data -> VBO
		sendData(3,0,koord);
		sendData(3,1,col);
		sendData(3,2,normals);
		sendData(2,3,uvs);


		int loc = glGetUniformLocation(shaderProgramMaterial.getId(),"lightPosition");
		//Licht Position an vertex shader ubergeben
		//glUniformMatrix4fv(loc, false, lightPos);

		glUniform3fv(loc, lightPos);


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
		angle += 0.01f;
		Matrix4 transform = new Matrix4();
		transform.translate(-0.2f,0.0f, 0.0f);
		transform.rotateY(angle);
		transform.scale(0.5f);
		//transform.rotateY((float)Math.toRadians(40.0f));


		//TODO ?? //welches shader programm + var name
		int loc = glGetUniformLocation(shaderProgramMaterial.getId(),"transformationsMatrix");
		//transform matrix an vertex shader ubergeben
		glUniformMatrix4fv(loc, false, transform.getValuesAsArray());
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glClear(GL_COLOR_BUFFER_BIT);
		// Matrix an Shader übertragen
		// VAOs zeichnen


		//TODO ??
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES,0,12);
	}

	@Override
	public void destroy() {
	}
}
