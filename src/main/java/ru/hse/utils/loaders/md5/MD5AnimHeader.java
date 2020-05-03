package ru.hse.utils.loaders.md5;

import java.util.List;

public class MD5AnimHeader {
    private String version;
    private String commandLine;

    private int framesAmount;
    private int animatedComponentsAmount;
    private int jointsAmount;

    private int frameRate;

    private void setVersion(String version) {
        this.version = version;
    }

    private void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    private void setFramesAmount(int framesAmount) {
        this.framesAmount = framesAmount;
    }

    private void setJointsAmount(int jointsAmount) {
        this.jointsAmount = jointsAmount;
    }

    private void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    private void setAnimatedComponentsAmount(int animatedComponentsAmount) {
        this.animatedComponentsAmount = animatedComponentsAmount;
    }

    @Override
    public String toString() {
        return "animHeader: [version: " + version +
                ", commandLine: " + commandLine +
                ", numFrames: " + framesAmount +
                ", numJoints: " + jointsAmount +
                ", frameRate: " + frameRate +
                ", numAnimatedComponents:" + animatedComponentsAmount + "]";
    }

    public static MD5AnimHeader parse(List<String> lines) throws Exception {
        MD5AnimHeader header = new MD5AnimHeader();

        int numLines = lines != null ? lines.size() : 0;
        if (numLines == 0) {
            throw new Exception("Cannot parse empty file");
        }

        boolean finishHeader = false;
        for (int i = 0; i < numLines && !finishHeader; i++) {
            String line = lines.get(i);
            String[] tokens = line.split("\\s+");

            int tokensAmount = tokens != null ? tokens.length : 0;

            if (tokensAmount > 1) {
                String paramName = tokens[0];
                String paramValue = tokens[1];

                switch (paramName) {
                    case "MD5Version":
                        header.setVersion(paramValue);
                        break;
                    case "commandline":
                        header.setCommandLine(paramValue);
                        break;
                    case "numFrames":
                        header.setFramesAmount(Integer.parseInt(paramValue));
                        break;
                    case "numJoints":
                        header.setJointsAmount(Integer.parseInt(paramValue));
                        break;
                    case "frameRate":
                        header.setFrameRate(Integer.parseInt(paramValue));
                        break;
                    case "numAnimatedComponents":
                        header.setAnimatedComponentsAmount(Integer.parseInt(paramValue));
                        break;
                    case "hierarchy":
                        finishHeader = true;
                        break;
                }
            }
        }

        return header;
    }
}
