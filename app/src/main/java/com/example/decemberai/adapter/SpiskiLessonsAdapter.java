package com.example.decemberai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decemberai.R;
import com.example.decemberai.model.SpiskiLessons;

import java.util.List;

public class SpiskiLessonsAdapter extends RecyclerView.Adapter<SpiskiLessonsAdapter.SpiskiLessonsViewHolder> {

    Context context; // Сюда передадим Страницу на которой все должно быть выведено
    List<SpiskiLessons> spiskiLessons;// Сюда передадим Список всех категорий что должны быть выведены

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
    public void onBindViewHolder(@NonNull SpiskiLessonsAdapter.SpiskiLessonsViewHolder holder, int position) { // создали holder на основе вложенного класса SpiskiLessonsViewHolder
            holder.spiskiLessonsTitle.setText(spiskiLessons.get(position).getTitle());// через объект holder обращаемся к  spiskiLessonsTitle и устанавливаем текст для этого поля
    }

    @Override
    public int getItemCount() {
            return spiskiLessons.size();
            }

    public static final class SpiskiLessonsViewHolder extends RecyclerView.ViewHolder {

        TextView spiskiLessonsTitle; // с каким объектом работаем
        public SpiskiLessonsViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiLessonsTitle = itemView.findViewById(R.id.spiskiLessonsTitle); // ссылка на него из дизайна
        }
    }

}
