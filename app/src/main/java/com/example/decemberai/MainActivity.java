package com.example.decemberai;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.decemberai.adapter.SpiskiLessonsAdapter;
import com.example.decemberai.model.SpiskiLessons;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Навигационное меню ч1 начало -------------------------------------------------------------------

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);// Устанавливаем размер стека для хранения страниц
    boolean flag = true;
    // Навигационное меню ч1 Конец -------------------------------------------------------------------


    // Установка списков текстов для Lessons ч1 Начало -------------------------------------------------

    RecyclerView spiskiLessonsRecycler;
    SpiskiLessonsAdapter spiskiLessonsAdapter;

    // Установка списков текстов для Lessons ч1 Конец -------------------------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // Навигационное меню ч2 начало -------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        integerDeque.push(R.id.btn_home);
        loadFragment(new HomeFragment());
        bottomNavigationView.setSelectedItemId(R.id.btn_home);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (integerDeque.contains(id)){
                    if(id == R.id.btn_home){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.btn_home);
                                flag = false;

                            }

                        }

                    }
                    integerDeque.remove(id);
                }
                integerDeque.push(id);

                loadFragment(getFragment(item.getItemId()));
                return true;
            }
        });
    // Навигационное меню ч2 Конец ---------------------------------------------------------------------


    // Установка списков текстов для Lessons ч2 Начало -------------------------------------------------

        List<SpiskiLessons> spiskiLessonsList = new ArrayList<>();
        spiskiLessonsList.add(new SpiskiLessons(133, "Игры"));
        spiskiLessonsList.add(new SpiskiLessons(233, "Сайты"));
        spiskiLessonsList.add(new SpiskiLessons(333, "Языки"));
        spiskiLessonsList.add(new SpiskiLessons(433, "Прочее"));

        setSpiskiLessonsRecycler(spiskiLessonsList);

    // Установка списков текстов для Lessons ч2 Конец -------------------------------------------------



    }


    // Навигационное меню ч3 Начало ---------------------------------------------------------------------
    private Fragment getFragment(int itemId){

        switch (itemId){

            case R.id.btn_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();


            case R.id.btn_lessons:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new LessonsFragment();


            case R.id.btn_practice:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new PracticeFragment();

            case R.id.btn_account:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new AccountFragment();

        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new HomeFragment();
    }


    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed(){
        integerDeque.pop();

        if(!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }else{
            finish();
        }
    }

    // Навигационное меню ч3 конец ---------------------------------------------------------------------

    // Установка списков текстов для Lessons ч3 Начало -------------------------------------------------

    private void setSpiskiLessonsRecycler(List<SpiskiLessons> categoryList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        spiskiLessonsRecycler = findViewById(R.id.spiskiLessonsRecycler);
        spiskiLessonsRecycler.setLayoutManager(layoutManager);

        spiskiLessonsAdapter = new SpiskiLessonsAdapter(this, categoryList);
        spiskiLessonsRecycler.setAdapter(spiskiLessonsAdapter);
    }

    // Установка списков текстов для Lessons ч3 Конец -------------------------------------------------



}