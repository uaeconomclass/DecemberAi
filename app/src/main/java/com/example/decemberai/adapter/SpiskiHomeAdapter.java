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

public class SpiskiHomeAdapter  extends RecyclerView.Adapter<SpiskiHomeAdapter.SpiskiHomeViewHolder> {

    Context context; // Сюда передадим Страницу на которой все должно быть выведено
    List<SpiskiLessons> spiskiHome;// Сюда передадим Список всех категорий что должны быть выведены(все товары)

    public SpiskiHomeAdapter (Context context, List<SpiskiLessons> spiskiLessons){ //Конструктор через который установим параметры
        this.context = context;
        this.spiskiHome = spiskiLessons;
    }


    @NonNull
    @Override

    public SpiskiHomeAdapter.SpiskiHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View spiskiLessonsItems = LayoutInflater.from(context).inflate(R.layout.spiski_home_item, parent, false); // указываем какой дизайн
        return new SpiskiHomeAdapter.SpiskiHomeViewHolder(spiskiLessonsItems); // указываем с какими элементами будем работать
    }



    @Override
    public void onBindViewHolder(@NonNull SpiskiHomeAdapter.SpiskiHomeViewHolder holder,  int position) { // создали holder на основе вложенного класса SpiskiLessonsViewHolder
        final int currentPosition = position;  // Объявили final переменную для position так как в новых версиях андроида что бы переменная была доступна внутри вложенной функции она должна быть final

        holder.spiskiHomeTitle.setText(spiskiHome.get(currentPosition).getTitle());// через объект holder обращаемся к  spiskiLessonsTitle и устанавливаем текст для этого поля
        //holder.spiskiLessonsBackGround.setBackgroundColor(Color.parseColor(spiskiLessons.get(currentPosition).getColor()));

        int imageId = context.getResources().getIdentifier(spiskiHome.get(currentPosition).getImg(), "drawable", context.getPackageName());// Получаем айди картинки по названию

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Действия которые будут выполняться по клику на какую либо из Тем(в списке  в LrssonsFragment.java)
                Intent intent = new Intent(context, ChatPage.class);// Создаем условие для переадрессации на LessonsPage

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.spiskiHomeTitle, "chatAnimations"));

                intent.putExtra("spiskiChatBackGround", Color.parseColor(spiskiHome.get(currentPosition).getColorFon()));
                intent.putExtra("spiskiChatColorText", Color.parseColor(spiskiHome.get(currentPosition).getColorText()));
                intent.putExtra("spiskiChatImageId", imageId);
                intent.putExtra("spiskiChatTitle", spiskiHome.get(currentPosition).getTitle());
                intent.putExtra("chatPageLevel", spiskiHome.get(currentPosition).getLevel());
                intent.putExtra("spiskiChatId", spiskiHome.get(currentPosition).getLessonsId());
                intent.putExtra("typeOfChat", "lessons");//Для сохранения в Лекции





                context.startActivity(intent, options.toBundle()); // Сама переадрессация и анимация
            }
        });
    }

    @Override
    public int getItemCount() {
        return spiskiHome.size();
    }

    public static final class SpiskiHomeViewHolder extends RecyclerView.ViewHolder {

        TextView spiskiHomeTitle; // с каким объектом работаем


        public SpiskiHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiHomeTitle = itemView.findViewById(R.id.spiskiHomeTitle); // ссылка на него из дизайна

        }
    }

}