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
import java.util.Objects;


public class LessonsFragment extends Fragment {
    //Параметры текущего пользователя тестово здесь
    String userLevel = "Новичок";




    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    // Еще Нужны файлы SpiskiLessons.java, SpiskiLessonsAdapter.java, spiski_lessons_adapter.xml
    RecyclerView spiskiLessonsRecycler; // Объект на основе класса RecyclerView
    static SpiskiLessonsAdapter spiskiLessonsAdapter;// Объект на основе класса SpiskiLessonsAdapter
    static List<SpiskiLessons> spiskiLessonsList = new ArrayList<>();//Создаем список, каждый элемент которого будет на основе класса SpiskiLessons называется он spiskiLessonsList и выделяем под него память ArrayList<>
    static List<SpiskiLessons> fullSpiskiLessonsList = new ArrayList<>();// переменная для полного начального варианта списка

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------
        spiskiLessonsList.clear();
        fullSpiskiLessonsList.clear();

        // Установка списков текстов для Lessons ч2 Начало -----------------------------------------

        spiskiLessonsList.add(new SpiskiLessons(1, 0001, "Утренняя кофемания", "ic_cofe_day_small2","ic_cofe_day_small2", "Новичок", "#424345" )); // Создаем объект на основе класса SpiskiLessons и указываем айди и Название(можно расширить поля)
        spiskiLessonsList.add(new SpiskiLessons(2, 0002, "Разговорчики \nс подругой", "ic_phyton_small","ic_phyton_small", "Новичок", "#9FA52D" ));
        spiskiLessonsList.add(new SpiskiLessons(3, 0003, "Утренняя кофемания", "ic_cofe_day_small2","ic_cofe_day_small2", "Новичок", "#424345" )); // Создаем объект на основе класса SpiskiLessons и указываем айди и Название(можно расширить поля)
        spiskiLessonsList.add(new SpiskiLessons(4, 0004, "Разговорчики \nс подругой", "ic_phyton_small", "ic_phyton_small", "<Бывалый>", "#9FA52D" ));
        spiskiLessonsList.add(new SpiskiLessons(5, 0005, "Утренняя кофемания", "ic_cofe_day_small2","ic_cofe_day_small2", "Новичок", "#424345" )); // Создаем объект на основе класса SpiskiLessons и указываем айди и Название(можно расширить поля)
        spiskiLessonsList.add(new SpiskiLessons(6, 0006, "Разговорчики \nс подругой", "ic_phyton_small", "ic_phyton_small", "<Новичок>", "#9FA52D" ));





        setSpiskiLessonsRecycler(view, spiskiLessonsList); // Вызываем наш метод и передаем туда список

        // Установка списков текстов для Lessons ч2 Конец ------------------------------------------
        fullSpiskiLessonsList.clear();
        fullSpiskiLessonsList.addAll(spiskiLessonsList); //заполняем переменную для сохранения неизменного начального варианта списка
        showCoursesByCategory(userLevel);

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


    public static void showCoursesByCategory(String userLevel){
        spiskiLessonsList.clear(); // стираем весь список
        spiskiLessonsList.addAll(fullSpiskiLessonsList); // заполняем его из переменной которая хранит первоначальные параметры
        List<SpiskiLessons> filterSpiski = new ArrayList<>(); // выполним фильтрацию и все необходимые курсы поместим в этот список filterCourses
        for (SpiskiLessons c : spiskiLessonsList){// цикл
            if(Objects.equals(c.getLevel(), userLevel)) // Проверяем уровень если нужный то добавляем в список filterCourses
                filterSpiski.add(c);
        }

        spiskiLessonsList.clear(); // очистили наш первоначальный список
        spiskiLessonsList.addAll(filterSpiski);// добавили в первоначальный список отфильтрованные элементы

        spiskiLessonsAdapter.notifyDataSetChanged(); // обновляем данные в адапторе

    }


}