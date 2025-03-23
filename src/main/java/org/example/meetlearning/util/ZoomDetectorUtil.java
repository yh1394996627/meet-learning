package org.example.meetlearning.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZoomDetectorUtil {

    // Zoom 可执行文件的名称
    private static final String ZOOM_EXECUTABLE_WINDOWS = "Zoom.exe";
    private static final String ZOOM_EXECUTABLE_MAC_LINUX = "Zoom";

    // Zoom 常见的安装路径
    private static final List<String> WINDOWS_PATHS = List.of(
            System.getenv("ProgramFiles") + "\\Zoom",
            System.getenv("LocalAppData") + "\\Zoom"
    );
    private static final List<String> MAC_PATHS = List.of(
            "/Applications/zoom.us.app/Contents/MacOS",
            System.getProperty("user.home") + "/Applications/zoom.us.app/Contents/MacOS"
    );
    private static final List<String> LINUX_PATHS = List.of(
            "/usr/bin",
            "/usr/local/bin",
            System.getProperty("user.home") + "/.zoom"
    );

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        String zoomPath = detectZoom(os);
        if (zoomPath != null) {
            System.out.println("Zoom is installed at: " + zoomPath);
        } else {
            System.out.println("Zoom is not installed.");
        }
    }

    public static String detectZoom(String os) {
        List<String> searchPaths = new ArrayList<>();
        String executableName;
        if (os.contains("win")) {
            // Windows
            searchPaths.addAll(WINDOWS_PATHS);
            executableName = ZOOM_EXECUTABLE_WINDOWS;
        } else if (os.contains("mac")) {
            // macOS
            searchPaths.addAll(MAC_PATHS);
            executableName = ZOOM_EXECUTABLE_MAC_LINUX;
        } else if (os.contains("nix") || os.contains("nux")) {
            // Linux
            searchPaths.addAll(LINUX_PATHS);
            executableName = ZOOM_EXECUTABLE_MAC_LINUX;
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
        // 搜索常见路径
        for (String path : searchPaths) {
            File zoomExecutable = new File(path, executableName);
            if (zoomExecutable.exists()) {
                return zoomExecutable.getAbsolutePath();
            }
        }
        // 如果常见路径未找到，尝试全局搜索（性能较低）
        return searchFileSystem(executableName);
    }

    private static String searchFileSystem(String executableName) {
        // 搜索根目录（可能需要管理员权限）
        File[] roots = File.listRoots();
        for (File root : roots) {
            String result = searchDirectory(root, executableName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static String searchDirectory(File directory, String executableName) {
        if (!directory.isDirectory()) {
            return null;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 递归搜索子目录
                String result = searchDirectory(file, executableName);
                if (result != null) {
                    return result;
                }
            } else if (file.getName().equals(executableName)) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
}