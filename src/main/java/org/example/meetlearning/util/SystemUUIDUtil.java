package org.example.meetlearning.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemUUIDUtil {

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        String systemUUID = getSystemUUID(os);
        System.out.println("System UUID: " + systemUUID);
    }

    public static String getSystemUUID(String os) {
        try {
            if (os.contains("mac")) {
                // macOS: 使用 system_profiler 获取 Hardware UUID
                return executeCommand("system_profiler SPHardwareDataType | awk '/UUID/ {print $NF}'");
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux: 使用 dmidecode 获取 system-uuid
                return executeCommand("sudo dmidecode -s system-uuid");
            } else if (os.contains("win")) {
                // Windows: 使用 wmic 获取 BIOS UUID
                return executeCommand("wmic csproduct get uuid");
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + os);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line.trim());
            }
            return output.toString().replace("UUID", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}