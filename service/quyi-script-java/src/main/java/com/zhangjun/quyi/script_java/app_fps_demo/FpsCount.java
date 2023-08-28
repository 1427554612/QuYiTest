package com.zhangjun.quyi.script_java.app_fps_demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FpsCount {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("cmd /c adb shell dumpsys gfxinfo com.android.chrome");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Draw:")) {
                    System.out.println("FPS: " + line);
                    // 获取Draw命令中的FPS数据
                    String[] tokens = line.trim().split("\\s+");
                    if (tokens.length >= 9) {
                        String fps = tokens[8];
                        System.out.println("FPS: " + fps);
                    }
                }
            }

            process.waitFor();
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
