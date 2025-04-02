//import com.google.gson.JsonObject;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;// 1. 检查用户是否存在并获取激活链接
//public Map<String, Object> checkUserAndGetActivationLink(String userEmail, String zoomApiToken) throws IOException {
//    Map<String, Object> result = new HashMap<>();
//
//    // 首先检查用户是否已存在
//    boolean userExists = checkUserExists(userEmail, zoomApiToken);
//    result.put("userExists", userExists);
//
//    if (!userExists) {
//        // 用户不存在，创建用户并获取激活链接
//        String activationLink = createUserAndGetActivationLink(userEmail,zoomApiToken);
//        result.put("activationLink", activationLink);
//    }
//
//    return result;
//}
//
//// 检查用户是否存在
//private boolean checkUserExists(String email, String zoomApiToken) throws IOException {
//    Request request = new Request.Builder()
//            .url(zoomApiUrl + "/users/" + email)
//            .header("Authorization", "Bearer " + zoomApiToken)
//            .build();
//
//    try (Response response = client.newCall(request).execute()) {
//        return response.isSuccessful();
//    }
//}
//
//// 创建用户并获取激活链接
//private String createUserAndGetActivationLink(String email, String zoomApiToken) throws IOException {
//    JSONObject userInfo = new JSONObject();
//    userInfo.put("email", email);
//    userInfo.put("type", 2); // 2 表示普通用户
//    userInfo.put("first_name", "New");
//    userInfo.put("last_name", "User");
//
//    JSONObject requestBody = new JSONObject();
//    requestBody.put("action", "create");
//    requestBody.put("user_info", userInfo);
//
//    RequestBody body = RequestBody.create(requestBody.toString(), JSON);
//    Request request = new Request.Builder()
//            .url(zoomApiUrl + "/users")
//            .header("Authorization", "Bearer " + zoomApiToken)
//            .post(body)
//            .build();
//
//    try (Response response = client.newCall(request).execute()) {
//        if (!response.isSuccessful()) {
//            throw new IOException("Failed to create user: " + response.body().string());
//        }
//
//        JsonObject responseJson = gson.fromJson(response.body().string(), JsonObject.class);
//        return responseJson.get("activation_url").getAsString();
//    }
//}
//
//// 2. 将用户添加到管理组
//public boolean addUserToAdminGroup(String userEmail, String groupId, String zoomApiToken  ) throws IOException {
//    // 首先获取用户ID
//    String userId = getUserIdByEmail(userEmail,zoomApiToken);
//    if (userId == null) {
//        throw new IOException("User not found: " + userEmail);
//    }
//
//    // 将用户添加到指定组
//    JsonObject requestBody = new JsonObject();
//    requestBody.addProperty("members", "[{\"email\": \"" + userEmail + "\"}]");
//
//    RequestBody body = RequestBody.create(requestBody.toString(), JSON);
//    Request request = new Request.Builder()
//            .url(zoomApiUrl + "/groups/" + groupId + "/members")
//            .header("Authorization", "Bearer " + zoomApiToken)
//            .post(body)
//            .build();
//
//    try (Response response = client.newCall(request).execute()) {
//        return response.isSuccessful();
//    }
//}
//
//// 获取用户ID
//private String getUserIdByEmail(String email,String zoomApiToken) throws IOException {
//    Request request = new Request.Builder()
//            .url(zoomApiUrl + "/users/" + email)
//            .header("Authorization", "Bearer " + zoomApiToken)
//            .build();
//
//    try (Response response = client.newCall(request).execute()) {
//        if (!response.isSuccessful()) {
//            return null;
//        }
//
//        JsonObject responseJson = gson.fromJson(response.body().string(), JsonObject.class);
//        return responseJson.get("id").getAsString();
//    }
//}