package com.example.decemberai.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decemberai.R;

public class LessonsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_page);


        ConstraintLayout spiskiLessonsBackGround = findViewById(R.id.lessonsPageContainer);// Создаем переменные которые ссылаются на объекты из дизайна привязыванием их по айди
        ImageView spiskiLessonsImageBigId = findViewById(R.id.lessonsPageImage);
        TextView spiskiLessonsTitle = findViewById(R.id.lessonsPageTitle);
        TextView lessonsPageLevel = findViewById(R.id.lessonsPageLevel);


        spiskiLessonsBackGround.setBackgroundColor(getIntent().getIntExtra("spiskiLessonsBackGround", 0)); // Получаем переданные параметры из адаптера и устанавливаем их значение куда нам нужно
        spiskiLessonsImageBigId.setImageResource(getIntent().getIntExtra("spiskiLessonsImageBigId", 0));
        spiskiLessonsTitle.setText(getIntent().getStringExtra("spiskiLessonsTitle"));
        lessonsPageLevel.setText(getIntent().getStringExtra("lessonsPageLevel"));


        int id_lessons = getIntent().getIntExtra("spiskiLessonsId", 0);
        add_view_item_lessons(id_lessons);






    }

    public void add_view_item_lessons(int id_lessons){
        User.lessons_item_id.add(id_lessons); // Записываем айди лекции в просмотренные
        Toast.makeText(this, "Добавлено в просмотренные :)", Toast.LENGTH_LONG).show();
    }
}