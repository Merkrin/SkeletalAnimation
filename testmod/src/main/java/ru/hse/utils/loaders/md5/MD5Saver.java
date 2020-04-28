package ru.hse.utils.loaders.md5;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MD5Saver {
    public static void save(MD5Model md5Model) throws Exception {
        System.out.println("Started saving...");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        File fout = new File(dtf.format(now) + ".md5mesh");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        // TODO: write line by line
        bw.write(md5Model.toString());

        bw.close();

        System.out.println("Saving completed!");
    }
}
