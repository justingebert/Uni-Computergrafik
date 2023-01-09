package projekt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class OBJLoader {

    public static RawModel loadOBJModel (String fileName, Loader loader){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File("res/geo/"+fileName+".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldnt load file");
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Float> vertices = new ArrayList<Float>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();
        List<Float> indices = new ArrayList<Float>();

        float [] verticesArray = null;
        float [] normalsArray = null;
        float [] textureArray = null;
        int [] indicesArray = null;
        Scanner scanner = new Scanner(fileReader);
        try{
            while (true){
                line = reader.readLine();
                String [] currentLine = line.split(" ");
                //vertex
                if(line.startsWith("v ")){
                    float x = Float.parseFloat(currentLine[1]);
                    float y = Float.parseFloat(currentLine[2]);
                    float z = Float.parseFloat(currentLine[3]);
                    vertices.add(x);
                    vertices.add(y);
                    vertices.add(z);
                } else if (line.startsWith("vt ")) {
                    float u = Float.parseFloat(currentLine[1]);
                    float v = Float.parseFloat(currentLine[2]);
                    uvs.add(u);
                    uvs.add(v);
                }else if (line.startsWith("vn ")) {
                    float nx = Float.parseFloat(currentLine[1]);
                    float ny = Float.parseFloat(currentLine[2]);
                    float nz = Float.parseFloat(currentLine[3]);
                    normals.add(nx);
                    normals.add(ny);
                    normals.add(nz);
                }else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }

            }
            while (line != null) {
                if(!line.startsWith("f ")){
                    line = reader.readLine();
                    continue;
                }
                String [] currentLine = line.split(" ");
                String [] vertex1 = currentLine[1].split("/");
                String [] vertex2 = currentLine[2].split("/");
                String [] vertex3 = currentLine[3].split("/");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void processVertex(String)

}
