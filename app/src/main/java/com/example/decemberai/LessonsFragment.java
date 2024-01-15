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

import java.util.ArrayList;
import java.util.List;



public class LessonsFragment extends Fragment {

    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    RecyclerView spiskiLessonsRecycler; // Объект на основе класса RecyclerView
    SpiskiLessonsAdapter spiskiLessonsAdapter;// Объект на основе класса SpiskiLessonsAdapter

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------


        // Установка списков текстов для Lessons ч2 Начало -----------------------------------------

        List<SpiskiLessons> spiskiLessonsList = new ArrayList<>();//Создаем список, каждый элемент которого будет на основе класса SpiskiLessons называется он spiskiLessonsList и выделяем под него память ArrayList<>
        spiskiLessonsList.add(new SpiskiLessons(1, "Лекция 1")); // Создаем объект на основе класса SpiskiLessons и указываем айди и Название(можно расширить поля)
        spiskiLessonsList.add(new SpiskiLessons(2, "Лекция 2"));
        spiskiLessonsList.add(new SpiskiLessons(3, "Лекция 3"));
        spiskiLessonsList.add(new SpiskiLessons(4, "Лекция 4"));
        spiskiLessonsList.add(new SpiskiLessons(5, "Лекция 5"));
        spiskiLessonsList.add(new SpiskiLessons(6, "Лекция 6"));
        spiskiLessonsList.add(new SpiskiLessons(7, "Лекция 7"));

        setSpiskiLessonsRecycler(view, spiskiLessonsList); // Вызываем наш метод и передаем туда список

        // Установка списков текстов для Lessons ч2 Конец ------------------------------------------



        return view;
    }


    // Установка списков текстов для Lessons ч3 Начало -------------------------------------------------


    private void setSpiskiLessonsRecycler(View view, List<SpiskiLessons> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);// Задаем стилей для списка
        spiskiLessonsRecycler = view.findViewById(R.id.spiskiLessonsRecycler); // Устанавливаем ссылку на наш дизайн
        spiskiLessonsRecycler.setLayoutManager(layoutManager); // Подключаем стили прописанные ранее к spiskiLessonsRecycler

        spiskiLessonsAdapter = new SpiskiLessonsAdapter(requireContext(), categoryList); // выделяем память для адаптера и передаем в конструктор параметры
        spiskiLessonsRecycler.setAdapter(spiskiLessonsAdapter); // Устанавливаем Адаптер к нашему spiskiLessonsRecycler
    }
    // Установка списков текстов для Lessons ч3 Конец -------------------------------------------------
}