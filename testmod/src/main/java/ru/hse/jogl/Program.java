package ru.hse.jogl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ru.hse.graphic.*;
import ru.hse.graphic.animation.AnimatedModel;
import ru.hse.utils.MouseInput;
import ru.hse.utils.loaders.OBJLoader;
import ru.hse.utils.Window;
import ru.hse.utils.loaders.md5.*;


import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Program {
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Camera camera;
    private final Vector3f cameraInc;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Renderer renderer;

    private Model[] models;

    private AnimatedModel animatedModel;
    private Model mainModel;

    private MD5JointInfo jointInfo;
    private int currentActiveJoint = 0;
    private int jointsAmount;

    private MD5Model md5MeshModel;

    private final String[] filePaths;
    /*
     * 0 - .obj
     * 1 - .md5mesh
     * 2 - .md5anim
     */
    private final byte fileType;

    public Program(String[] filePaths, byte fileType) {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();

        this.filePaths = filePaths;
        this.fileType = fileType;
    }

    // Go from engine
    public void init(Window window) throws Exception {
        renderer.init(window);

        if (fileType == 0)
            initObj();
        else if (fileType == 1)
            initMd5Mesh();
        else if (fileType == 2)
            initMd5Anim();
    }

    private void initObj() throws Exception {
        Mesh mesh = OBJLoader.loadMesh(filePaths[0], true);
        mainModel = new Model(mesh);
        models = new Model[]{mainModel};
    }

    private void initMd5Mesh() throws Exception {
        md5MeshModel = MD5Model.parse(filePaths[0]);
        mainModel = MD5Loader.process(md5MeshModel, new Vector4f(1, 1, 1, 1));

        jointInfo = md5MeshModel.getJointInfo();
        jointsAmount = jointInfo.getJoints().size();
        models = new Model[jointsAmount + 1];
        models[0] = mainModel;

        MD5JointInfo.MD5JointData joint;
        for (int i = 0; i < jointInfo.getJoints().size(); i++) {
            Mesh mesh = OBJLoader.loadMesh("/models/kub.obj", false);
            mesh.setIsSquare(true);
            if (i == 0)
                mesh.swapActive();
            Model model = new Model(mesh);

            joint = jointInfo.getJoints().get(i);

            model.setPosition(joint.getPosition().x,
                    joint.getPosition().y,
                    joint.getPosition().z);
            model.setScale(0.0005f);

            models[i + 1] = model;
        }
    }

    private void initMd5Anim() throws Exception {
        MD5Model md5MeshModel = MD5Model.parse(filePaths[0]);
        MD5AnimModel md5AnimModel = MD5AnimModel.parse(filePaths[1]);
        animatedModel = MD5LoaderWAnim.process(md5MeshModel, md5AnimModel, new Vector4f(1, 1, 1, 1));

        models = new Model[]{animatedModel};
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
        if (window.isKeyPressed(GLFW_KEY_P) && fileType == 2) {
            animatedModel.nextFrame();
        }
        if (fileType == 1) {
            if (window.isKeyPressed(GLFW_KEY_TAB) && !window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                models[currentActiveJoint + 1].getMesh().swapActive();
                currentActiveJoint = (currentActiveJoint + 1) % jointsAmount;
                models[currentActiveJoint + 1].getMesh().swapActive();
            }
            if (window.isKeyPressed(GLFW_KEY_TAB) && window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                models[currentActiveJoint + 1].getMesh().swapActive();
                currentActiveJoint = (currentActiveJoint - 1) >= 0 ? currentActiveJoint - 1 : jointsAmount - 1;
                models[currentActiveJoint + 1].getMesh().swapActive();
            }
            try {
                if (window.isKeyPressed(GLFW_KEY_UP)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.y += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.y -= 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.x += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.x -= 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_SEMICOLON)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.z += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_SLASH)) {
                    Vector3f position = jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.z -= 1;
                    renewJointsPositions(position);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void renewJointsPositions(Vector3f position) throws Exception {
        jointInfo.getJoints().get(currentActiveJoint).setPosition(position);
        md5MeshModel.setJointInfo(jointInfo);
        Model monster = MD5Loader.process(md5MeshModel, new Vector4f(1, 1, 1, 1));
        models[0] = monster;
        models[currentActiveJoint + 1].setPosition(position.x, position.y, position.z);
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
