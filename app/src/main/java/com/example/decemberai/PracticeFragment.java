package com.example.decemberai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.decemberai.adapter.SpiskiLessonsAdapter;
import com.example.decemberai.adapter.SpiskiPracticeAdapter;
import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {
    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    // Еще Нужны файлы SpiskiPractice.java, SpiskiPracticeAdapter.java, spiski_practice_adapter.xml
    RecyclerView spiskiPracticeRecycler; // Объект на основе класса RecyclerView
    SpiskiPracticeAdapter spiskiPracticeAdapter;// Объект на основе класса SpiskiLessonsAdapter

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------


        // Установка списков текстов для Lessons ч2 Начало -----------------------------------------

        List<SpiskiPractice> spiskiPracticeList = new ArrayList<>();//Создаем список, каждый элемент которого будет на основе класса SpiskiPractice называется он spiskiPracticeList и выделяем под него память ArrayList<>
        spiskiPracticeList.add(new SpiskiPractice(11, "Лекция 1")); // Создаем объект на основе класса SpiskiPractice и указываем айди и Название(можно расширить поля)
        spiskiPracticeList.add(new SpiskiPractice(12, "Лекция 2"));
        spiskiPracticeList.add(new SpiskiPractice(13, "Лекция 3"));
        spiskiPracticeList.add(new SpiskiPractice(14, "Лекция 4"));
        spiskiPracticeList.add(new SpiskiPractice(15, "Лекция 5"));
        spiskiPracticeList.add(new SpiskiPractice(16, "Лекция 6"));
        spiskiPracticeList.add(new SpiskiPractice(17, "Лекция 7"));
        spiskiPracticeList.add(new SpiskiPractice(18, "Лекция 8"));
        spiskiPracticeList.add(new SpiskiPractice(19, "Лекция 9"));

        setSpiskiPracticeRecycler(view, spiskiPracticeList); // Вызываем наш метод и передаем туда список

        // Установка списков текстов для Lessons ч2 Конец ------------------------------------------



        return view;
    }


    // Установка списков текстов для Lessons ч3 Начало -------------------------------------------------


    private void setSpiskiPracticeRecycler(View view, List<SpiskiPractice> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);// Задаем стилей для списка
        spiskiPracticeRecycler = view.findViewById(R.id.spiskiPracticeRecycler); // Устанавливаем ссылку на наш дизайн
        spiskiPracticeRecycler.setLayoutManager(layoutManager); // Подключаем стили прописанные ранее к spiskiPracticeRecycler

        spiskiPracticeAdapter = new SpiskiPracticeAdapter(requireContext(), categoryList); // выделяем память для адаптера и передаем в конструктор параметры
        spiskiPracticeRecycler.setAdapter(spiskiPracticeAdapter); // Устанавливаем Адаптер к нашему spiskiPracticeRecycler
    }
    // Установка списков текстов для Lessons ч3 Конец -------------------------------------------------

}