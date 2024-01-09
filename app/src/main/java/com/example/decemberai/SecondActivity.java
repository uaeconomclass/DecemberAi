package com.example.decemberai;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second); // Устанавливаем макет для SecondActivity


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_lessons:
                    // Обработка нажатия на Уроки
                    startActivity(new Intent(SecondActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_practice:
                    // Обработка нажатия на Практика
                    startActivity(new Intent(SecondActivity.this, SecondActivity.class));
                    return true;
                case R.id.menu_progress:
                    // Обработка нажатия на Прогресс
                    return true;
                case R.id.menu_profile:
                    // Обработка нажатия на Профиль
                    return true;
                default:
                    return false;
            }


        });





    }
}
