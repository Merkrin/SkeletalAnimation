package ru.hse.jogl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ru.hse.graphic.*;
import ru.hse.graphic.HUD.Hud;
import ru.hse.graphic.animation.AnimatedModel;
import ru.hse.utils.MouseInput;
import ru.hse.utils.loaders.OBJLoader;
import ru.hse.utils.Window;
import ru.hse.utils.loaders.md5.*;



import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Program {
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Camera camera;
    private final Vector3f cameraInc;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Renderer renderer;

    private Model[] models;

    //private AnimatedModel monster;
    private Model monster;

    private MD5JointInfo jointInfo;
    private MD5Model md5Meshodel;

    public Program() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    // Go from engine
    public void init(Window window) throws Exception {
        renderer.init(window);

//        Mesh mesh = OBJLoader.loadMesh("/models/teapot.obj");
//        monster = new Model(mesh);

        md5Meshodel = MD5Model.parse("/models/monster.md5mesh");
        Model monster = MD5Loader.process(md5Meshodel, new Vector4f(1, 1, 1, 1));
//        MD5Model md5Meshodel = MD5Model.parse("/models/monster.md5mesh");
//        MD5AnimModel md5AnimModel = MD5AnimModel.parse("/models/monster.md5anim");
//        monster = MD5LoaderWAnim.process(md5Meshodel, md5AnimModel, new Vector4f(1, 1, 1, 1));

        monster.setScale(0.05f);
        monster.setRotation(90, 0, 0);
        monster.setPosition(0, 0, 0);


        Mesh square1 = OBJLoader.loadMesh("/models/kub.obj");

        square1.setIsSquare(true);
        Model model = new Model(square1);
        model.setScale(0.0005f);
        model.setPosition(0,0,0);
        models = new Model[]{monster, model};

//        jointInfo = md5Meshodel.getJointInfo();

//        hud = new Hud("Here is long\nmultiline help\ni hope.");

//        // Create the Mesh
//        Mesh mesh = OBJLoader.loadMesh("/models/teapot.obj");
//        //Texture texture = new Texture("/textures/grassblock.jpg");
//        //mesh.setTexture(texture);
//        Model model = new Model(mesh);
//        model.setScale(0.5f);
//        model.setPosition(0, 0, -2);
//        models = new Model[] { model };
    }

    // TODO: make input options different for different files opened.
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
        if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_P)) {
            //monster.nextFrame();
        }
        if(window.isKeyPressed(GLFW_KEY_R)){
            try {
//                MD5Model md5Meshodel = MD5Model.parse("/models/monster.md5mesh");
                List<MD5JointInfo.MD5JointData> tmp = jointInfo.getJoints();
                tmp.get(12).setPosition(new Vector3f(100,100,-100));
                jointInfo.setJoints(tmp);
                md5Meshodel.setJointInfo(jointInfo);
                Model monster = MD5Loader.process(md5Meshodel, new Vector4f(1, 1, 1, 1));
                monster.setScale(0.05f);
                monster.setRotation(90, 0, 0);
                monster.setPosition(0, 0, 0);
                models[0] = monster;

//                monster = MD5Loader.process(md5Meshodel, new Vector4f(1, 1, 1, 1));
//                Mesh mesh = OBJLoader.loadMesh("/models/teapot.obj");
//                models[0] = new Model(mesh);
//                models = new Model[]{monster};
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void update(MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY,
                    rotVec.y * MOUSE_SENSITIVITY,
                    0);
        }
    }

    public void render(Window window) {
        renderer.render(window, camera, models);
        //renderer.render(window, camera, models, jointInfo.getJoints());
    }

    public void cleanup() {
        renderer.cleanup();
        for (Model model : models) {
            model.getMesh().cleanUp();
        }
    }
}
