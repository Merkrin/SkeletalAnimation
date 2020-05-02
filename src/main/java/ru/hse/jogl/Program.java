package ru.hse.jogl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import ru.hse.graphic.*;
import ru.hse.graphic.animation.AnimatedModel;
import ru.hse.utils.MouseInput;
import ru.hse.utils.loaders.OBJLoader;
import ru.hse.utils.Window;
import ru.hse.utils.loaders.md5.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private MD5JointInfo firstJointsInfo;

    private int currentActiveJoint = 0;
    private int jointsAmount;

    private MD5Model md5MeshModel;

    private Window window;

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
        mainModel = MD5Loader.process(md5MeshModel);

        jointInfo = md5MeshModel.getJointInfo();
        firstJointsInfo = MD5Model.parse(filePaths[0]).getJointInfo();
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
        animatedModel = MD5LoaderWAnim.process(md5MeshModel, md5AnimModel,
                new Vector4f(1, 1, 1, 1));

        models = new Model[]{animatedModel};
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
        if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_I)) {
            saveImage();
        }
        if (window.isKeyPressed(GLFW_KEY_P) && fileType == 2) {
            animatedModel.nextFrame();
        }
        if (fileType == 1) {
            if (window.isKeyPressed(GLFW_KEY_TAB) &&
                    !window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                models[currentActiveJoint + 1].getMesh().swapActive();
                currentActiveJoint = (currentActiveJoint + 1) % jointsAmount;
                models[currentActiveJoint + 1].getMesh().swapActive();
            }
            if (window.isKeyPressed(GLFW_KEY_TAB) &&
                    window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                models[currentActiveJoint + 1].getMesh().swapActive();
                currentActiveJoint = (currentActiveJoint - 1) >= 0 ?
                        currentActiveJoint - 1 : jointsAmount - 1;
                models[currentActiveJoint + 1].getMesh().swapActive();
            }
            try {
                if (window.isKeyPressed(GLFW_KEY_R)) {
                    List<MD5JointInfo.MD5JointData> firstJoints =
                            firstJointsInfo.getJoints();
                    List<MD5JointInfo.MD5JointData> joints =
                            jointInfo.getJoints();
                    Vector3f currentJoint;

                    for (int i = 0; i < jointsAmount; i++) {
                        currentJoint = firstJoints.get(i).getPosition();
                        joints.get(i).setPosition(new Vector3f(currentJoint.x,
                                currentJoint.y, currentJoint.z));

                        models[i + 1].setPosition(currentJoint.x,
                                currentJoint.y, currentJoint.z);
                    }

                    jointInfo.setJoints(joints);
                    md5MeshModel.setJointInfo(jointInfo);

                    Model monster = MD5Loader.process(md5MeshModel);
                    models[0] = monster;
                }
                if (window.isKeyPressed(GLFW_KEY_F)) {
                    MD5Saver.save(md5MeshModel);
                }
                if (window.isKeyPressed(GLFW_KEY_UP)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.y += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.y -= 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.x += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.x -= 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_SEMICOLON)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
                    position.z += 1;
                    renewJointsPositions(position);
                }
                if (window.isKeyPressed(GLFW_KEY_SLASH)) {
                    Vector3f position =
                            jointInfo.getJoints().get(currentActiveJoint).getPosition();
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
        Model monster = MD5Loader.process(md5MeshModel);
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
    }

    private void saveImage() {
        GL11.glReadBuffer(GL11.GL_FRONT);

        int height = window.getHeight();
        int width = window.getWidth();

        int bpp = 4;
        ByteBuffer buffer =
                BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        File file = new File(dtf.format(now) + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1),
                        (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void cleanup() {
        renderer.cleanup();
        for (Model model : models) {
            model.getMesh().cleanUp();
        }
    }
}
