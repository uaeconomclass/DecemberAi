package com.example.decemberai;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayDeque;
import java.util.Deque;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);// Устанавливаем размер стека для хранения страниц
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }



    private Fragment getFragment(int itemId){

        switch (itemId){

            case R.id.btn_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();


            case R.id.btn_Dashboard:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new DashboardFragment();


            case R.id.btn_Notification:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new NotificationFragment();

            case R.id.btn_Item:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new ItemFragment();

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





}