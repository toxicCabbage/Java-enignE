package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {

    public static RawModel loadObjModel(String fileName, Loader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.out.println("faild to Load file");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArrays = null;
        float[] normalsArrays = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArrays = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArrays);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArrays);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArrays);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArrays = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArrays[vertexPointer++] = vertex.x;
            verticesArrays[vertexPointer++] = vertex.y;
            verticesArrays[vertexPointer++] = vertex.z;

        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        System.out.println("FINE LOADED");
        System.out.println(verticesArrays.length + " " + textureArray.length + " " + indicesArray.length);
        return loader.loadToVAO(verticesArrays, textureArray, indicesArray);

    }

    public static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> texture,
            List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currentVectexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVectexPointer);

        Vector2f currentTex = texture.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVectexPointer * 2] = currentTex.x;
        textureArray[currentVectexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVectexPointer * 3] = currentNorm.x;
        normalsArray[currentVectexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVectexPointer * 3 + 2] = currentNorm.z;
    }
}
