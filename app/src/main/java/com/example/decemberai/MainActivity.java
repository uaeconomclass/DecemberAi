package com.example.decemberai;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;
import com.example.decemberai.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    // Навигационное меню ч1 начало -------------------------------------------------------------------

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);// Устанавливаем размер стека для хранения страниц
    boolean flag = true;

    // Навигационное меню ч1 Конец -------------------------------------------------------------------

    SharedPreferences sp; // Переменная для SharedPreferences
    String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE); //Прячем до загрузки что бы ни кто не тыкал в меню до подгрузки обработчика нажатия
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences

        // Проверяем есть ли в преференсах емайл и пароль
        if (!AuthorizationStarting()){
            // Если условие не выполняется, переходим на RegisterActivity
            redirectionPageLogIn();
            return; // Останавливаем выполнение кода здесь
        }



        // Анимация загрузки страницы
        TextView text_download_start = findViewById(R.id.text_download_end);
        // Создаем объект анимации
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_blink);
        // Запускаем анимацию
        text_download_start.startAnimation(animation);


        // Если в преференсах висит запись с айди  пройденого события которое не сохранилось на сервере ещё раз пытаемся его сохранить
        int errorUserUpdateSkill = sp.getInt("errorUserUpdateSkill", 0);
        if ( errorUserUpdateSkill != 0) {
            Executor executor5 = Executors.newSingleThreadExecutor();
            executor5.execute(new Runnable() {
                @Override
                public void run() {
                    String result = User.userUpdateSkill(userEmail, errorUserUpdateSkill);
                    if (Objects.equals(result, "true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("111111111111111", "Ошибочный Скилл пользователя обновлен");
                                //Toast.makeText(MainActivity.this, "Ошибочный Скилл пользователя обновлен", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                                editor.putInt("errorUserUpdateSkill", 0); // в editor Сохраняем данные в формате (ключь, значение)
                                editor.apply(); // Применяем изменения
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("111111111111111", "Error. Ошибочный Скилл пользователя НЕ обновлен");
                                //Toast.makeText(MainActivity.this, "Error. Ошибочный Скилл пользователя НЕ обновлен", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }









        AuthorizationCompleted(new AuthorizationCallback() {//А здесть проверим после старта активити правильный ли пароль
            @Override
            public void onAuthorizationCompleted(String result) {
                if (!Objects.equals(result, "true")) { // Если не верный логин и пароль, переходим на RegisterActivity

                    redirectionPageLogIn();
                } else {
                    // Пользователь авторизован, продолжаем выполнение кода
                    getUserData(new UserDataCallback() {// Обновляем данные юзера при старте приложения
                        @Override
                        public void onUserDataReceived(boolean success) {
                            if (success) {
                                //Toast.makeText(MainActivity.this, "Данные пользователя получены успешно", Toast.LENGTH_LONG).show();
                                Log.e("111111111111111", "Данные пользователя получены успешно");
                                // Данные пользователя получены успешно
                                // Здесь можно продолжить выполнение кода после получения данных пользователя
                                // Навигационное меню ч2 начало -------------------------------------------------------------------


                                // Напоминание клиенту что у него не последняя версия приложения
                                if(!Objects.equals(User.actualVersionApp, User.thisVersionApp)){
                                    // выполнить код в основном потоке находясь на второстипенном
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, getString(R.string.new_version_of_the_application_is_available), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


                                // Если пользователь еще не был на странице определения его уровня то это первый его старт, и перекидываем его туда
                                int userViewPageTesterUserActivity = sp.getInt("userViewPageTesterUserActivity", 0);
                                if(userViewPageTesterUserActivity == 0){
                                    Intent intent = new Intent(MainActivity.this, TesterUserActivity.class);// Перекидываем на MainActivity
                                    startActivity(intent);
                                }



                                // Показываем навигационное меню когда все загрузилось
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bottomNavigationView.setVisibility(View.VISIBLE);
                                    }
                                });


                                if(User.howManyWordsToBlocking <= 0){
                                    Intent intent = new Intent(MainActivity.this, PageBlocking.class);// Перекидываем на MainActivity
                                    startActivity(intent);
                                    finish();

                                }



                                integerDeque.push(R.id.btn_home);
                                loadFragment(new HomeFragment());
                                bottomNavigationView.setSelectedItemId(R.id.btn_home);

                                bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                    @Override
                                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                        int id = item.getItemId();
                                        if (integerDeque.contains(id)) {
                                            if (id == R.id.btn_home) {
                                                if (integerDeque.size() != 1) {
                                                    if (flag) {
                                                        integerDeque.addFirst(R.id.btn_home);
                                                        flag = false;
                                                    }
                                                }
                                            }
                                            integerDeque.remove(id);
                                        }
                                        integerDeque.push(id);
                                        loadFragment(getFragment(item.getItemId()));
                                        return true;
                                    }
                                });
                                // Навигационное меню ч2 Конец ---------------------------------------------------------------------

                            }
                        }
                    });
                }
            }
        });
    }

    private void redirectionPageLogIn(){
        // Если условие не выполняется, переходим на RegisterActivity
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
    }
    private boolean AuthorizationStarting() {
        userEmail = sp.getString("email", "");
        userPassword = sp.getString("password", "");

        if ((!userEmail.equals("")) && (!userPassword.equals(""))) {
            return true;
        }else{
           return false;
        }
    }


    private interface AuthorizationCallback {
        void onAuthorizationCompleted(String result);
    }

    private void AuthorizationCompleted(final AuthorizationCallback callback) {
        userEmail = sp.getString("email", "");
        userPassword = sp.getString("password", "");

        if ((!userEmail.equals("")) && (!userPassword.equals(""))) {
            Executor executor2 = Executors.newSingleThreadExecutor();
            executor2.execute(new Runnable() {
                @Override
                public void run() {
                    String result = userCheckLogin(userEmail, userPassword);
                    if (Objects.equals(result, "true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, getString(R.string.the_user_has_been_successfully_logged_in), Toast.LENGTH_SHORT).show();
                                callback.onAuthorizationCompleted("true");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, getString(R.string.user_authorization_error), Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, "Ошибка авторизации пользователя   " + result, Toast.LENGTH_LONG).show();
                                // Ваш код для неудачной авторизации здесь...
                                callback.onAuthorizationCompleted("false");
                            }
                        });
                    }
                }
            });
        } else {
            callback.onAuthorizationCompleted("false");
        }
    }

    public interface UserDataCallback {
        void onUserDataReceived(boolean success);
    }





    private void getUserData(final UserDataCallback callback) {
        // Получаем список всех доступных лекций, здесь пока по умолчанию
        //User.addFullSpiskiLessonsList(new SpiskiLessons(14, "0001-Утренняя кофемания", "cofemania","Бывалый", "#424345", "#ffffff" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(2, "0002-Веселые \nразговорчики", "razgovor_s_druziami","Новичок", "#d0e2c7", "#4F564C" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(13,  "0003-Утренняя кофемания", "cofemania","Новичок", "#e9a95a", "#ffffff" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(4, "0004-Разговорчики \nс подругой", "ic_phyton_small", "Бывалый", "#9FA52D", "#ffffff" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(5, "0005-Утренняя кофемания", "ic_cofe_day_small2","Новичок", "#424345", "#ffffff" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(6, "0006-Утренняя кофемания", "ic_cofe_day_small2","Новичок", "#424345", "#ffffff" ));
        //User.addFullSpiskiLessonsList(new SpiskiLessons(7, "0007-Утренняя кофемания", "ic_cofe_day_small2","Новичок", "#424345", "#ffffff" ));

        // Получаем список всех доступных практик, здесь пока по умолчанию
        //User.addFullSpiskiPracticeList(new SpiskiPractice(11, "Утренняя кофемания", "ic_cofe_day_small2","Новичок", "#424345", "#ffffff")); // Создаем объект на основе класса SpiskiPractice и указываем айди и Название(можно расширить поля)
        //User.addFullSpiskiPracticeList(new SpiskiPractice(12, "Разговорчики \nс подругой", "ic_phyton_small","Новичок", "#9FA52D", "#ffffff" ));
        //User.addFullSpiskiPracticeList(new SpiskiPractice(13, "Утренняя кофемания", "ic_cofe_day_small2","Бывалый", "#424345", "#ffffff")); // Создаем объект на основе класса SpiskiPractice и указываем айди и Название(можно расширить поля)
        //User.addFullSpiskiPracticeList(new SpiskiPractice(14, "Разговорчики \nс подругой", "ic_phyton_small","Бывалый", "#9FA52D", "#ffffff" ));


        //Выполняем асинхронно запрос на получение данных ользователя
        //Вызываем функцию передачи данных на сервер
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = getUserDataAsync();
                if (Objects.equals(result, "true")) {
                    callback.onUserDataReceived(true);// Возвращаем колбек что данные пользователя получены
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this, "Данные пользователя получены успешно ", Toast.LENGTH_SHORT).show();
                            Log.e("111111111111111", "Данные пользователя получены успешно");

                        }
                    });
                } else {
                    // Возвращаем колбек что произошла ошибка получения данных пользователя
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this, "Ошибка получения данных пользователя   " + result, Toast.LENGTH_LONG).show();
                            Log.e("111111111111111", "Ошибка получения данных пользователя   " + result);
                            redirectionPageLogIn();
                        }
                    });
                }
            }
        });

    }

    // Функция получения данных пользователя
    private String getUserDataAsync() {
        userEmail = sp.getString("email", "");
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com//hendlers_api/userData.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "email=" + userEmail;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Установка заголовков запроса
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

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

            // Обработка JSON-ответа и сохранение данных в объекте User
            JSONObject userDataJson = new JSONObject(response.toString());
            User.userName = userDataJson.getString("name");
            User.userPhone = userDataJson.getString("phone");
            User.userLevel = userDataJson.getString("user_level");
            User.userSkill = userDataJson.getInt("user_skill");
            User.openaiApiKey = userDataJson.getString("openaiApiKey");
            //User.assistantId = userDataJson.getString("assistantId");
            User.userId = userDataJson.getInt("userId");
            User.actualVersionApp = userDataJson.getString("actualVersionApp");
            User.howManyWordsToBlocking = userDataJson.getInt("howManyWordsToBlocking");

            // Получение массива завершенных лекций из JSON ------------------------------
            JSONArray completedLessonsJson = userDataJson.getJSONArray("completed_lessons");
            // Очистка списка завершенных уроков
            User.lessons_item_id.clear();
            // Добавление каждого элемента массива завершенных уроков в lessons_item_id
            for (int i = 0; i < completedLessonsJson.length(); i++) {
                int lessonId = completedLessonsJson.getInt(i);
                User.lessons_item_id.add(lessonId);
            }
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

            // Получение массива завершенных практик из JSON ------------------------------
            JSONArray completedPracticeJson = userDataJson.getJSONArray("completed_practices");
            // Очистка списка завершенных уроков
            User.practice_item_id.clear();
            // Добавление каждого элемента массива завершенных уроков в lessons_item_id
            for (int i = 0; i < completedPracticeJson.length(); i++) {
                int practiceId = completedPracticeJson.getInt(i);
                User.practice_item_id.add(practiceId);
            }
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

            // Получение массива всех лекций из JSON ---------------------------------------
            JSONArray allLessonsJson = userDataJson.getJSONArray("lessons_data");
            // Очистка списка всех лекций
            User.clearFullSpiskiLessonsList();
            // Добавление каждого элемента массива завершенных уроков в lessons_item_id
            for (int i = 0; i < allLessonsJson.length(); i++) {
                JSONObject lessonJson = allLessonsJson.getJSONObject(i);
                int lessonId = lessonJson.getInt("lessonId");
                String title = lessonJson.getString("title");
                String img = lessonJson.getString("img");
                String level = lessonJson.getString("level");
                String colorFon = lessonJson.getString("colorFon");
                String colorText = lessonJson.getString("colorText");
                String assistantId = lessonJson.getString("assistantId");
                User.addFullSpiskiLessonsList(new SpiskiLessons(lessonId, title, img,level, colorFon, colorText, assistantId));
            }
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

            // Получение массива всех практик из JSON ---------------------------------------
            JSONArray allPracticesJson = userDataJson.getJSONArray("practices_data");
            // Очистка списка всех лекций
            User.clearFullSpiskiPracticeList();
            // Добавление каждого элемента массива завершенных уроков в lessons_item_id
            for (int i = 0; i < allPracticesJson.length(); i++) {
                JSONObject practiceJson = allPracticesJson.getJSONObject(i);
                int practiceId = practiceJson.getInt("practiceId");
                String title = practiceJson.getString("title");
                String img = practiceJson.getString("img");
                String level = practiceJson.getString("level");
                String colorFon = practiceJson.getString("colorFon");
                String colorText = practiceJson.getString("colorText");
                String assistantId = practiceJson.getString("assistantId");
                User.addFullSpiskiPracticeList(new SpiskiPractice(practiceId, title, img,level, colorFon, colorText, assistantId));
            }
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


            // Проверка ответа сервера и возврат результата
            return "true";

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }





    // Функция проверки авторизации пользователя
    public static String userCheckLogin(String emailRegister, String passwordRegister) {
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/hendlers_api/login.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "email=" + emailRegister + "&password=" + passwordRegister;
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




    // Навигационное меню ч3 Начало ---------------------------------------------------------------------
    private Fragment getFragment(int itemId){

        switch (itemId){

            case R.id.btn_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();


            case R.id.btn_lessons:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new LessonsFragment();


            case R.id.btn_practice:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new PracticeFragment();

            case R.id.btn_account:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new AccountFragment();

        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new HomeFragment();
    }


    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed(){
        integerDeque.pop();

        if(!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }else{
            finish();
        }
    }

    // Навигационное меню ч3 конец ---------------------------------------------------------------------




}