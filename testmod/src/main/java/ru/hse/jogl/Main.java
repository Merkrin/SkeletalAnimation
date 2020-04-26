package ru.hse.jogl;

import ru.hse.utils.Engine;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");

        try {
            Program program = new Program();

            // TODO: delete width and height 'cause they're counted dynamically.
            //  Add args here to choose from loading options
            Engine engine = new Engine("Skeletal animation",
                    600,
                    480,
                    true,
                    program, false);
            engine.run();

            engine = new Engine("Skeletal animation",
                    600,
                    480,
                    true,
                    program, true);
            engine.run();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
}
