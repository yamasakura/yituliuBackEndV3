package com.lhs.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class ReadFileUtil {

    public static String readFile(String fileName) {
//        System.out.println(fileName);
        String str = "";
        try {
            File jsonFile = new File(fileName);
            Reader reader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            reader.close();
            str = sb.toString();
            return str;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(MultipartFile file) {
        String jsonStr = "";
        try {
            File toFile = null;
            if (file.equals("") || file.getSize() <= 0) {
                file = null;
            } else {
                InputStream ins = null;
                ins = file.getInputStream();
                toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
                inputStreamToFile(ins, toFile);
                ins.close();
            }

            Reader reader = new InputStreamReader(Files.newInputStream(toFile.toPath()), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = Files.newOutputStream(file.toPath());
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
