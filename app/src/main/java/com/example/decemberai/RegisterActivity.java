package com.example.decemberai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.decemberai.model.User;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences sp; // Переменная для SharedPreferences
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences
        btnLogin = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {// Клик по кнопке Регистрация
            @Override
            public void onClick(View v) {

                showRegisterWindow();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {// Клик по кнопке Регистрация
            @Override
            public void onClick(View v) {

                showLoginWindow();
            }
        });



    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("register");// Заголовок
        dialog.setMessage("Enter all the registration details");//Текст

        LayoutInflater inflater = LayoutInflater.from(this); //Создали просто объект на основе класса LayoutInflater
        View register_window = inflater.inflate(R.layout.register_window,null); //Благодаря объекту inflater получаем наш шаблон register_window и записываем его в переменную register_window
        dialog.setView(register_window); //и дальше устанавливаем наш шаблон в качестве всплывающего окна

        final EditText emailRegister = register_window.findViewById(R.id.emailRegister);// Получаем значения полей со всплывающего окна
        final EditText passwordRegister = register_window.findViewById(R.id.passwordRegister);
        final EditText nameRegister = register_window.findViewById(R.id.nameRegister);
        final EditText phoneRegister = register_window.findViewById(R.id.phoneRegister);




        Button registerButton = register_window.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, "Enter your name", Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(emailRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                        Toast.makeText(RegisterActivity.this, "Enter your email address", Toast.LENGTH_LONG).show();//Если не ввели то всплывающее окно
                } else if(TextUtils.isEmpty(phoneRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, "Enter your phone number", Toast.LENGTH_LONG).show();
                } else if(passwordRegister.getText().toString().length() < 5 ){ //Проверяем что бы пароль был длиннее 5
                    Toast.makeText(RegisterActivity.this, "Enter a password, that is more than 5 characters long", Toast.LENGTH_LONG).show();
                } else {

                    //На этом этапе проверки пройдены и делаем Регистрацию пользователя
                    //Вызываем функцию передачи данных на сервер
                    // userRegister(nameRegister.getText().toString(), emailRegister.getText().toString(), phoneRegister.getText().toString(), passwordRegister.getText().toString())
                    // Если вернулся ОК записываем данные в SharedPreferences
                    SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                    editor.putString("email", emailRegister.getText().toString()); // в editor Сохраняем данные в формате (ключь, значение)
                    editor.putString("password", passwordRegister.getText().toString());
                    editor.commit(); // Записали из editor в SharedPreferences


                    User user = User.getInstance();//Создаем объект user в этой функции, но подгружаем в него объект из класса User что бы редактировать не сдешнего Юзера а Глобального, созданного в User
                    user.setUserName(nameRegister.getText().toString()); // Записываем жанные в Юзера приложения
                    user.setUserEmail(emailRegister.getText().toString());
                    user.setUserPhone(phoneRegister.getText().toString());
                    user.setUserPassword(passwordRegister.getText().toString());

                    // После успешной авторизации перекидываем пользователя на MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // Создаем кнопку закрытия всплывающего окна
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();// По клику на отменить скрываем всплывающее окно
            }
        });


        dialog.show();
    }


    private void showLoginWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("login");// Заголовок
        dialog.setMessage("Enter the login information");//Текст

        LayoutInflater inflater = LayoutInflater.from(this); //Создали просто объект на основе класса LayoutInflater
        View register_window = inflater.inflate(R.layout.login_window,null); //Благодаря объекту inflater получаем наш шаблон register_window и записываем его в переменную register_window
        dialog.setView(register_window); //и дальше устанавливаем наш шаблон в качестве всплывающего окна

        final EditText emailLogin = register_window.findViewById(R.id.emailLogin);// Получаем значения полей со всплывающего окна
        final EditText passwordLogin = register_window.findViewById(R.id.passwordLogin);


        Button loginButton = register_window.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailLogin.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Toast.makeText(RegisterActivity.this, "Enter your email address", Toast.LENGTH_LONG).show();//Если не ввели то всплывающее окно
                } else if(passwordLogin.getText().toString().length() < 5 ){ //Проверяем что бы пароль был длиннее 5
                    Toast.makeText(RegisterActivity.this, "Enter a password, that is more than 5 characters long", Toast.LENGTH_LONG).show();
                } else {

                    //На этом этапе проверки пройдены и делаем Авторизацию пользователя
                    //Вызываем функцию передачи данных на сервер
                    // userLogin(emailRegister.getText().toString(), passwordRegister.getText().toString())
                    // Если вернулся ОК записываем данные в SharedPreferences и все данные пользователя
                    SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                    editor.putString("email", emailLogin.getText().toString()); // в editor Сохраняем данные в формате (ключь, значение)
                    editor.putString("password", passwordLogin.getText().toString());
                    editor.commit(); // Записали из editor в SharedPreferences

                    User user = User.getInstance();//В user помещаем глобального Юзера
                    user.setUserName("Шаблон имени"); // Записываем жанные в Юзера приложения
                    user.setUserEmail(emailLogin.getText().toString());
                    user.setUserPhone("Шаблон телефона");
                    user.setUserPassword(passwordLogin.getText().toString());

                    // После успешной авторизации перекидываем пользователя на MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // Создаем кнопку закрытия всплывающего окна
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();// По клику на отменить скрываем всплывающее окно
            }
        });



        dialog.show();

    }

}