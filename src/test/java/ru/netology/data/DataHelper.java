package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }


    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo(String login) {
        String password = null;
        if (login.equals("vasya")) {
            password = "qwerty123";
        } else if (login.equals("petya")) {
            password = "123qwerty";
        }
        return new AuthInfo(login, password);
    }

    public static AuthInfo getInvalidAuthInfo(AuthInfo user, String param) {
        Faker faker = new Faker(new Locale("en"));
        AuthInfo userInfo = null;
        if (param.equals("login")) {
            userInfo = new AuthInfo(faker.beer().name(), user.getPassword());
        } else if (param.equals("password")) {
            userInfo = new AuthInfo(user.getLogin(), faker.beer().name());
        }
        return userInfo;
    }

    public static String getInvalidVerificationCode() {
        return String.valueOf((int) (Math.random() * 1000000));
    }
}