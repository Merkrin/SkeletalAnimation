package ru.hse.utils;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
    private final Vector2d previousPosition;
    private final Vector2d currentPosition;
    private final Vector2f displayVector;

    private boolean isInWindow = false;
    private boolean isLeftButtonPressed = false;
    private boolean isRightButtonPressed = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0, 0);
        displayVector = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPosition.x = xpos;
            currentPosition.y = ypos;
        });

        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> isInWindow = entered);

        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            isLeftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            isRightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }

    public void input() {
        displayVector.x = 0;
        displayVector.y = 0;

        if (previousPosition.x > 0 && previousPosition.y > 0 && isInWindow) {
            double deltax = currentPosition.x - previousPosition.x;
            double deltay = currentPosition.y - previousPosition.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;

            if (rotateX)
                displayVector.y = (float) deltax;
            if (rotateY)
                displayVector.x = (float) deltay;
        }

        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public boolean isLeftButtonPressed() {
        return isLeftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return isRightButtonPressed;
    }
}
