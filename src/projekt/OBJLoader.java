package projekt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJLoader {

    public static Model loadOBJModel (String fileName, Loader loader){
        FileReader fileReader = null;

       /* File file = new File("./src/res/geo/");
        for(String fileNames : file.list()) System.out.println(fileNames);*/
        try {
            File file = new File("./src/res/geo/"+fileName+".obj");
            //FileInputStream fIs = new FileInputStream(file);
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.err.println("Couldnt load file");
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Float> vertices = new ArrayList<Float>();
        List<Float> uvs = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();
        List<Integer> indices = new ArrayList<Integer>();

        float [] verticesArray = null;
        float [] normalsArray = null;
        float [] uvArray = null;
        int [] indicesArray = null;
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
                    uvArray = new float[vertices.size()*2];
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
                processVertex(vertex1,indices,uvs,normals,uvArray,normalsArray);
                processVertex(vertex2,indices,uvs,normals,uvArray,normalsArray);
                processVertex(vertex3,indices,uvs,normals,uvArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(float vertex:vertices){
            verticesArray[vertexPointer] = vertex;
            vertexPointer++;
        }
        for(int i = 0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray,normalsArray,uvArray,indicesArray);
    }

    private static void processVertex(String [] vertexData,List<Integer> indices,List<Float> uvs,List<Float> normals,float [] uvArray, float [] normalsArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0])-1; //-1 becasue obj starts at 1
        indices.add(currentVertexPointer);
        float u = uvs.get((Integer.parseInt(vertexData[1])-1)*2); //what index? *2 or 3? -1 -2
        float v = uvs.get((Integer.parseInt(vertexData[1])-1)*2+1);
        uvArray[currentVertexPointer*2] = u;
        uvArray[currentVertexPointer*2+1] = v;


        float nx = normals.get((Integer.parseInt(vertexData[2])-1)*3);
        float ny = normals.get((Integer.parseInt(vertexData[2])-1)*3+1);
        float nz = normals.get((Integer.parseInt(vertexData[2])-1)*3)+2;
        normalsArray[currentVertexPointer*3] = nx;
        normalsArray[currentVertexPointer*3+1] = ny;
        normalsArray[currentVertexPointer*3+2] = nz;
    }

}
