package org.example.meetlearning.common;

import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class QfUserUtil {

    public final static class LoginResult {
        /**
         * code可能的取值：
         * 0     登录成功（注册成功）
         * 101   登录失败（注册失败），数据库错误
         * 10001 用户名不能为空
         * 10002 密码不能为空
         * 10003 用户名已经存在
         * 10004 用户不存在
         * 10005 密码错误
         */
        public int code;
        public String msg;
        public long userId;
        public String token;
    }

    public enum Gender {
        M,
        F
    }

    public static final int ConnTimeoutMillis = 5000;
    public static final int ReadTimeoutMillis = 7000;

    /**
     * 尝试登录，如果用户不存在，则再尝试注册
     * @param userName 用户名
     * @param password 密码
     * @param pid 合作方约定的id
     * @param nickName 用户昵称
     * @param mobile 用户手机号
     * @param email 用户email地址
     * @param gender 用户性别： M=男，F=女
     * @return 如果成功，然后用户id和token，如果失败返回
     */
    public LoginResult loginOrRegister(String userName, String password,
                                       String pid, String nickName, String mobile,
                                       String email, Gender gender) throws IOException {
        LoginResult loginResult = login(userName, password);
        if(loginResult.code == 10004) { //用户不存在
            return register(userName, password, pid, nickName, mobile, email, gender);
        }
        return loginResult;
    }

    public LoginResult login(String userName, String password) throws IOException {
        String url = "https://readapi.bluebirdabc.com/user/login";
        JSONObject request = new JSONObject();
        request.put("username", userName);
        request.put("password", password);
        return invokeReadApi(url, request);
    }

    public LoginResult register(String userName, String password,
                                String pid, String nickName, String mobile,
                                String email, Gender gender) throws IOException {
        String url = "https://readapi.bluebirdabc.com/user/register";
        JSONObject request = new JSONObject();
        request.put("username", userName);
        request.put("password", password);
        request.put("pid", pid);
        request.put("nickname", nickName);
        request.put("mobile", mobile);
        request.put("email", email);
        request.put("gender", gender.toString());
        return invokeReadApi(url, request);
    }

    private LoginResult invokeReadApi(String remoteUrl, JSONObject request) throws IOException {

        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();

        try {

            URL url = new URL(remoteUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(ConnTimeoutMillis);
            connection.setReadTimeout(ReadTimeoutMillis);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            connection.connect();

            os = connection.getOutputStream();
            os.write(request.toString().getBytes());
            os.flush();

            is = connection.getInputStream();

            byte[] buffer = new byte[1024];
            while(true) {
                int len = is.read(buffer);
                if(len <= 0)
                    break;
                outBuffer.write(buffer, 0, len);
            }

            String r = new String(outBuffer.toByteArray());
            JSONObject res = new JSONObject(r);

            LoginResult loginResult = new LoginResult();
            loginResult.code = res.getInt("code");
            loginResult.msg  = res.getStr("msg");

            if(loginResult.code == 0) {
                JSONObject data = res.getJSONObject("data");
                loginResult.userId = data.getLong("userId");
                loginResult.token = data.getStr("token");
            }

            return loginResult;

        } finally {
            closeStream(os);
            closeStream(is);
            if(connection != null) { connection.disconnect(); }
        }
    }

    private static void closeStream(Closeable c) {
        if(c == null)
            return;
        try {
            c.close();
        } catch (IOException e) {
        }
    }

}
