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
    RecyclerView spiskiLessonsRecycler;
    SpiskiLessonsAdapter spiskiLessonsAdapter;

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        // Установка списков текстов для Lessons ч2 Начало -------------------------------------------------

        List<SpiskiLessons> spiskiLessonsList = new ArrayList<>();
        spiskiLessonsList.add(new SpiskiLessons(1, "Лекция 1"));
        spiskiLessonsList.add(new SpiskiLessons(2, "Лекция 2"));
        spiskiLessonsList.add(new SpiskiLessons(3, "Лекция 3"));
        spiskiLessonsList.add(new SpiskiLessons(4, "Лекция 4"));
        spiskiLessonsList.add(new SpiskiLessons(5, "Лекция 5"));
        spiskiLessonsList.add(new SpiskiLessons(6, "Лекция 6"));
        spiskiLessonsList.add(new SpiskiLessons(7, "Лекция 7"));

        setSpiskiLessonsRecycler(view, spiskiLessonsList);

        // Установка списков текстов для Lessons ч2 Конец -------------------------------------------------



        return view;
    }


    // Установка списков текстов для Lessons ч3 Начало -------------------------------------------------


    private void setSpiskiLessonsRecycler(View view, List<SpiskiLessons> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        spiskiLessonsRecycler = view.findViewById(R.id.spiskiLessonsRecycler);
        spiskiLessonsRecycler.setLayoutManager(layoutManager);

        spiskiLessonsAdapter = new SpiskiLessonsAdapter(requireContext(), categoryList);
        spiskiLessonsRecycler.setAdapter(spiskiLessonsAdapter);
    }
    // Установка списков текстов для Lessons ч3 Конец -------------------------------------------------
}