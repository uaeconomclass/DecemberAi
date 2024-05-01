package com.example.decemberai.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    public static List<SpiskiLessons> selectSpiskiLessonsList = new ArrayList<>();
    public static List<SpiskiLessons> fullSpiskiLessonsList = new ArrayList<>();

    public static void addSelectSpiskiLessonsList(SpiskiLessons lessons) {
        selectSpiskiLessonsList.add(lessons);
    }
    // Добавление всех элементов из другого списка
    public static void addAllSelectSpiskiLessonsList(List<SpiskiLessons> lessonsList) {
        selectSpiskiLessonsList.addAll(lessonsList);
    }

    public static void clearSelectSpiskiLessonsList() {
        selectSpiskiLessonsList.clear();
    }

    public static void addFullSpiskiLessonsList(SpiskiLessons lessons) {
        fullSpiskiLessonsList.add(lessons);
    }
    // Добавление всех элементов из другого списка
    public static void addAllFullSpiskiLessonsList(List<SpiskiLessons> lessonsList) {
        fullSpiskiLessonsList.addAll(lessonsList);
    }

    public static void clearFullSpiskiLessonsList() {
        fullSpiskiLessonsList.clear();
    }






    public static List<SpiskiPractice> selectSpiskiPracticeList = new ArrayList<>();
    public static List<SpiskiPractice> fullSpiskiPracticeList = new ArrayList<>();

    public static void addSelectSpiskiPracticeList(SpiskiPractice practice) {
        selectSpiskiPracticeList.add(practice);
    }
    // Добавление всех элементов из другого списка
    public static void addAllSelectSpiskiPracticeList(List<SpiskiPractice> practiceList) {
        selectSpiskiPracticeList.addAll(practiceList);
    }

    public static void clearSelectSpiskiPracticeList() {
        selectSpiskiPracticeList.clear();
    }

    public static void addFullSpiskiPracticeList(SpiskiPractice practice) {
        fullSpiskiPracticeList.add(practice);
    }
    // Добавление всех элементов из другого списка
    public static void addAllFullSpiskiPracticeList(List<SpiskiPractice> practiceList) {
        fullSpiskiPracticeList.addAll(practiceList);
    }

    public static void clearFullSpiskiPracticeList() {
        fullSpiskiPracticeList.clear();
    }



    public static Set<Integer> lessons_item_id = new HashSet<>(); // Set отличается от List тем что Set не дублирует поля, если встречается дубль он его не записывает
    public static Set<Integer> practice_item_id = new HashSet<>();
    public static String userName = "";
    //public static String userEmail = ""; Емайл и пароль храним в преференсах
    public static String userPhone = "";
    //public static String userPassword = "";

    public static String userTest;

    public static int userId;
    public static String userLevel = "";
    public static int userSkill = 0;
    public static String openaiApiKey = "";
    public static String assistantId = "";
    public static String thisVersionApp = "1";
    public static String actualVersionApp = "";
    public static String assistantIdForTest = "";

    public static int howManyWordsToBlocking = 60;

    // Функция обновления Скила пользователя

    public static String userUpdateSkill(String userEmail, Integer id_chat) {
        //emailRegister, String passwordRegister
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/hendlers_api/user_update_skill.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "email=" + userEmail + "&id_chat=" + id_chat;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Установка заголовков запроса
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            // Устанавливаем таймауты соединения и чтения
            connection.setConnectTimeout(60000); // Устанавливаем таймаут соединения на 60 секунд
            connection.setReadTimeout(60000); // Устанавливаем таймаут чтения на 60 секунд

            // Отправка данных
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(postDataBytes);
            }

            // Получение ответа от сервера
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Закрытие соединения
            connection.disconnect();

            // Проверка ответа сервера и возврат результата
            return response.toString();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Вышло время ожидания";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }




    // Функция обновления уровня пользователя
    public static String userUpdateLevel(String userEmail, String userLevel) {
        //emailRegister, String passwordRegister
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/hendlers_api/user_update_level.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "email=" + userEmail + "&userLevel=" + userLevel;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Установка заголовков запроса
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            // Устанавливаем таймауты соединения и чтения
            connection.setConnectTimeout(60000); // Устанавливаем таймаут соединения на 60 секунд
            connection.setReadTimeout(60000); // Устанавливаем таймаут чтения на 60 секунд

            // Отправка данных
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(postDataBytes);
            }

            // Получение ответа от сервера
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Закрытие соединения
            connection.disconnect();

            // Проверка ответа сервера и возврат результата
            return response.toString();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Вышло время ожидания";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }



}
