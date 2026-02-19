package com.example.decemberai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText newPassword1EditText, newPassword2EditText;
    private Button btnChangePassword;
    private String email, password, newPassword;
    SharedPreferences sp; // Переменная для SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences


        email = sp.getString("email", "");
        password = sp.getString("password", "");

        // Находим поля ввода и кнопку в макете
        newPassword1EditText = findViewById(R.id.newPassword1);
        newPassword2EditText = findViewById(R.id.newPassword2);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Назначаем обработчик нажатия на кнопку
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем значения из полей ввода
                String newPassword1 = newPassword1EditText.getText().toString().trim();
                String newPassword2 = newPassword2EditText.getText().toString().trim();

                // Проверяем, что оба поля не пустые
                if (!newPassword1.isEmpty() && !newPassword2.isEmpty()) {
                    // Сравниваем значения из обоих полей
                    if (newPassword1.equals(newPassword2)) {
                        // Если значения совпадают, показываем сообщение
                        //Toast.makeText(ChangePasswordActivity.this, "Проверка пройдена можно обновлять пароль", Toast.LENGTH_SHORT).show();


                        Executor executor2 = Executors.newSingleThreadExecutor();
                        executor2.execute(new Runnable() {
                            @Override
                            public void run() {
                                String result = userChangePassword(email, password, newPassword1);
                                if (Objects.equals(result, "true")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ChangePasswordActivity.this, getString(R.string.password_has_been_successfully_updated), Toast.LENGTH_SHORT).show();
                                            // Если вернулся ОК записываем данные в SharedPreferences
                                            SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                                            editor.putString("password", newPassword1);
                                            editor.commit(); // Записали из editor в SharedPreferences
                                            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ChangePasswordActivity.this, getString(R.string.password_update_error_please_try_again), Toast.LENGTH_LONG).show();
                                            //Toast.makeText(ChangePasswordActivity.this, "Ошибка обновления пароля   " + result, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        // Если значения не совпадают, показываем сообщение об ошибке
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.the_passwords_do_not_match), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Если какое-то из полей пустое, показываем сообщение об ошибке
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.fill_in_all_the_fields), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    // Функция проверки авторизации пользователя
    public static String userChangePassword(String email, String password, String newPassword) {
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/handlers_api/change_password.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "email=" + email + "&password=" + password + "&newPassword=" + newPassword;
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
