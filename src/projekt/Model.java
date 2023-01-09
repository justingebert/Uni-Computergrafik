package projekt;


//3D model stored in Memorey
public class Model {

    private int voaID;
    private int vertexCount;

    public Model(int voaID, int vertexCount) {
        this.voaID = voaID;
        this.vertexCount = vertexCount;
    }

    public int getVoaID() {
        return voaID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
