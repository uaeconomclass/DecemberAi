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
import com.example.decemberai.model.LessonsPage;
import com.example.decemberai.model.PracticePage;
import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;

import java.util.List;

public class SpiskiPracticeAdapter extends RecyclerView.Adapter<SpiskiPracticeAdapter.SpiskiPracticeViewHolder> {

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
        holder.spiskiPracticeBackGround.setBackgroundColor(Color.parseColor(spiskiPractice.get(currentPosition).getColor()));

        int imageSmallId = context.getResources().getIdentifier(spiskiPractice.get(currentPosition).getImgSmall(), "drawable", context.getPackageName());// Получаем айди картинки по названию
        holder.spiskiPracticeImageSmall.setImageResource(imageSmallId);
        int imageBigId = context.getResources().getIdentifier(spiskiPractice.get(currentPosition).getImgBig(), "drawable", context.getPackageName());// Получаем айди картинки по названию

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Действия которые будут выполняться по клику на какую либо из Тем(в списке  в LrssonsFragment.java)
                Intent intent = new Intent(context, PracticePage.class);// Создаем условие для переадрессации на LessonsPage

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.spiskiPracticeImageSmall, "practiceAnimations"));

                intent.putExtra("spiskiPracticeBackGround", Color.parseColor(spiskiPractice.get(currentPosition).getColor()));
                intent.putExtra("spiskiPracticeImageBigId", imageBigId);
                intent.putExtra("spiskiPracticeTitle", spiskiPractice.get(currentPosition).getTitle());
                intent.putExtra("practicePageLevel", spiskiPractice.get(currentPosition).getLevel());
                intent.putExtra("spiskiPracticeId", spiskiPractice.get(currentPosition).getId());
                intent.putExtra("poleZapasnoe", spiskiPractice.get(currentPosition).getPoleZapasnoe());





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
        ImageView spiskiPracticeImageSmall;
        public SpiskiPracticeViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiPracticeTitle = itemView.findViewById(R.id.spiskiPracticeTitle); // ссылка на него из дизайна
            spiskiPracticeBackGround = itemView.findViewById(R.id.spiskiPracticeBackGround);
            spiskiPracticeImageSmall = itemView.findViewById(R.id.spiskiPracticeImageSmall);
        }
    }

}
