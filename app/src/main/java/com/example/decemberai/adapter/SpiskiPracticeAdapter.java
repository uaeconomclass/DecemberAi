package com.example.decemberai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decemberai.R;
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

    public SpiskiPracticeAdapter.SpiskiPracticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View spiskiPracticeItems = LayoutInflater.from(context).inflate(R.layout.spiski_practice_item, parent, false); // указываем какой дизайн
        return new SpiskiPracticeAdapter.SpiskiPracticeViewHolder(spiskiPracticeItems); // указываем с какими элементами будем работать
    }



    @Override
    public void onBindViewHolder(@NonNull SpiskiPracticeAdapter.SpiskiPracticeViewHolder holder, int position) { // создали holder на основе вложенного класса SpiskiPracticeViewHolder
        holder.spiskiPracticeTitle.setText(spiskiPractice.get(position).getTitle());// через объект holder обращаемся к  spiskiPracticeTitle и устанавливаем текст для этого поля
    }

    @Override
    public int getItemCount() {
        return spiskiPractice.size();
    }

    public static final class SpiskiPracticeViewHolder extends RecyclerView.ViewHolder {

        TextView spiskiPracticeTitle; // с каким объектом работаем
        public SpiskiPracticeViewHolder(@NonNull View itemView) {
            super(itemView);

            spiskiPracticeTitle = itemView.findViewById(R.id.spiskiPracticeTitle); // ссылка на него из дизайна
        }
    }

}
