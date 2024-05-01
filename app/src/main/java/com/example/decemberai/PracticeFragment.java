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
import com.example.decemberai.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PracticeFragment extends Fragment {
    //Параметры текущего пользователя тестово здесь
    //String userLevel = "Новичок";
    String userLevel = User.userLevel;




    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    // Еще Нужны файлы SpiskiPractice.java, SpiskiPracticeAdapter.java, spiski_practice_adapter.xml
    RecyclerView spiskiPracticeRecycler; // Объект на основе класса RecyclerView
    static SpiskiPracticeAdapter spiskiPracticeAdapter;// Объект на основе класса SpiskiLessonsAdapter
    //static List<SpiskiPractice> spiskiPracticeList = new ArrayList<>();//Создаем список, каждый элемент которого будет на основе класса SpiskiPractice называется он spiskiPracticeList и выделяем под него память ArrayList<>
    //static List<SpiskiPractice> fullSpiskiPracticeList = new ArrayList<>();// переменная для полного начального варианта списка

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Навигационное меню  Начало --------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        // Навигационное меню  Конец + ( return view;)---------------------------------------------
        User.clearSelectSpiskiPracticeList(); // Стираем переменную для временного хранения
        User.addAllSelectSpiskiPracticeList(User.fullSpiskiPracticeList);  // Записываем в неё полный список

        setSpiskiPracticeRecycler(view, User.selectSpiskiPracticeList); // Вызываем наш метод и передаем туда список

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

        List<SpiskiPractice> filterSpiski = new ArrayList<>(); // выполним фильтрацию и все необходимые курсы поместим в этот список filterSpiski
        for (SpiskiPractice c : User.selectSpiskiPracticeList){// цикл
            // Закоментил что бы небыло фильтрации по уровню пользователя
            //if(Objects.equals(c.getLevel(), userLevel)) // Проверяем уровень если нужный то добавляем в список filterSpiski
                filterSpiski.add(c);
        }

        User.clearSelectSpiskiPracticeList(); //  очистили временное хранение
        User.addAllSelectSpiskiPracticeList(filterSpiski);// добавили в временное хранение отфильтрованные элементы
        spiskiPracticeAdapter.notifyDataSetChanged(); // обновляем данные в адапторе

    }


}