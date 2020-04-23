package ru.hse.jogl;

import ru.hse.graphic.Mesh;
import ru.hse.graphic.Renderer;
import ru.hse.utils.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Program {
    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    private Mesh mesh;

    public Program() {
        renderer = new Renderer();
    }

    // Go from engine
    public void init(Window window) throws Exception {
        renderer.init(window);

        float[] positions = new float[]{
                -0.5f, 0.5f, -1.5f,
                -0.5f, -0.5f, -1.5f,
                0.5f, -0.5f, -1.5f,
                0.5f, 0.5f, -1.5f,
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
        mesh = new Mesh(positions, colours, indices);
    }

    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    public void cleanup() {
        renderer.cleanup();
        mesh.cleanUp();
    }
}
