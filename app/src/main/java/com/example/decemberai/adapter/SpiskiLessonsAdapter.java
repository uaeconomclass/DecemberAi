package com.example.decemberai.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decemberai.R;
import com.example.decemberai.model.LessonsPage;
import com.example.decemberai.model.SpiskiLessons;

import java.util.List;

public class SpiskiLessonsAdapter extends RecyclerView.Adapter<SpiskiLessonsAdapter.SpiskiLessonsViewHolder> {

    Context context; // Сюда передадим Страницу на которой все должно быть выведено
    List<SpiskiLessons> spiskiLessons;// Сюда передадим Список всех категорий что должны быть выведены(все товары)

    public SpiskiLessonsAdapter (Context context, List<SpiskiLessons> spiskiLessons){ //Конструктор через который установим параметры
            this.context = context;
            this.spiskiLessons = spiskiLessons;
    }


    @NonNull
    @Override

    public SpiskiLessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View spiskiLessonsItems = LayoutInflater.from(context).inflate(R.layout.spiski_lessons_item, parent, false); // указываем какой дизайн
            return new SpiskiLessonsViewHolder(spiskiLessonsItems); // указываем с какими элементами будем работать
    }



    @Override
    public void onBindViewHolder(@NonNull SpiskiLessonsAdapter.SpiskiLessonsViewHolder holder,  int position) { // создали holder на основе вложенного класса SpiskiLessonsViewHolder
        final int currentPosition = position;  // Объявили final переменную для position так как в новых версиях андроида что бы переменная была доступна внутри вложенной функции она должна быть final

        holder.spiskiLessonsTitle.setText(spiskiLessons.get(currentPosition).getTitle());// через объект holder обращаемся к  spiskiLessonsTitle и устанавливаем текст для этого поля
        holder.spiskiLessonsBackGround.setBackgroundColor(Color.parseColor(spiskiLessons.get(currentPosition).getColor()));

        int imageSmallId = context.getResources().getIdentifier(spiskiLessons.get(currentPosition).getImgSmall(), "drawable", context.getPackageName());// Получаем айди картинки по названию
        holder.spiskiLessonsImageSmall.setImageResource(imageSmallId);
        int imageBigId = context.getResources().getIdentifier(spiskiLessons.get(currentPosition).getImgBig(), "drawable", context.getPackageName());// Получаем айди картинки по названию

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Действия которые будут выполняться по клику на какую либо из Тем(в списке  в LrssonsFragment.java)
                Intent intent = new Intent(context, LessonsPage.class);// Создаем условие для переадрессации на LessonsPage

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.spiskiLessonsImageSmall, "lessonsAnimations"));

                intent.putExtra("spiskiLessonsBackGround", Color.parseColor(spiskiLessons.get(currentPosition).getColor()));
                intent.putExtra("spiskiLessonsImageBigId", imageBigId);
                intent.putExtra("spiskiLessonsTitle", spiskiLessons.get(currentPosition).getTitle());
                intent.putExtra("lessonsPageLevel", spiskiLessons.get(currentPosition).getLevel());
                intent.putExtra("spiskiLessonsId", spiskiLessons.get(currentPosition).getId());
                intent.putExtra("poleZapasnoe", spiskiLessons.get(currentPosition).getPoleZapasnoe());





                context.startActivity(intent, options.toBundle()); // Сама переадрессация и анимация
            }
        });
    }

    @Override
    public int getItemCount() {
            return spiskiLessons.size();
            }

    public static final class SpiskiLessonsViewHolder extends RecyclerView.ViewHolder {

        TextView spiskiLessonsTitle; // с каким объектом работаем
        LinearLayout spiskiLessonsBackGround;
        ImageView spiskiLessonsImageSmall;

        public SpiskiLessonsViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiLessonsTitle = itemView.findViewById(R.id.spiskiLessonsTitle); // ссылка на него из дизайна
            spiskiLessonsBackGround = itemView.findViewById(R.id.spiskiLessonsBackGround);
            spiskiLessonsImageSmall = itemView.findViewById(R.id.spiskiLessonsImageSmall);
        }
    }

}
