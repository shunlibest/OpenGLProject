package org.example;

import java.io.*;
import java.net.URL;

public class FileUtils {

    public static String readResourceFile(String name) {
        URL resource = ClassLoader.getSystemResource(name);
        File file = new File(resource.getPath());
        return convertFileToString(file.getAbsolutePath());
    }

    public static String convertFileToString(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // 可根据需要自定义换行符
            }
            bufferedReader.close();
            fileInputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
