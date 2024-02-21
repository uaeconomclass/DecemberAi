package com.example.decemberai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Button button2, button_register;

    EditText emailViewById, passwordViewById;
    String emailInsert, passwordInsert;
    SharedPreferences sp; // Переменная для SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button2 = findViewById(R.id.button_test2);
        button_register = findViewById(R.id.button_register);
        emailViewById = findViewById(R.id.editText);
        passwordViewById = findViewById(R.id.editText2);
        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences

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
}