package com.example.decemberai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;
import com.example.decemberai.model.User;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    String name, email, phone, password, skillString, level;
    Integer skillInt;
    TextView name_account, email_account, phone_account, password_account, user_skill_account, user_level_account;
    SharedPreferences sp; // Переменная для SharedPreferences
    // phone пока клиенту не выводим
    Button button_reset_level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        name_account = view.findViewById(R.id.name_account);
        email_account = view.findViewById(R.id.email_account);
        phone_account = view.findViewById(R.id.phone_account);
        password_account = view.findViewById(R.id.password_account);
        user_skill_account = view.findViewById(R.id.user_skill_account);
        user_level_account = view.findViewById(R.id.user_level_account);
        button_reset_level = view.findViewById(R.id.button_reset_level);

        // По клику на button_reset_level перекидываем на страницу обновления уровня
        button_reset_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем намерение для перехода на активити TesterUserClass
                Intent intent = new Intent(requireContext(), TesterUserActivity.class);

                // Запускаем активити TesterUserClass
                startActivity(intent);
            }
        });


        sp = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);//Присваиваем sp название UserPreferences

        name = User.userName;
        phone = User.userPhone;
        level = User.userLevel;
        skillInt = User.userSkill;
        skillString = Integer.toString(skillInt);// перевели значение скила в текст, потом подумаем на счет как добавлять звездочки
        email = sp.getString("email", "");
        password = sp.getString("password", "");

        name_account.setText(name);
        phone_account.setText(phone);
        email_account.setText(email);
        password_account.setText(password);
        user_skill_account.setText(skillString);
        user_level_account.setText(level);

        ListView item_lessons_list = view.findViewById(R.id.item_lessons_list);

        List<String> itemLessonsInfo = new ArrayList<>();// переменная которая будет содержать массив из названий курсов
        for(SpiskiLessons c : User.fullSpiskiLessonsList){// перебираем массив с полным списком всех курсов
            if(User.lessons_item_id.contains(c.getLessonsId())) {// Перебираем каждый элемент, если его айди совпадает со списком айди которые сейчас есть в Л/К
                // Формируем строку, объединяя название и айди с использованием тире
                String lessonInfo = c.getLessonsId() + " - " + c.getTitle();
                itemLessonsInfo.add(lessonInfo);
            }
        }

        item_lessons_list.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemLessonsInfo));
        //Выводим список с помошью дизайна встроенного адаптера, стиль списка simple_list_item_1



        ListView item_practice_list = view.findViewById(R.id.item_practice_list);

        List<String> itemPracticeInfo = new ArrayList<>();
        for(SpiskiPractice c : User.fullSpiskiPracticeList){
            if(User.practice_item_id.contains(c.getPracticeId())) {
                // Формируем строку, объединяя название и айди с использованием тире
                String practiceInfo = c.getPracticeId() + " - " + c.getTitle();
                itemPracticeInfo.add(practiceInfo);
            }
        }

        item_practice_list.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemPracticeInfo));
        //Выводим список с помошью дизайна встроенного адаптера, стиль списка simple_list_item_1


        return view;
    }


}