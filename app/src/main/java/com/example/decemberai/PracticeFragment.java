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
import java.util.Objects;

public class PracticeFragment extends Fragment {
    //Параметры текущего пользователя тестово здесь
    String userLevel = "Бывалый";




    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    // Еще Нужны файлы SpiskiPractice.java, SpiskiPracticeAdapter.java, spiski_practice_adapter.xml
    RecyclerView spiskiPracticeRecycler; // Объект на основе класса RecyclerView
    static SpiskiPracticeAdapter spiskiPracticeAdapter;// Объект на основе класса SpiskiLessonsAdapter
    static List<SpiskiPractice> spiskiPracticeList = new ArrayList<>();//Создаем список, каждый элемент которого будет на основе класса SpiskiPractice называется он spiskiPracticeList и выделяем под него память ArrayList<>
    static List<SpiskiPractice> fullSpiskiPracticeList = new ArrayList<>();// переменная для полного начального варианта списка

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------
        spiskiPracticeList.clear();
        fullSpiskiPracticeList.clear();


        // Установка списков текстов для Lessons ч2 Начало -----------------------------------------


        spiskiPracticeList.add(new SpiskiPractice(11, 0001, "Утренняя кофемания", "ic_cofe_day_small2","ic_cofe_day_small2", "Новичок", "#424345")); // Создаем объект на основе класса SpiskiPractice и указываем айди и Название(можно расширить поля)
        spiskiPracticeList.add(new SpiskiPractice(12, 0002, "Разговорчики \nс подругой", "ic_phyton_small","ic_phyton_small", "Новичок", "#9FA52D" ));
        spiskiPracticeList.add(new SpiskiPractice(13, 0003, "Утренняя кофемания", "ic_cofe_day_small2","ic_cofe_day_small2", "Бывалый", "#424345")); // Создаем объект на основе класса SpiskiPractice и указываем айди и Название(можно расширить поля)
        spiskiPracticeList.add(new SpiskiPractice(14, 0004, "Разговорчики \nс подругой", "ic_phyton_small","ic_phyton_small", "Бывалый", "#9FA52D" ));


        setSpiskiPracticeRecycler(view, spiskiPracticeList); // Вызываем наш метод и передаем туда список

        // Установка списков текстов для Lessons ч2 Конец ------------------------------------------

        fullSpiskiPracticeList.clear();
        fullSpiskiPracticeList.addAll(spiskiPracticeList); //заполняем переменную для сохранения неизменного начального варианта списка
        showSpiskiByLevel(userLevel);



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

    public static void showSpiskiByLevel(String userLevel){
        spiskiPracticeList.clear(); // стираем весь список
        spiskiPracticeList.addAll(fullSpiskiPracticeList); // заполняем его из переменной которая хранит первоначальные параметры
        List<SpiskiPractice> filterSpiski = new ArrayList<>(); // выполним фильтрацию и все необходимые курсы поместим в этот список filterCourses
        for (SpiskiPractice c : spiskiPracticeList){// цикл
            if(Objects.equals(c.getLevel(), userLevel)) // Проверяем уровень если нужный то добавляем в список filterCourses
                filterSpiski.add(c);
        }

        spiskiPracticeList.clear(); // очистили наш первоначальный список
        spiskiPracticeList.addAll(filterSpiski);// добавили в первоначальный список отфильтрованные элементы

        spiskiPracticeAdapter.notifyDataSetChanged(); // обновляем данные в адапторе

    }


}