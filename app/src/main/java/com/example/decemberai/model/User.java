package com.example.decemberai.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    public static Set<Integer> lessons_item_id = new HashSet<>(); // Set отличается от List тем что Set не дублирует поля, если встречается дубль он его не записывает
    public static Set<Integer> practice_item_id = new HashSet<>();
    String userName = "rer";
    String userEmail = "qwqe";
    String userPhone = "323234";
    String userPassword = "2e234";

    int userId = 1;// Наверно не нужно
    String userLevel = "Новичок";
    int userSkill = 15;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(int userSkill) {
        this.userSkill = userSkill;
    }



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
