package com.example.decemberai.model;

import java.util.HashSet;
import java.util.Set;

public class User {

    private static User instance; // Объект в который будем записывать данные пользователя, сделал что бы был только один объект и к нему можно было обратиться из любого класса
    //Как бы создаем глобального юзера к которому можно ото всюду обратиться
    // Приватный конструктор, чтобы предотвратить создание объекта User извне
    private User() {}

    public static Set<Integer> lessons_item_id = new HashSet<>(); // Set отличается от List тем что Set не дублирует поля, если встречается дубль он его не записывает
    public static Set<Integer> practice_item_id = new HashSet<>();
    private String userName = "";
    private String userEmail = "";
    private String userPhone = "";
    private String userPassword = "";

    private int userId = 1;// Наверно не нужно
    private String userLevel = "Новичок";
    private int userSkill = 15;


    // Метод для получения единственного экземпляра User
    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

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
