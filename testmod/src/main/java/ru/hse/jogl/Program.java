package ru.hse.jogl;

import org.joml.Vector3f;
import ru.hse.graphic.Mesh;
import ru.hse.graphic.Model;
import ru.hse.graphic.Renderer;
import ru.hse.utils.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Program {
    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private final Renderer renderer;

    private Mesh mesh;

    private Model[] models;

    public Program() {
        renderer = new Renderer();
    }

    // Go from engine
    public void init(Window window) throws Exception {
        renderer.init(window);

        // Create the Mesh
        float[] positions = new float[]{
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
        };
        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        Mesh mesh = new Mesh(positions, colours, indices);
        Model model = new Model(mesh);
        model.setPosition(0, 0, -2);
        models = new Model[] { model };
//        mesh = new Mesh(positions, colours, indices);
    }

    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    public void update(float interval) {
        for (Model model : models) {
            // Update position
            Vector3f itemPos = model.getPosition();
            float posx = itemPos.x + displxInc * 0.01f;
            float posy = itemPos.y + displyInc * 0.01f;
            float posz = itemPos.z + displzInc * 0.01f;
            model.setPosition(posx, posy, posz);

            // Update scale
            float scale = model.getScale();
            scale += scaleInc * 0.05f;
            if ( scale < 0 ) {
                scale = 0;
            }
            model.setScale(scale);

            // Update rotation angle
            float rotation = model.getRotation().z + 1.5f;
            if ( rotation > 360 ) {
                rotation = 0;
            }
            model.setRotation(0, 0, rotation);
        }
    }

    public void render(Window window) {
        renderer.render(window, models);
    }

    public void cleanup() {
        renderer.cleanup();
        for (Model model : models) {
            model.getMesh().cleanUp();
        }
    }
}
