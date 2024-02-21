package com.example.decemberai;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {
    Button button_test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем корневую View фрагмента
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Находим кнопку в корневой View
        button_test = rootView.findViewById(R.id.button_test);


        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Возвращаем корневую View
        return rootView;
    }
}