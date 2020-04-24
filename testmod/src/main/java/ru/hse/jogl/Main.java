package ru.hse.jogl;

import ru.hse.utils.Engine;

public class Main {
    public static void main(String[] args) {
        try {
            Program program = new Program();
            Engine engine = new Engine("Skeletal animation",
                    600,
                    480,
                    true,
                    program);
            engine.run();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }
}
