package com.example.decemberai;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Получаем корневую View фрагмента
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        // Возвращаем корневую View
        return rootView;
    }
}