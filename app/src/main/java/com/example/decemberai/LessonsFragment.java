package com.example.decemberai;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.decemberai.adapter.SpiskiLessonsAdapter;
import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LessonsFragment extends Fragment {
    //Параметры текущего пользователя тестово здесь
    //String userLevel = "Новичок";//пока тестируем на Новичке
    String userLevel = User.userLevel;



    RecyclerView spiskiLessonsRecycler; // Объект на основе класса RecyclerView
    static SpiskiLessonsAdapter spiskiLessonsAdapter;// Объект на основе класса SpiskiLessonsAdapter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------


        User.clearSelectSpiskiLessonsList(); // Стираем переменную для временного хранения
        User.addAllSelectSpiskiLessonsList(User.fullSpiskiLessonsList);  // Записываем в неё полный список

        setSpiskiLessonsRecycler(view, User.selectSpiskiLessonsList); // Вызываем наш метод и передаем туда список

        showCoursesByCategoryForHome(userLevel);

        return view;
    }


    private void setSpiskiLessonsRecycler(View view, List<SpiskiLessons> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);// Задаем стилей для списка
        spiskiLessonsRecycler = view.findViewById(R.id.spiskiLessonsRecycler); // Устанавливаем ссылку на наш дизайн
        spiskiLessonsRecycler.setLayoutManager(layoutManager); // Подключаем стили прописанные ранее к spiskiLessonsRecycler

        spiskiLessonsAdapter = new SpiskiLessonsAdapter(requireContext(), categoryList); // выделяем память для адаптера и передаем в конструктор параметры
        spiskiLessonsRecycler.setAdapter(spiskiLessonsAdapter); // Устанавливаем Адаптер к нашему spiskiLessonsRecycler
    }


    public static void showCoursesByCategoryForHome(String userLevel){

        List<SpiskiLessons> filterSpiski = new ArrayList<>(); // выполним фильтрацию и все необходимые курсы поместим в этот список filterSpiski
        for (SpiskiLessons c : User.selectSpiskiLessonsList){// цикл
            // Закоментил что бы небыло фильтрации по уровню пользователя для Лекций
            // if(Objects.equals(c.getLevel(), userLevel)) // Проверяем уровень если нужный то добавляем в список filterSpiski
                filterSpiski.add(c);
        }

        User.clearSelectSpiskiLessonsList(); //  очистили временное хранение
        User.addAllSelectSpiskiLessonsList(filterSpiski);// добавили в временное хранение отфильтрованные элементы
            spiskiLessonsAdapter.notifyDataSetChanged(); // обновляем данные в адапторе
    }


}