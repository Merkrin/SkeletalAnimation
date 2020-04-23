package ru.hse.jogl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.hse.graphic.*;
import ru.hse.utils.MouseInput;
import ru.hse.utils.OBJLoader;
import ru.hse.utils.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Program {
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Model[] models;

    private static final float CAMERA_POS_STEP = 0.05f;

    public Program() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    // Go from engine
    public void init(Window window) throws Exception {
        renderer.init(window);

        // Create the Mesh
        Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        //Texture texture = new Texture("textures/grassblock.png");
        //mesh.setTexture(texture);
        Model model = new Model(mesh);
        model.setScale(0.5f);
        model.setPosition(0, 0, -2);
        models = new Model[] { model };
    }

    public void input(Window window) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    public void update(MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    public void render(Window window) {
        renderer.render(window, camera, models);
    }

    public void cleanup() {
        renderer.cleanup();
        for (Model model : models) {
            model.getMesh().cleanUp();
        }
    }
}
