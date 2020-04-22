package ru.hse.jogl;

public class Main {
    public static void main(String[] args) {
        try {
            Program program = new Program();
            Engine engine = new Engine("GAME", 600, 480, true, program);
            engine.run();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }
}
