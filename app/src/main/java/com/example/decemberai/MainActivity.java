package com.example.decemberai;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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

    SharedPreferences sp; // Переменная для SharedPreferences
    String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences

        // Проверяем условие авторизован ли клиент
        if (!AuthorizationCompleted()) {
            // Если условие не выполняется, переходим на RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
            return;
        }


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

    }

    private boolean AuthorizationCompleted() {
        userEmail = sp.getString("email", "");
        userPassword = sp.getString("password", "");
        if((!userEmail.equals(""))&&(!userPassword.equals(""))){ //Если в SharedPreferences есть почта и пароль клиента то считаем его авторизованным
            return true;
        }else{
            return false;
        }
        // Возвращаете true, если Авторизован, и false, если не Авторизован

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




}