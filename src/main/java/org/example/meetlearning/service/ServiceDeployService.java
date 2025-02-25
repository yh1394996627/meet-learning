package org.example.meetlearning.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ServiceDeployService {

    public String uploadFolder(Integer type, MultipartFile file) {
        if (file.isEmpty()) {
            return "文件不能为空";
        }
        if (type != 1 && type != 2) {
            return "type 类型不对  1夺分派  2后台管理";
        }
        String dist = type == 1 ? "manage" : "student";
        String destinationPath = "/usr/local/apps/vue/" + dist + "/";
        // 指定上传文件的存储路径
        String filePath = destinationPath + file.getOriginalFilename();
        System.out.println("文件目录：" + filePath);
        // 保存文件
        try {
            file.transferTo(new File(filePath));
            deployService(dist, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return "文件保存失败";
        }
        return "文件保存成功";
    }

    private String deployService(String dfp, String filePath) {
        // 直接写死了固定地址
        deleteDirectory("/usr/share/nginx/html/" + dfp + "/dist/");
        deleteDirectory("/usr/share/nginx/html/" + dfp + "/__MACOSX/");
        String unzipCommand = "unzip " + filePath + " -d " + "/usr/share/nginx/html/" + dfp + "/";
        System.out.println("解压路径：" + unzipCommand);
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec(unzipCommand);
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                return "解压命令执行失败，退出码：" + exitCode;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "解压命令执行异常";
        } finally {
            // 删除原始ZIP文件
            new File(filePath).delete();
        }
        deploy();
        result = "服务发布成功";
        return result;
    }


    public void deleteDirectory(String directoryPath) {
        String command = "rm -rf " + directoryPath;
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("目录删除成功");
            } else {
                System.out.println("目录删除失败，退出码：" + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void deploy() {
        try {
            System.out.println("开始发布服务");
            String command = "sudo systemctl reload nginx";
            Process process1 = Runtime.getRuntime().exec(command);
            process1.waitFor();
            System.out.println("重新加载配置完成");
            command = "sudo systemctl restart nginx";
            Process process2 = Runtime.getRuntime().exec(command);
            process2.waitFor();
            System.out.println("重新启动nginx完成");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
