package projekt;

//Alle Operationen ändern das Matrixobjekt selbst und geben das eigene Matrixobjekt zurück
//Dadurch kann man Aufrufe verketten, z.B.
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4 {

	float [][] mat = new float[4][4];
	public Matrix4() {
		// TODO mit der Identitätsmatrix initialisieren
		for(int x = 0; x < 4;x++){
			this.mat[x][x] = 1;
		}
	}

	public Matrix4 (Matrix4 copy) {
		// TODO neues Objekt mit den Werten von "copy" initialisieren
		this.mat = (float[][]) copy.mat.clone();
		//this.mat = copy.mat;
	}

	public Matrix4(float near, float far) {
		// TODO erzeugt Projektionsmatrix mit Abstand zur nahen Ebene "near" und Abstand zur fernen Ebene "far", ggf. weitere Parameter hinzufügen
		this.mat[0][0] = 1.0F;
		this.mat[1][1] = 1.0F;
		this.mat[2][2] = (-far - near) / (far - near);
		this.mat[2][3] = -2.0F * near * far / (far - near);
		this.mat[3][2] = -1.0F;
		this.mat[3][3] = 0.0F;

	}

	public Matrix4 multiply(Matrix4 other) {
		// TODO hier Matrizenmultiplikation "this = other * this" einfügen
		float [] [] tmp = new float[4][4];
		for(int zeile = 0; zeile<4; zeile++){
			for(int spalte = 0; spalte < 4; spalte++){
				float sum = 0.0F;
				for(int i = 0;i<4;i++){
					sum += other.mat[zeile][i] * this.mat[i][spalte];
				}
				tmp[zeile][spalte] = sum;
			}
		}
		this.mat = tmp;
		return this;
	}

	public Matrix4 translate(float x, float y, float z) {
		// TODO Verschiebung um x,y,z zu this hinzufügen
		Matrix4 tmp = new Matrix4();
		float [] translate = {x,y,z,1.0F};
		for(int zeile = 0;zeile<4;zeile++){
			tmp.mat[zeile][3] = translate[zeile];
		}
		this.multiply(tmp);
		return this;
	}

	public Matrix4 scale(float uniformFactor) {
		// TODO gleichmäßige Skalierung um Faktor "uniformFactor" zu this hinzufügen
		Matrix4 tmp = new Matrix4();
		for(int dia = 0;dia<3;dia++){
			tmp.mat[dia][dia] = uniformFactor;
		}
		this.multiply(tmp);
		return this;
	}

	public Matrix4 scale(float sx, float sy, float sz) {
		// TODO ungleichförmige Skalierung zu this hinzufügen
		Matrix4 tmp = new Matrix4();
		float[] scale = {sx,sy,sz,1.0F};
		for(int dia = 0;dia<4;dia++){
			tmp.mat[dia][dia] = scale[dia];
		}
		this.multiply(tmp);
		return this;
	}

	public Matrix4 rotateX(float angle) {
		// TODO Rotation um X-Achse zu this hinzufügen
		Matrix4 rotX = new Matrix4();
		float[] rotate = {(float) Math.cos(angle), (float) Math.sin(angle)};
		rotX.mat[1][1] = rotate[0];
		rotX.mat[2][2] = rotate[0];
		rotX.mat[1][2] = -1*rotate[1];
		rotX.mat[2][1] = rotate[1];
		this.multiply(rotX);
		return this;
	}

	public Matrix4 rotateY(float angle) {
		// TODO Rotation um Y-Achse zu this hinzufügen
		Matrix4 rotY = new Matrix4();
		float[] rotate = {(float) Math.cos(angle), (float) ((float)Math.sin(angle))};
		rotY.mat[0][0] = rotate[0];
		rotY.mat[2][2] = rotate[0];
		rotY.mat[2][0] = rotate[1];
		rotY.mat[0][2] = -1*rotate[1];
		this.multiply(rotY);
		return this;
	}

	public Matrix4 rotateZ(float angle) {
		// TODO Rotation um Z-Achse zu this hinzufügen
		Matrix4 rotZ = new Matrix4();
		float[] rotate = {(float) Math.cos(angle), (float) ((float)Math.sin(angle))};
		rotZ.mat[0][0] = rotate[0];
		rotZ.mat[0][1] = -1*rotate[1];
		rotZ.mat[1][0] = rotate[1];
		rotZ.mat[1][1] = rotate[0];
		this.multiply(rotZ);
		return this;
	}

	public float[] getValuesAsArray() {
		// TODO hier Werte in einem Float-Array mit 16 Elementen (spaltenweise gefüllt) herausgeben
		float[] ret = new float[16];
		int i = 0;
			for(int spalte = 0;spalte <= 3; spalte++){
				for(int zeile = 0;zeile <= 3;zeile++){
					ret[i] = this.mat[zeile][spalte];
				i++;
			}
		}
		return ret;
	}
}
