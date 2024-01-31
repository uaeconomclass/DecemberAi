package com.example.decemberai.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decemberai.R;

public class PracticePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_page);

        ConstraintLayout spiskiPracticeBackGround = findViewById(R.id.practicePageContainer);// Создаем переменные которые ссылаются на объекты из дизайна привязыванием их по айди
        ImageView spiskiPracticeImageBigId = findViewById(R.id.practicePageImage);
        TextView spiskiPracticeTitle = findViewById(R.id.practicePageTitle);
        TextView practicePageLevel = findViewById(R.id.practicePageLevel);

        spiskiPracticeBackGround.setBackgroundColor(getIntent().getIntExtra("spiskiPracticeBackGround", 0)); // Получаем переданные параметры из адаптера и устанавливаем их значение куда нам нужно
        spiskiPracticeImageBigId.setImageResource(getIntent().getIntExtra("spiskiPracticeImageBigId", 0));
        spiskiPracticeTitle.setText(getIntent().getStringExtra("spiskiPracticeTitle"));
        practicePageLevel.setText(getIntent().getStringExtra("practicePageLevel"));

        int id_practice = getIntent().getIntExtra("spiskiPracticeId", 0);
        add_view_item_practice(id_practice);
    }
    public void add_view_item_practice(int id_practice){
        User.practice_item_id.add(id_practice); // Записываем айди лекции в просмотренные
        Toast.makeText(this, "Добавлено в просмотренные :)", Toast.LENGTH_LONG).show();
    }
}