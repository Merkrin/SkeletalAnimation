package ru.hse.graphic;

import org.joml.Vector3f;

public class Model {
    private Mesh[] meshes;

    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public Model() {
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Model(Mesh mesh){
        this();
        meshes = new Mesh[]{mesh};
    }

    public Model(Mesh[] meshes){
        this();
        this.meshes = meshes;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public Mesh[] getMeshes() {
        return meshes;
    }

    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public void setMesh(Mesh mesh) {
        if (this.meshes != null) {
            for (Mesh currMesh : meshes)
                currMesh.cleanUp();
        }

        this.meshes = new Mesh[]{mesh};
    }
}
