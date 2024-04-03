package com.example.decemberai;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.decemberai.adapter.SpiskiHomeAdapter;
import com.example.decemberai.adapter.SpiskiLessonsAdapter;
import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class HomeFragment extends Fragment {
    String level, skillString;
    Integer skillInt;
    TextView home_level, home_skill;

    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------
    // Еще Нужны файлы SpiskiLessons.java, SpiskiLessonsAdapter.java, spiski_lessons_adapter.xml
    RecyclerView spiskiLessonsRecycler; // Объект на основе класса RecyclerView
    static SpiskiHomeAdapter spiskiHomeAdapter;// Объект на основе класса SpiskiHomeAdapter

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем корневую View фрагмента
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        home_level = rootView.findViewById(R.id.home_level);
        home_skill = rootView.findViewById(R.id.home_skill);

        level = User.userLevel;
        skillInt = User.userSkill;
        skillString = Integer.toString(skillInt);// перевели значение скила в текст, потом подумаем на счет как добавлять звездочки

        home_level.setText(level);
        home_skill.setText(skillString);

        User.clearSelectSpiskiLessonsList(); // Стираем переменную для временного хранения
        User.addAllSelectSpiskiLessonsList(User.fullSpiskiLessonsList);  // Записываем в неё полный список

        setSpiskiLessonsRecycler(rootView, User.selectSpiskiLessonsList); // Вызываем наш метод и передаем туда список

        showCoursesByCategory(level);

        // Возвращаем корневую View
        return rootView;
    }


    private void setSpiskiLessonsRecycler(View view, List<SpiskiLessons> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);// Задаем стилей для списка
        spiskiLessonsRecycler = view.findViewById(R.id.spiskiHomeRecycler); // Устанавливаем ссылку на наш объект Recicler
        spiskiLessonsRecycler.setLayoutManager(layoutManager); // Подключаем стили прописанные ранее к spiskiLessonsRecycler

        spiskiHomeAdapter = new SpiskiHomeAdapter(requireContext(), categoryList); // выделяем память для адаптера и передаем в конструктор параметры
        spiskiLessonsRecycler.setAdapter(spiskiHomeAdapter); // Устанавливаем Адаптер к нашему spiskiLessonsRecycler
    }
    // Установка списков текстов для Lessons ч3 Конец -------------------------------------------------


    public static void showCoursesByCategory(String userLevel){

        List<SpiskiLessons> filterSpiski = new ArrayList<>(); // выполним фильтрацию и все необходимые курсы поместим в этот список filterSpiski
        for (SpiskiLessons c : User.selectSpiskiLessonsList){// цикл
            if(Objects.equals(c.getLevel(), userLevel)) // Проверяем уровень если нужный то добавляем в список filterSpiski
                filterSpiski.add(c);
        }

        Random random = new Random();
        List<SpiskiLessons> selectedLessons = new ArrayList<>();
        // Выбираем четыре случайных элемента из filterSpiski
        int size = filterSpiski.size();
        for (int i = 0; i < Math.min(size, 4); i++) {
            SpiskiLessons randomLesson = filterSpiski.remove(random.nextInt(filterSpiski.size()));
            selectedLessons.add(randomLesson);
        }


        User.clearSelectSpiskiLessonsList(); // очистили временное хранение
        User.addAllSelectSpiskiLessonsList(selectedLessons);// добавили в временное хранение отфильтрованные элементы
        spiskiHomeAdapter.notifyDataSetChanged(); // обновляем данные в адапторе

    }

}