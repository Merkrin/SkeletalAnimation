package ru.hse.utils.loaders.md5;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;

import ru.hse.graphic.*;
import ru.hse.utils.*;

public class MD5Loader {
    public static Model process(MD5Model md5Model){
        List<Mesh> list = new ArrayList<>();
        for (MD5Mesh md5Mesh : md5Model.getMeshes()) {
            Mesh mesh = generateMesh(md5Model, md5Mesh);
            list.add(mesh);
        }
        Mesh[] meshes = new Mesh[list.size()];
        meshes = list.toArray(meshes);

        return new Model(meshes);
    }

    private static Mesh generateMesh(MD5Model md5Model, MD5Mesh md5Mesh) {
        List<VertexInfo> vertexInfoList = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        List<MD5Mesh.MD5Vertex> vertices = md5Mesh.getVertices();
        List<MD5Mesh.MD5Weight> weights = md5Mesh.getWeights();
        List<MD5JointInfo.MD5JointData> joints = md5Model.getJointInfo().getJoints();

        for (MD5Mesh.MD5Vertex vertex : vertices) {
            Vector3f vertexPos = new Vector3f();

            int startWeight = vertex.getStartWeight();
            int numWeights = vertex.getWeightCount();

            for (int i = startWeight; i < startWeight + numWeights; i++) {
                MD5Mesh.MD5Weight weight = weights.get(i);
                MD5JointInfo.MD5JointData joint = joints.get(weight.getJointIndex());
                Vector3f rotatedPos = new Vector3f(weight.getPosition()).rotate(joint.getOrientation());
                Vector3f acumPos = new Vector3f(joint.getPosition()).add(rotatedPos);
                acumPos.mul(weight.getBias());
                vertexPos.add(acumPos);
            }

            vertexInfoList.add(new VertexInfo(vertexPos));
        }

        for (MD5Mesh.MD5Triangle tri : md5Mesh.getTriangles()) {
            indices.add(tri.getVertex0());
            indices.add(tri.getVertex1());
            indices.add(tri.getVertex2());

            // Normals
            VertexInfo v0 = vertexInfoList.get(tri.getVertex0());
            VertexInfo v1 = vertexInfoList.get(tri.getVertex1());
            VertexInfo v2 = vertexInfoList.get(tri.getVertex2());
            Vector3f pos0 = v0.position;
            Vector3f pos1 = v1.position;
            Vector3f pos2 = v2.position;

            Vector3f normal = (new Vector3f(pos2).sub(pos0)).cross(new Vector3f(pos1).sub(pos0));

            v0.normal.add(normal);
            v1.normal.add(normal);
            v2.normal.add(normal);
        }

        // Once the contributions have been added, normalize the result
        for(VertexInfo v : vertexInfoList) {
            v.normal.normalize();
        }

        float[] positionsArr = VertexInfo.toPositionsArr(vertexInfoList);
        float[] normalsArr = VertexInfo.toNormalArr(vertexInfoList);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh(positionsArr, normalsArr, indicesArr);
    }

    private static class VertexInfo {

        final Vector3f position;
        final Vector3f normal;

        VertexInfo(Vector3f position) {
            this.position = position;
            normal = new Vector3f();
        }

        static float[] toPositionsArr(List<VertexInfo> list) {
            int length = list != null ? list.size() * 3 : 0;
            float[] result = new float[length];
            int i = 0;

            for (VertexInfo v : list) {
                result[i] = v.position.x;
                result[i + 1] = v.position.y;
                result[i + 2] = v.position.z;
                i += 3;
            }
            return result;
        }

        static float[] toNormalArr(List<VertexInfo> list) {
            int length = list != null ? list.size() * 3 : 0;
            float[] result = new float[length];
            int i = 0;

            for (VertexInfo v : list) {
                result[i] = v.normal.x;
                result[i + 1] = v.normal.y;
                result[i + 2] = v.normal.z;
                i += 3;
            }
            return result;
        }
    }
}
