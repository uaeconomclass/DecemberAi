package com.example.decemberai;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_lessons:
                    // Обработка нажатия на Уроки
                    return true;
                case R.id.menu_practice:
                    // Обработка нажатия на Практика
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