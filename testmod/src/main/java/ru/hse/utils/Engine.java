package ru.hse.utils;

import ru.hse.jogl.Program;

public class Engine {
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Window window;
    private final Program program;
    private final MouseInput mouseInput;

    // TODO: delete width and height 'cause they're counted dynamically.
    //  Add args here to choose from loading options:
    //  .obj, only .md5mesh, .md5anim
    public Engine(String windowTitle,
                  boolean vSync, Program program) throws Exception {
        window = new Window(windowTitle,vSync);
        this.program = program;
        mouseInput = new MouseInput();
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
            update();
            render();
        }
    }

    protected void init() throws Exception {
        window.init();
        mouseInput.init(window);
//        if(isPolygon)
        program.init(window);
//        else
//            program.initHelp(window);
    }

    protected void cleanup() {
        program.cleanup();
    }


    protected void input() {
        mouseInput.input(window);
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
