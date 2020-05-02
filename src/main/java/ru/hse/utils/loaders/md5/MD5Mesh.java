package ru.hse.utils.loaders.md5;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MD5Mesh {
    private static final Pattern PATTERN_SHADER =
            Pattern.compile("\\s*shader\\s*\\\"([^\\\"]+)\\\"");

    private static final Pattern PATTERN_VERTEX =
            Pattern.compile("\\s*vert\\s*(\\d+)\\s*\\(\\s*("
                    + MD5Utils.FLOAT_REGEXP + ")\\s*("
                    + MD5Utils.FLOAT_REGEXP
                    + ")\\s*\\)\\s*(\\d+)\\s*(\\d+)");

    private static final Pattern PATTERN_TRI =
            Pattern.compile("\\s*tri\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)");

    private static final Pattern PATTERN_WEIGHT =
            Pattern.compile("\\s*weight\\s*(\\d+)\\s*(\\d+)\\s*" +
                    "(" + MD5Utils.FLOAT_REGEXP + ")\\s*" +
                    MD5Utils.VECTOR3_REGEXP);

    private String texture;

    private List<MD5Vertex> vertices;

    private List<MD5Triangle> triangles;

    private List<MD5Weight> weights;

    private MD5Mesh() {
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("mesh {" + System.lineSeparator());
        str.append("\tshader \"").append(texture).append("\"").append(System.lineSeparator());

        str.append("\n\tnumverts " + vertices.size()).append(System.lineSeparator());
        for (MD5Vertex vertex : vertices) {
            str.append(vertex).append(System.lineSeparator());
        }

        str.append("\n\tnumtris " + triangles.size()).append(System.lineSeparator());
        for (MD5Triangle triangle : triangles) {
            str.append(triangle).append(System.lineSeparator());
        }

        str.append("\n\tnumweights " + weights.size()).append(System.lineSeparator());
        for (MD5Weight weight : weights) {
            str.append(weight).append(System.lineSeparator());
        }
        str.append("}").append(System.lineSeparator());

        return str.toString();
    }

    public static MD5Mesh parse(List<String> meshBlock) {
        MD5Mesh mesh = new MD5Mesh();
        List<MD5Vertex> vertices = mesh.getVertices();
        List<MD5Triangle> triangles = mesh.getTriangles();
        List<MD5Weight> weights = mesh.getWeights();

        for (String line : meshBlock) {
            if (line.contains("shader")) {
                Matcher textureMatcher = PATTERN_SHADER.matcher(line);
                if (textureMatcher.matches()) {
                    mesh.setTexture(textureMatcher.group(1));

                }
            } else if (line.contains("vert")) {
                Matcher vertexMatcher = PATTERN_VERTEX.matcher(line);
                if (vertexMatcher.matches()) {
                    MD5Vertex vertex = new MD5Vertex();
                    vertex.setIndex(Integer.parseInt(vertexMatcher.group(1)));
                    float x = Float.parseFloat(vertexMatcher.group(2));
                    float y = Float.parseFloat(vertexMatcher.group(3));
                    vertex.setTextCoords(new Vector2f(x, y));
                    vertex.setStartWeight(Integer.parseInt(vertexMatcher.group(4)));
                    vertex.setWeightCount(Integer.parseInt(vertexMatcher.group(5)));
                    vertices.add(vertex);
                }
            } else if (line.contains("tri")) {
                Matcher triMatcher = PATTERN_TRI.matcher(line);
                if (triMatcher.matches()) {
                    MD5Triangle triangle = new MD5Triangle();
                    triangle.setIndex(Integer.parseInt(triMatcher.group(1)));
                    triangle.setVertex0(Integer.parseInt(triMatcher.group(2)));
                    triangle.setVertex1(Integer.parseInt(triMatcher.group(3)));
                    triangle.setVertex2(Integer.parseInt(triMatcher.group(4)));
                    triangles.add(triangle);
                }
            } else if (line.contains("weight")) {
                Matcher weightMatcher = PATTERN_WEIGHT.matcher(line);
                if (weightMatcher.matches()) {
                    MD5Weight weight = new MD5Weight();
                    weight.setIndex(Integer.parseInt(weightMatcher.group(1)));
                    weight.setJointIndex(Integer.parseInt(weightMatcher.group(2)));
                    weight.setBias(Float.parseFloat(weightMatcher.group(3)));
                    float x = Float.parseFloat(weightMatcher.group(4));
                    float y = Float.parseFloat(weightMatcher.group(5));
                    float z = Float.parseFloat(weightMatcher.group(6));
                    weight.setPosition(new Vector3f(x, y, z));
                    weights.add(weight);
                }
            }
        }
        return mesh;
    }

    public String getTexture() {
        return texture;
    }

    private void setTexture(String texture) {
        this.texture = texture;
    }

    List<MD5Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<MD5Vertex> vertices) {
        this.vertices = vertices;
    }

    List<MD5Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<MD5Triangle> triangles) {
        this.triangles = triangles;
    }

    List<MD5Weight> getWeights() {
        return weights;
    }

    public void setWeights(List<MD5Weight> weights) {
        this.weights = weights;
    }

    public static class MD5Vertex {

        private int index;

        private Vector2f textCoords;

        private int startWeight;
        private int weightCount;

        public int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        Vector2f getTextCoords() {
            return textCoords;
        }

        void setTextCoords(Vector2f textCoords) {
            this.textCoords = textCoords;
        }

        int getStartWeight() {
            return startWeight;
        }

        void setStartWeight(int startWeight) {
            this.startWeight = startWeight;
        }

        int getWeightCount() {
            return weightCount;
        }

        void setWeightCount(int weightCount) {
            this.weightCount = weightCount;
        }

        @Override
        public String toString() {
            return "\tvert " + index + " ( " +
                    textCoords.x + " " +
                    textCoords.y + " ) " +
                    startWeight + " " + weightCount;
        }
    }

    public static class MD5Triangle {

        private int index;
        private int vertex0;
        private int vertex1;
        private int vertex2;

        public int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        int getVertex0() {
            return vertex0;
        }

        void setVertex0(int vertex0) {
            this.vertex0 = vertex0;
        }

        int getVertex1() {
            return vertex1;
        }

        void setVertex1(int vertex1) {
            this.vertex1 = vertex1;
        }

        int getVertex2() {
            return vertex2;
        }

        void setVertex2(int vertex2) {
            this.vertex2 = vertex2;
        }

        @Override
        public String toString() {
            return "\ttri " + index + " " +
                    vertex0
                    + " " +
                    vertex1 + " " +
                    vertex2;
        }
    }

    public static class MD5Weight {
        private int index;
        private int jointIndex;
        private float bias;

        private Vector3f position;

        public int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        int getJointIndex() {
            return jointIndex;
        }

        void setJointIndex(int jointIndex) {
            this.jointIndex = jointIndex;
        }

        float getBias() {
            return bias;
        }

        void setBias(float bias) {
            this.bias = bias;
        }

        public Vector3f getPosition() {
            return position;
        }

        void setPosition(Vector3f position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return "\tweight " + index + " " + jointIndex
                    + " " +
                    bias + " ( " +
                    position.x + " " +
                    position.y + " " +
                    position.z + " )";
        }
    }
}
