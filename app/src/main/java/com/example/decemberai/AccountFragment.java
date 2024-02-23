package com.example.decemberai;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;
import com.example.decemberai.model.User;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    String name, email,  password;
    TextView name_account, email_account,  password_account;
    // phone пока клиенту не выводим

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        name_account = view.findViewById(R.id.name_account);
        email_account = view.findViewById(R.id.email_account);
        password_account = view.findViewById(R.id.password_account);

        User user = new User();
        name = user.getUserName();
        email = user.getUserEmail();
        password = user.getUserPassword();

        name_account.setText(name);
        email_account.setText(email);
        password_account.setText(password);

        ListView item_lessons_list = view.findViewById(R.id.item_lessons_list);

        List<String> itemLessonsInfo = new ArrayList<>();// переменная которая будет содержать массив из названий курсов
        for(SpiskiLessons c : LessonsFragment.fullSpiskiLessonsList){// перебираем массив с полным списком всех курсов
            if(User.lessons_item_id.contains(c.getId())) {// Перебираем каждый элемент, если его айди совпадает со списком айди которые сейчас есть в Л/К
                // Формируем строку, объединяя название и айди с использованием тире
                String lessonInfo = c.getId() + " - " + c.getTitle();
                itemLessonsInfo.add(lessonInfo);
            }
        }

        item_lessons_list.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemLessonsInfo));
        //Выводим список с помошью дизайна встроенного адаптера, стиль списка simple_list_item_1



        ListView item_practice_list = view.findViewById(R.id.item_practice_list);

        List<String> itemPracticeInfo = new ArrayList<>();
        for(SpiskiPractice c : PracticeFragment.fullSpiskiPracticeList){
            if(User.practice_item_id.contains(c.getId())) {
                // Формируем строку, объединяя название и айди с использованием тире
                String practiceInfo = c.getId() + " - " + c.getTitle();
                itemPracticeInfo.add(practiceInfo);
            }
        }

        item_practice_list.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemPracticeInfo));
        //Выводим список с помошью дизайна встроенного адаптера, стиль списка simple_list_item_1


        return view;
    }


}