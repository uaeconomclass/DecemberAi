package com.example.decemberai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    Button button2, button_register;

    EditText emailViewById, passwordViewById;
    String emailInsert, passwordInsert;
    SharedPreferences sp; // Переменная для SharedPreferences

    Button btnLogIn, btnRegister;

    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button2 = findViewById(R.id.button_test2);
        button_register = findViewById(R.id.button_register);
        emailViewById = findViewById(R.id.editText);
        passwordViewById = findViewById(R.id.editText2);
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences

        btnLogIn = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);
        root = findViewById(R.id.root_activity_register);


        btnRegister.setOnClickListener(new View.OnClickListener() {//Проверить авторизацию
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {//Проверить авторизацию
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад

            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {// Регистрация
            @Override
            public void onClick(View v) {
                emailInsert = emailViewById.getText().toString(); // Записываем в переменную то что ввел пользователь
                passwordInsert = passwordViewById.getText().toString();


                SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                editor.putString("email", emailInsert); // в editor Сохраняем данные в формате (ключь, значение)
                editor.putString("password", passwordInsert);
                editor.commit(); // Записали из editor в SharedPreferences
                Toast.makeText(RegisterActivity.this, "Информация сохранена", Toast.LENGTH_LONG).show();


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

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // Создаем кнопку закрытия всплывающего окна
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();// По клику на отменить скрываем всплывающее окно
            }
        });

        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(emailRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Snackbar.make(root,"Enter your email address", Snackbar.LENGTH_SHORT).show(); //Если не ввели то всплывающее окно
                    return; // и выходим
                }
                if(TextUtils.isEmpty(nameRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Snackbar.make(root,"Enter your name", Snackbar.LENGTH_SHORT).show(); //Если не ввели то всплывающее окно
                    return; // и выходим
                }
                if(TextUtils.isEmpty(phoneRegister.getText().toString())){ //Проверяем ввел ли что-нибудь пользователь
                    Snackbar.make(root,"Enter your phone number", Snackbar.LENGTH_SHORT).show(); //Если не ввели то всплывающее окно
                    return; // и выходим
                }
                if(passwordRegister.getText().toString().length() < 5 ){ //Проверяем что бы пароль был длиннее 5
                    Snackbar.make(root,"Enter a password, that is more than 5 characters long", Snackbar.LENGTH_SHORT).show(); //Если не ввели то всплывающее окно
                    return; // и выходим
                }

                //На этом этапе проверки пройдены и делаем Регистрацию пользователя
                //Вызываем функцию передачи данных на сервер
                // userRegister(nameRegister.getText().toString(), emailRegister.getText().toString(), phoneRegister.getText().toString(), passwordRegister.getText().toString())
                // Если вернулся ОК записываем данные в SharedPreferences
                SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                editor.putString("email", emailRegister.getText().toString()); // в editor Сохраняем данные в формате (ключь, значение)
                editor.putString("password", passwordRegister.getText().toString());
                editor.commit(); // Записали из editor в SharedPreferences

                User user = new User();
                user.setUserName(nameRegister.getText().toString());
                user.setUserEmail(emailRegister.getText().toString());
                user.setUserPhone(phoneRegister.getText().toString());
                user.setUserPassword(passwordRegister.getText().toString());
            }
        });

    }
}