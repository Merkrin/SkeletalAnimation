package ru.hse.utils;

import ru.hse.jogl.Program;

public class Engine {
    private final Window window;
    private final Program program;
    private final MouseInput mouseInput;

    public Engine(String windowTitle,
                  boolean vSync, Program program) {
        window = new Window(windowTitle, vSync);
        this.program = program;
        this.program.setWindow(window);
        mouseInput = new MouseInput();
    }

    public void run() {
        try {
            init();
            loop();
        } catch (Exception excp) {
            System.out.println(excp.getMessage());
        } finally {
            cleanup();
        }
    }

    protected void loop() {
        while (!window.windowShouldClose()) {
            input();
            update();
            render();
        }
    }

    protected void init() throws Exception {
        window.init();
        mouseInput.init(window);
        program.init(window);
    }

    protected void cleanup() {
        program.cleanup();
    }


    protected void input() {
        mouseInput.input();
        program.input(window);
    }

    protected void update() {
        program.update(mouseInput);
    }

    protected void render() {
        program.render(window);
        window.update();
    }
}
