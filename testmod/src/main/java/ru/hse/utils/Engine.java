package ru.hse.utils;

import ru.hse.jogl.Program;
import ru.hse.utils.Window;

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
            update(0.0f);
            render();
        }
    }

    protected void init() throws Exception {
        window.init();
        program.init(window);
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
