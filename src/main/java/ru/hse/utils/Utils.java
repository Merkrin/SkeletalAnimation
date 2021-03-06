package ru.hse.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;

        try (InputStream in = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in,
                     java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

    public static List<String> readAllLines(String fileName,
                                            boolean isAbsolutePath)
            throws Exception {
        List<String> list = new ArrayList<>();
        if (isAbsolutePath) {
            try (BufferedReader br =
                         new BufferedReader(
                                 new InputStreamReader(
                                         new FileInputStream(fileName)))) {
                String line;

                while ((line = br.readLine()) != null)
                    list.add(line);
            }
        } else {
            try (BufferedReader br =
                         new BufferedReader(
                                 new InputStreamReader(
                                         Class.forName(
                                                 Utils.class.getName())
                                                 .getResourceAsStream(fileName)))) {
                String line;

                while ((line = br.readLine()) != null)
                    list.add(line);
            }
        }

        return list;
    }

    public static int[] listIntToArray(List<Integer> list) {
        return list.stream().mapToInt((Integer v) -> v).toArray();
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];

        for (int i = 0; i < size; i++)
            floatArr[i] = list.get(i);

        return floatArr;
    }
}