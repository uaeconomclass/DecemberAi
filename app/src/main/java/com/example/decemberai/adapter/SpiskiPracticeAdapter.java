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
import com.example.decemberai.model.SpiskiPractice;

import java.util.List;

public class SpiskiPracticeAdapter  extends RecyclerView.Adapter<SpiskiPracticeAdapter.SpiskiPracticeViewHolder> {

    Context context; // Сюда передадим Страницу на которой все должно быть выведено
    List<SpiskiPractice> spiskiPractice;// Сюда передадим Список всех категорий что должны быть выведены

    public SpiskiPracticeAdapter (Context context, List<SpiskiPractice> spiskiPractice){ //Конструктор через который установим параметры
        this.context = context;
        this.spiskiPractice = spiskiPractice;
    }


    @NonNull
    @Override

    public SpiskiPracticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View spiskiPracticeItems = LayoutInflater.from(context).inflate(R.layout.spiski_practice_item, parent, false); // указываем какой дизайн
        return  new SpiskiPracticeViewHolder(spiskiPracticeItems); // указываем с какими элементами будем работать
    }



    @Override
    public void onBindViewHolder(@NonNull SpiskiPracticeAdapter.SpiskiPracticeViewHolder holder, int position) { // создали holder на основе вложенного класса SpiskiPracticeViewHolder
        final int currentPosition = position;  // Объявили final переменную для position так как в новых версиях андроида что бы переменная была доступна внутри вложенной функции она должна быть final

        holder.spiskiPracticeTitle.setText(spiskiPractice.get(currentPosition).getTitle());// через объект holder обращаемся к  spiskiLessonsTitle и устанавливаем текст для этого поля
        holder.spiskiPracticeTitle.setTextColor(Color.parseColor(spiskiPractice.get(currentPosition).getColorText()));
        holder.spiskiPracticeBackGround.setBackgroundColor(Color.parseColor(spiskiPractice.get(currentPosition).getColorFon()));


        int imageId = context.getResources().getIdentifier(spiskiPractice.get(currentPosition).getImg(), "drawable", context.getPackageName());// Получаем айди картинки по названию
        holder.spiskiPracticeImage.setImageResource(imageId);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Действия которые будут выполняться по клику на какую либо из Тем(в списке  в LrssonsFragment.java)
                Intent intent = new Intent(context, ChatPage.class);// Создаем условие для переадрессации на LessonsPage

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.spiskiPracticeImage, "chatAnimations"));

                intent.putExtra("spiskiChatBackGround", Color.parseColor(spiskiPractice.get(currentPosition).getColorFon()));
                intent.putExtra("spiskiChatColorText", Color.parseColor(spiskiPractice.get(currentPosition).getColorText()));
                intent.putExtra("spiskiChatImageId", imageId);
                intent.putExtra("spiskiChatTitle", spiskiPractice.get(currentPosition).getTitle());
                intent.putExtra("chatPageLevel", spiskiPractice.get(currentPosition).getLevel());
                intent.putExtra("spiskiChatId", spiskiPractice.get(currentPosition).getPracticeId());
                intent.putExtra("assistantId", spiskiPractice.get(currentPosition).getAssistantId());
                intent.putExtra("typeOfChat", "practice");

                context.startActivity(intent, options.toBundle()); // Сама переадрессация и анимация
            }
        });
    }

    @Override
    public int getItemCount() {
        return spiskiPractice.size();
    }

    public static final class SpiskiPracticeViewHolder extends RecyclerView.ViewHolder {

        TextView spiskiPracticeTitle; // с каким объектом работаем
        LinearLayout spiskiPracticeBackGround;
        ImageView spiskiPracticeImage;
        public SpiskiPracticeViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiPracticeTitle = itemView.findViewById(R.id.spiskiPracticeTitle); // ссылка на него из дизайна
            spiskiPracticeBackGround = itemView.findViewById(R.id.spiskiPracticeBackGround);
            spiskiPracticeImage = itemView.findViewById(R.id.spiskiPracticeImageSmall);
        }
    }

}
