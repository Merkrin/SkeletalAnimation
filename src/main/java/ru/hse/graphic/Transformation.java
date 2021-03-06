package ru.hse.graphic;

import org.joml.Matrix4f;
import org.joml.Vector3f;

class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    Transformation() {
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    final Matrix4f getProjectionMatrix(float fov,
                                       float width,
                                       float height,
                                       float zNear,
                                       float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    Matrix4f getModelViewMatrix(Model model, Matrix4f viewMatrix) {
        Vector3f rotation = model.getRotation();

        modelViewMatrix.set(viewMatrix).translate(model.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(model.getScale());

        return modelViewMatrix;
    }

    Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();

        // Rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));

        // Translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        return viewMatrix;
    }
}
