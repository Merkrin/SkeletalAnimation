package ru.hse.graphic;

import org.joml.Matrix4f;
import ru.hse.graphic.animation.AnimatedFrame;
import ru.hse.graphic.animation.AnimatedModel;
import ru.hse.utils.ShaderProgram;
import ru.hse.utils.Utils;
import ru.hse.utils.Window;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    // Field of View in Radians
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private ShaderProgram sceneShaderProgram;

    private final Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    // Go from program
    public void init(Window window) throws Exception {
        setupSceneShader();
    }

    private void setupSceneShader() throws Exception {
        // Create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(Utils.
                loadResource("/shaders/vertex.frag"));
        sceneShaderProgram.createFragmentShader(Utils.
                loadResource("/shaders/fragment.frag"));
        sceneShaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        sceneShaderProgram.createUniform("jointsMatrix");
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");

        // Create uniform for default colour
        sceneShaderProgram.createUniform("colour");
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Model[] models) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderScene(window, camera, models);
    }

    private void renderScene(Window window, Camera camera, Model[] models) {
        sceneShaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation
                .getProjectionMatrix(FOV,
                        window.getWidth(),
                        window.getHeight(),
                        Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Render each gameItem
        for (Model model : models) {
            Mesh mesh = model.getMesh();

            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation
                    .getModelViewMatrix(model, viewMatrix);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            sceneShaderProgram.setUniform("colour", mesh.getColour());

            if (model instanceof AnimatedModel) {
                AnimatedModel animatedModel = (AnimatedModel) model;
                AnimatedFrame animatedFrame = animatedModel.getCurrentFrame();
                sceneShaderProgram.setUniform("jointsMatrix",
                        animatedFrame.getJointMatrices());
            }

            mesh.render();
        }

        sceneShaderProgram.unbind();
    }

    public void cleanup() {
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
    }
}
