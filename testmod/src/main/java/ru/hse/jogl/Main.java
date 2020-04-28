package ru.hse.jogl;

import ru.hse.utils.Engine;

public class Main {
    private static void printHelp() {
        System.out.println("Use yor keyboard keys <W>, <S>, <A>, <D>, <X>" +
                " and <SPACE> to move and right mouse button to rotate camera.\n" +
                "Use <I> to save a picture (note that it may be scaled!).");
        System.out.println("If you choose .obj file:\n" +
                "\t-You will only be able to move and rotate camera.");
        System.out.println("If you choose only .md5mesh file:\n" +
                "\t-Use <TAB> to choose next joint;\n" +
                "\t-Use <TAB>+<LEFT_SHIFT> to choose previous joint;\n" +
                "\t-Use arrows on your keyboard, <;> and </> to change joint's position;\n" +
                "\t-Use <R> to dismiss changes.");
        System.out.println("If you choose .md5mesh and .md5anim files:\n" +
                "\t-Use <P> to show next frame.");
    }

    private static byte getFileOption(String path) {
        int pathLength = path.length();
        int endingLength = 4;

        if (pathLength <= endingLength)
            return -1;

        String ending = path.substring(pathLength - endingLength, pathLength);

        if (ending.equalsIgnoreCase(".obj"))
            return 0;

        endingLength = 8;

        if (pathLength <= endingLength)
            return -1;

        ending = path.substring(pathLength - endingLength, pathLength);

        return (byte) (ending.equalsIgnoreCase(".md5mesh") ? 1
                : ending.equalsIgnoreCase(".md5anim") ? 2 : -1);
    }

    public static void main(String[] args) {
        //System.setProperty("java.awt.headless", "true");

        // TODO:
        String[] filePaths = null;
        byte fileOption = -1;

        int argsLength = args.length;

        System.out.println("Welcome to the Skeletal Animation Editor!");

        if (argsLength > 0 && argsLength <= 3) {
            if (args[0].equalsIgnoreCase("help")) {
                printHelp();
                if (argsLength > 1) {
                    byte option = getFileOption(args[1]);

                    if (option == 0 && argsLength != 3) {
                        filePaths = new String[]{args[1]};
                        fileOption = option;
                    } else if (option == 1) {
                        if (argsLength > 2) {
                            if (getFileOption(args[2]) == 2) {
                                filePaths = new String[]{args[1], args[2]};
                                fileOption = 2;
                            }
                        } else {
                            filePaths = new String[]{args[1]};
                            fileOption = option;
                        }
                    }
                }
            } else {
                if (argsLength == 1) {
                    fileOption = getFileOption(args[0]);

                    if (fileOption != -1)
                        filePaths = new String[]{args[0]};
                } else if(argsLength == 2) {
                    if (getFileOption(args[0]) == 1 && getFileOption(args[1]) == 2) {
                        fileOption = 2;

                        filePaths = new String[]{args[0], args[1]};
                    }
                }
            }
        }

        if (filePaths != null && fileOption != -1) {
            try {
                Program program = new Program(filePaths, fileOption);

                Engine engine = new Engine("Skeletal animation",
                        true,
                        program);
                engine.run();
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }else{
            System.out.println("Something went wrong in your input." +
                    " Please check arguments.");
        }
    }
}
