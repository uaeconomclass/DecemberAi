package com.example.decemberai.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    public static Set<Integer> lessons_item_id = new HashSet<>(); // Set отличается от List тем что Set не дублирует поля, если встречается дубль он его не записывает
    public static Set<Integer> practice_item_id = new HashSet<>();

    int userId = 1;

    String userLevel = "Новичок";

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
