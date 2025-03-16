package org.example.meetlearning.util;


public class RedisCommonsUtil {

    public static String emailKeyGet(String email) {
        return String.format("user::email::%s", email);
    }

}
