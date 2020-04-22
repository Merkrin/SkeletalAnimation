package ru.hse.jogl;

import ru.hse.jogl.*;

public class Engine {
    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Program program;

    public Engine(String windowTitle, int width, int height, boolean vSync, Program program) throws Exception {
        window = new Window(windowTitle, width, height, vSync);
        this.program = program;
    }

    public void run() {
        try {
            init();
            loop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void loop() {
        while (!window.windowShouldClose()) {
            input();

            render();
        }
    }

    protected void init() throws Exception {
        window.init();
        program.init();
    }

    protected void cleanup() {
        program.cleanup();
    }


    protected void input() {
        program.input(window);
    }

    protected void update(float interval) {
        program.update(interval);
    }

    protected void render() {
        program.render(window);
        window.update();
    }
}
