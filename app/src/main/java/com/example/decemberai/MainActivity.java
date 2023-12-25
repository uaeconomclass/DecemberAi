package com.example.decemberai;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.decemberai.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // binding.bottomNavigationView.setSelectedItemId(R.id.button_practice); Это на всякий случай установка активного эллемента нижнего меню
        replaceFragment(new Lessons_wFragment(), false); // При загрузке страницы сразу загружаемся с Лекции

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.button_lessons:
                    replaceFragment(new Lessons_wFragment(), true);
                    break;
                case R.id.button_practice:
                    replaceFragment(new Practice_wFragment(), true);
                    break;
                case R.id.button_profile:
                    replaceFragment(new Profile_wFragment(), true);
                    break;
                case R.id.button_progress:
                    replaceFragment(new Progress_wFragment(), true);
                    break;
                default:
                    replaceFragment(new Lessons_wFragment(), true);
                    break;

            }


            return true;
        });


    }



    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed(); // Стандартное поведение, если стек возврата пуст
        } else {
            fragmentManager.popBackStack(); // Возврат к предыдущему фрагменту из стека возврата
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_w, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null); // Добавление в стек возврата, если нужно
        }

        fragmentTransaction.commit();
    }


}



