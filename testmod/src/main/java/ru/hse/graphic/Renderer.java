package ru.hse.graphic;

import org.joml.Matrix4f;
import ru.hse.graphic.animation.AnimatedFrame;
import ru.hse.graphic.animation.AnimatedModel;
import ru.hse.utils.ShaderProgram;
import ru.hse.utils.Utils;
import ru.hse.utils.Window;
import ru.hse.utils.loaders.md5.MD5JointInfo;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {
    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private ShaderProgram hudShaderProgram;
    private ShaderProgram sceneShaderProgram;

    private final Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    // Go from program
    public void init(Window window) throws Exception {
        setupSceneShader();
        //setupHudShader();
    }

    private void setupSceneShader() throws Exception{
        // Create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.frag"));
        sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
        sceneShaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        sceneShaderProgram.createUniform("jointsMatrix");
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        //shaderProgram.createUniform("texture_sampler");
        // Create uniform for default colour and the flag that controls it
        sceneShaderProgram.createUniform("colour");
        //shaderProgram.createUniform("useColour");
    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hudVertex.frag"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hudFragment.frag"));
        hudShaderProgram.link();

        // Create uniforms for Orthographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
        hudShaderProgram.createUniform("hasTexture");
    }

    public void clear() {
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

    private void renderScene(Window window, Camera camera, Model[] models){
        sceneShaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Render each gameItem
        for (Model model : models) {
            Mesh mesh = model.getMesh();

            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(model, viewMatrix);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            sceneShaderProgram.setUniform("colour", mesh.getColour());

            if(model instanceof AnimatedModel){
                AnimatedModel animatedModel = (AnimatedModel)model;
                AnimatedFrame animatedFrame = animatedModel.getCurrentFrame();
                sceneShaderProgram.setUniform("jointsMatrix", animatedFrame.getJointMatrices());
            }

            if(mesh.getIsSquare())
                mesh.renderSquare();
            else
                mesh.render();
            // Render the mesh for this game item
            //shaderProgram.setUniform("colour", mesh.getColour());
            //shaderProgram.setUniform("useColour",1);
            //mesh.render();
        }

        sceneShaderProgram.unbind();
    }

    public void cleanup() {
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
    }
}
