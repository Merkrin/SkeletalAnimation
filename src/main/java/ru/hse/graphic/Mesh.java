package ru.hse.graphic;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL30.*;

/*
 * VAO - Vertex Array Object
 * VBO - Vertex Buffer Object
 */

public class Mesh {
    public static final int MAX_WEIGHTS = 4;

    private final int vaoId;
    private final List<Integer> vboIdList;

    private boolean isSquare = false;
    private boolean isActive = false;

    private final int vertexCount;

    public Mesh(float[] positions,
                float[] normals,
                int[] indices) {
        this(positions, normals, indices,
                Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3
                ),
                Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3
                ));
    }

    public Mesh(float[] positions,
                float[] normals, int[] indices,
                int[] jointIndices, float[] weights) {
        FloatBuffer posBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        FloatBuffer weightsBuffer = null;
        IntBuffer jointIndicesBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            if (vecNormalsBuffer.capacity() > 0) {
                vecNormalsBuffer.put(normals).flip();
            } else {
                // Create empty structure
                vecNormalsBuffer = MemoryUtil.memAllocFloat(positions.length);
            }
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Weights
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            weightsBuffer = MemoryUtil.memAllocFloat(weights.length);
            weightsBuffer.put(weights).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, weightsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);

            // Joint indices
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            jointIndicesBuffer = MemoryUtil.memAllocInt(jointIndices.length);
            jointIndicesBuffer.put(jointIndices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, jointIndicesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(4);
            glVertexAttribPointer(4, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (weightsBuffer != null) {
                MemoryUtil.memFree(weightsBuffer);
            }
            if (jointIndicesBuffer != null) {
                MemoryUtil.memFree(jointIndicesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private int getVaoId() {
        return vaoId;
    }

    private int getVertexCount() {
        return vertexCount;
    }

    private void initRender() {
        // Draw the mesh
        glBindVertexArray(getVaoId());
    }

    private void endRender() {
        // Restore state
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void render() {
        initRender();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        endRender();
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    private static float[] createEmptyFloatArray(int length) {
        float[] result = new float[length];

        Arrays.fill(result, (float) 0);

        return result;
    }

    private static int[] createEmptyIntArray(int length) {
        int[] result = new int[length];

        Arrays.fill(result, 0);

        return result;
    }

    Vector3f getColour(){
        if(isSquare && !isActive)
            return new Vector3f(1f, 1f, 1f);
        else if(isSquare)
            return new Vector3f(1f, 0f, 0f);
        else
            return new Vector3f(0.196f, 0.804f, 0.196f);
    }

    public void setIsSquare(boolean isSquare){
        this.isSquare = isSquare;
    }

    public void swapActive(){
        isActive = !isActive;
    }
}
