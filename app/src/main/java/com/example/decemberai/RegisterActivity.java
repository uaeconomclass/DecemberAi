package com.example.decemberai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.decemberai.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlinx.coroutines.CoroutineScope;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences sp; // Переменная для SharedPreferences
    Button btnLogin, btnRegister, buttonTest;

    Dialog dialogRegister,  dialogLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences
        btnLogin = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);

        dialogRegister = new Dialog(RegisterActivity.this);
        dialogLogin = new Dialog(RegisterActivity.this);



        btnRegister.setOnClickListener(new View.OnClickListener() {// Клик по кнопке Регистрация
            @Override
            public void onClick(View v) {
                showRegisterWindow(); // Показываем окно регистрации
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {// Клик по кнопке Регистрация
            @Override
            public void onClick(View v) {
                showLoginWindow(); //Показываем окно Залогинивания
            }
        });

    }


    private void redirectToMainActivity() {
        // После успешной авторизации перекидываем пользователя на MainActivity
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
    }


    private void showRegisterWindow() {

        dialogRegister.setContentView(R.layout.register_window);
        dialogRegister.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogRegister.setCancelable(true);

        Button registerButton = dialogRegister.findViewById(R.id.registerButton);
        Button canselRegisterButton = dialogRegister.findViewById(R.id.canselRegisterButton);
        final EditText nameRegister = dialogRegister.findViewById(R.id.nameRegister);
        final EditText emailRegister = dialogRegister.findViewById(R.id.emailRegister);
        final EditText phoneRegister = dialogRegister.findViewById(R.id.phoneRegister);
        final EditText passwordRegister = dialogRegister.findViewById(R.id.passwordRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nameRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_your_name), Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(emailRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_your_email_address), Toast.LENGTH_LONG).show();//Если не ввели то всплывающее окно
                } else if(TextUtils.isEmpty(phoneRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_your_phone_number), Toast.LENGTH_LONG).show();
                } else if(passwordRegister.getText().toString().length() < 5 ){ //Проверяем что бы пароль был длиннее 5
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_password_that_is_more_than_5_characters_long), Toast.LENGTH_LONG).show();
                } else {

                    //На этом этапе проверки пройдены и делаем Регистрацию пользователя
                    //Вызываем функцию передачи данных на сервер
                    Executor executor2 = Executors.newSingleThreadExecutor();
                    executor2.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = userRegister(nameRegister.getText().toString(), emailRegister.getText().toString(), phoneRegister.getText().toString(), passwordRegister.getText().toString());
                            if (Objects.equals(result, "true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registration_was_successful), Toast.LENGTH_LONG).show();

                                        // Если вернулся ОК записываем данные в SharedPreferences
                                        SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                                        editor.putString("email", emailRegister.getText().toString()); // в editor Сохраняем данные в формате (ключь, значение)
                                        editor.putString("password", passwordRegister.getText().toString());
                                        editor.commit(); // Записали из editor в SharedPreferences


                                        // После успешной авторизации перекидываем пользователя на MainActivity
                                        dialogRegister.dismiss(); // Закрываем диалоговое окно
                                        redirectToMainActivity(); // Перенаправляем пользователя на MainActivity



                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registration_error_please_try_again), Toast.LENGTH_LONG).show();
                                       // Toast.makeText(RegisterActivity.this, "Ошибка регистрации   " + result, Toast.LENGTH_LONG).show();
                                        // Ваш код для неудачной авторизации здесь...
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });

        canselRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRegister.dismiss();
            }
        });

        dialogRegister.show();

    }

    public static String userRegister(String name, String email, String phone, String password) {
        try {
            // URL для отправки запроса
            URL url = new URL("https://yourtalker.com/hendlers_api/register.php");

            // Создание соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Включение возможности отправки данных
            connection.setDoOutput(true);

            // Формирование тела запроса
            String postData = "name=" + name + "&email=" + email + "&phone=" + phone + "&password=" + password;
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

            // Проверка ответа сервера и возврат результата
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }






    private void showLoginWindow() {
        dialogLogin.setContentView(R.layout.login_window);
        dialogLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLogin.setCancelable(true);

        Button loginButton = dialogLogin.findViewById(R.id.loginButton);
        Button canselLoginButton = dialogLogin.findViewById(R.id.canselLoginButton);
        final EditText emailLogin = dialogLogin.findViewById(R.id.emailLogin);
        final EditText passwordLogin = dialogLogin.findViewById(R.id.passwordLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(emailLogin.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_your_email_address), Toast.LENGTH_LONG).show();//Если не ввели то всплывающее окно
                } else if(passwordLogin.getText().toString().length() < 5 ){ //Проверяем что бы пароль был длиннее 5
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_password_that_is_more_than_5_characters_long), Toast.LENGTH_LONG).show();
                } else {

                    //На этом этапе проверки пройдены и делаем Авторизацию пользователя
                    //Вызываем функцию передачи данных на сервер
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = userLogin(emailLogin.getText().toString(), passwordLogin.getText().toString());
                            if (Objects.equals(result, "true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.authorization_was_successful), Toast.LENGTH_LONG).show();
                                        // Код для успешной авторизации здесь...
                                        // Если вернулся ОК записываем данные в SharedPreferences и все данные пользователя
                                        SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                                        editor.putString("email", emailLogin.getText().toString()); // в editor Сохраняем данные в формате (ключь, значение)
                                        editor.putString("password", passwordLogin.getText().toString());
                                        editor.commit(); // Записали из editor в SharedPreferences


                                        // После успешной авторизации перекидываем пользователя на MainActivity
                                        dialogLogin.dismiss(); // Закрываем диалоговое окно
                                        redirectToMainActivity(); // Перенаправляем пользователя на MainActivity


                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.authorization_error), Toast.LENGTH_LONG).show();
                                        // Toast.makeText(RegisterActivity.this, "Ошибка авторизации   " + result, Toast.LENGTH_LONG).show();
                                        // Ваш код для неудачной авторизации здесь...
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });

        canselLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogin.dismiss();
            }
        });

        dialogLogin.show();

    }
    // Функция авторизации пользователя
    public static String userLogin(String emailRegister, String passwordRegister) {
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

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


}