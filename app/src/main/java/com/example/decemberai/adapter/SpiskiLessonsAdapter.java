package com.example.decemberai.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.decemberai.R;
import com.example.decemberai.model.ChatPage;
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
        holder.spiskiLessonsTitle.setTextColor(Color.parseColor(spiskiLessons.get(currentPosition).getColorText()));
        holder.spiskiLessonsBackGround.setBackgroundColor(Color.parseColor(spiskiLessons.get(currentPosition).getColorFon()));

        int imageId = context.getResources().getIdentifier(spiskiLessons.get(currentPosition).getImg(), "drawable", context.getPackageName());// Получаем айди картинки по названию
        holder.spiskiLessonsImage.setImageResource(imageId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Действия которые будут выполняться по клику на какую либо из Тем(в списке  в LrssonsFragment.java)
                Intent intent = new Intent(context, ChatPage.class);// Создаем условие для переадрессации на LessonsPage

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.spiskiLessonsImage, "chatAnimations"));

                intent.putExtra("spiskiChatBackGround", Color.parseColor(spiskiLessons.get(currentPosition).getColorFon()));
                intent.putExtra("spiskiChatColorText", Color.parseColor(spiskiLessons.get(currentPosition).getColorText()));
                intent.putExtra("spiskiChatImageId", imageId);
                intent.putExtra("spiskiChatTitle", spiskiLessons.get(currentPosition).getTitle());
                intent.putExtra("chatPageLevel", spiskiLessons.get(currentPosition).getLevel());
                intent.putExtra("spiskiChatId", spiskiLessons.get(currentPosition).getLessonsId());
                intent.putExtra("assistantId", spiskiLessons.get(currentPosition).getAssistantId());
                intent.putExtra("typeOfChat", "lessons");


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
        ImageView spiskiLessonsImage;

        public SpiskiLessonsViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiLessonsTitle = itemView.findViewById(R.id.spiskiLessonsTitle); // ссылка на него из дизайна
            spiskiLessonsBackGround = itemView.findViewById(R.id.spiskiLessonsBackGround);
            spiskiLessonsImage = itemView.findViewById(R.id.spiskiLessonsImageSmall);
        }
    }

}
