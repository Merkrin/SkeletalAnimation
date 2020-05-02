package ru.hse.utils.loaders.md5;

import java.util.List;

public class MD5ModelHeader {
    private String version;

    private String commandLine;

    private int numJoints;

    private int numMeshes;

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

    public int getNumJoints() {
        return numJoints;
    }

    private void setNumJoints(int numJoints) {
        this.numJoints = numJoints;
    }

    public int getNumMeshes() {
        return numMeshes;
    }

    private void setNumMeshes(int numMeshes) {
        this.numMeshes = numMeshes;
    }

    @Override
    public String toString() {
        return "MD5Version " + version + "\ncommandLine \"add your command-line here\"" +
                "\n\nnumJoints " + numJoints + "\nnumMeshes " + numMeshes + "\n";
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
                        header.setNumJoints(Integer.parseInt(paramValue));
                        break;
                    case "numMeshes":
                        header.setNumMeshes(Integer.parseInt(paramValue));
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
