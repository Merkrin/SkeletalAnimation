package ru.hse.utils.loaders.md5;

import java.util.List;

public class MD5ModelHeader {
    private String version;
    private String commandLine;

    private int jointsAmount;
    private int meshesAmount;

    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public String getCommandLine() {
        return commandLine;
    }

    private void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public int getJointsAmount() {
        return jointsAmount;
    }

    private void setJointsAmount(int jointsAmount) {
        this.jointsAmount = jointsAmount;
    }

    public int getMeshesAmount() {
        return meshesAmount;
    }

    private void setMeshesAmount(int meshesAmount) {
        this.meshesAmount = meshesAmount;
    }

    @Override
    public String toString() {
        return "MD5Version " + version + "\ncommandLine \"add your command-line here\"" +
                "\n\nnumJoints " + jointsAmount + "\nnumMeshes " + meshesAmount + "\n";
    }

    public static MD5ModelHeader parse(List<String> lines) throws Exception {
        MD5ModelHeader header = new MD5ModelHeader();
        int numLines = lines != null ? lines.size() : 0;

        if (numLines == 0) {
            throw new Exception("Cannot parse empty file");
        }

        boolean finishHeader = false;
        for (int i = 0; i < numLines && !finishHeader; i++) {
            String line = lines.get(i);
            String[] tokens = line.split("\\s+");

            int numTokens = tokens != null ? tokens.length : 0;

            if (numTokens > 1) {
                String paramName = tokens[0];
                String paramValue = tokens[1];

                switch (paramName) {
                    case "MD5Version":
                        header.setVersion(paramValue);
                        break;
                    case "commandline":
                        header.setCommandLine(paramValue);
                        break;
                    case "numJoints":
                        header.setJointsAmount(Integer.parseInt(paramValue));
                        break;
                    case "numMeshes":
                        header.setMeshesAmount(Integer.parseInt(paramValue));
                        break;
                    case "joints":
                        finishHeader = true;
                        break;
                }
            }
        }

        return header;
    }
}
