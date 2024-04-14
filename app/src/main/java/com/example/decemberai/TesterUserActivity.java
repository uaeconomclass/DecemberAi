package com.example.decemberai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.decemberai.model.ChatPage;
import com.example.decemberai.model.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TesterUserActivity extends AppCompatActivity {

    private Button btnStartLearning, btnTakeTest;
    private Context context;
    private boolean userSelectedParameter;
    private String userEmail;
    SharedPreferences sp; // Переменная для SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester_user);


        context = this;
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences
        userEmail = sp.getString("email", "");

        // Записываем в переменную что пользователь посещал эту страницу
        SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
        editor.putInt("userViewPageTesterUserActivity", 1); // в editor Сохраняем данные в формате (ключь, значение)
        editor.apply(); // Применяем изменения


        btnStartLearning = findViewById(R.id.btnStartLearning);
        // По клику на btnStartLearning перекидываем на MainActivity
        btnStartLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем намерение для перехода на активити TesterUserClass
                Intent intent = new Intent(TesterUserActivity.this, MainActivity.class);
                // Запускаем активити MainActivity
                startActivity(intent);
            }
        });



        btnTakeTest = findViewById(R.id.btnTakeTest);
        // По клику на btnStartLearning перекидываем на MainActivity
        btnTakeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TesterUserActivity.this, ChatPage.class);// Создаем условие для переадрессации на LessonsPage


                int imageId = context.getResources().getIdentifier("img_301", "drawable", context.getPackageName());// Получаем айди картинки по названию

                intent.putExtra("spiskiChatBackGround", Color.parseColor("#f8f1ab"));
                intent.putExtra("spiskiChatColorText", Color.parseColor("#8e3a09"));
                intent.putExtra("spiskiChatImageId", imageId);
                intent.putExtra("spiskiChatTitle", "Testing");
                intent.putExtra("chatPageLevel", "test");
                intent.putExtra("spiskiChatId", 0);
                intent.putExtra("assistantId", "asst_6M2GxnItZV8Xz8hXllFCFWAj");
                intent.putExtra("typeOfChat", "testerUser");


                startActivity(intent);
            }
        });





        Spinner parameterSpinner = findViewById(R.id.parameter_spinner);

    // Создание списка параметров
        List<String> parameters = new ArrayList<>();
        parameters.add("A0");
        parameters.add("A1");
        parameters.add("A2");
        parameters.add("B1");
        parameters.add("B2");
        parameters.add("C1");
        parameters.add("C2");

        // Создание адаптера
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, parameters);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        // Установка адаптера для Spinner
        parameterSpinner.setAdapter(adapter);

        // Установка значения "A1" по умолчанию
        int defaultPosition = adapter.getPosition(User.userLevel);
        parameterSpinner.setSelection(defaultPosition);

        // Флаг для проверки, был ли выбор параметра изменен пользователем
         userSelectedParameter = false;

        parameterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userSelectedParameter) {
                    // Получаем выбранный параметр
                    String selectedParameter = (String) parent.getItemAtPosition(position);
                    Toast.makeText(TesterUserActivity.this, "Выбран параметр " + selectedParameter, Toast.LENGTH_SHORT).show();

                    Executor executor3 = Executors.newSingleThreadExecutor();
                    executor3.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = userUpdateLevel2(userEmail, selectedParameter);
                            if (Objects.equals(result, "true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TesterUserActivity.this, "Уровень пользователя обновлен", Toast.LENGTH_SHORT).show();
                                        User.userLevel = selectedParameter;

                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TesterUserActivity.this, "Error. There may be a problem with the Internet", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }
                    });
                }
                // Сброс флага после выполнения действий
                userSelectedParameter = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Можно оставить пустым, если не нужно обрабатывать это событие
            }
        });
    }


    // Функция обновления уровня пользователя
    public static String userUpdateLevel2(String userEmail, String userLevel) {
        //emailRegister, String passwordRegister
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/hendlers_api/add_id_chat.php");

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