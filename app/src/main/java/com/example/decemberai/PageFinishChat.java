package com.example.decemberai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.decemberai.model.ChatPage;
import com.example.decemberai.model.SpiskiLessons;
import com.example.decemberai.model.SpiskiPractice;
import com.example.decemberai.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PageFinishChat extends AppCompatActivity {
    private String typeOfChat, comand, type, parameter, spiskiChatTitleString, userEmail;
    private int id_chat, spiskiChatImageId;
    TextView textChatTitleFinish, textRecomendedFinish;
    SharedPreferences sp; // Переменная для SharedPreferences
    ImageView pageFinishChatImage;
    private int schetchik_slov = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_finish_chat);

        sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE); //Присваиваем sp название UserPreferences
        userEmail = sp.getString("email", "");

        ImageView imageDownload = findViewById(R.id.imageDownload); // Замените R.id.imageView на ваш идентификатор ImageView
        Glide.with(this).asGif().load(R.drawable.lightness_rotate_36).into(imageDownload); // Замените my_gif на имя вашего GIF-файла без расширения


        id_chat = getIntent().getIntExtra("id_chat", 0);
        spiskiChatImageId = getIntent().getIntExtra("spiskiChatImageId", 0);
        typeOfChat = getIntent().getStringExtra("typeOfChat");
        spiskiChatTitleString = getIntent().getStringExtra("spiskiChatTitleString");
        comand = getIntent().getStringExtra("comand");
        schetchik_slov = getIntent().getIntExtra("schetchik_slov", 0);

        System.out.println("Пришла команда" + comand);
        type = comand.substring(0, 3); // Получаем первые три символа
        parameter = comand.substring(4);
        System.out.println("Разбили её на type " + type + " parameter " + parameter);
        System.out.println("id_chat " + id_chat);
        System.out.println("typeOfChat " + typeOfChat);

        textChatTitleFinish = findViewById(R.id.textChatTitleFinish);
        textRecomendedFinish = findViewById(R.id.textRecomendedFinish);
        pageFinishChatImage = findViewById(R.id.pageFinishChatImage);

        pageFinishChatImage.setImageResource(getIntent().getIntExtra("spiskiChatImageId", 0));
        textRecomendedFinish.setText(parameter);
        textChatTitleFinish.setText(spiskiChatTitleString);


        // Создание Handler
        final Handler handler = new Handler();

        // Задержка в 10 секунд
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ваш код, который должен выполниться через 10 секунд
                if (Objects.equals(type, "300")) {
                    Executor executor2 = Executors.newSingleThreadExecutor();
                    executor2.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = User.userUpdateLevel(userEmail, parameter, schetchik_slov);
                            if (Objects.equals(result, "true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PageFinishChat.this, getString(R.string.the_user_level_has_been_updated), Toast.LENGTH_SHORT).show();
                                        User.userLevel = parameter;
                                        Intent intent = new Intent(PageFinishChat.this, MainActivity.class);// Перекидываем на MainActivity
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(PageFinishChat.this, "Error. Select your level manually", Toast.LENGTH_LONG).show();
                                        Log.e("111111111111111", "Ошибка обновления уровня пользователя");
                                        Intent intent = new Intent(PageFinishChat.this, TesterUserActivity.class);// Перекидываем на MainActivity
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                } else if (Objects.equals(type, "100")) {
                    Executor executor4 = Executors.newSingleThreadExecutor();
                    executor4.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = User.userUpdateSkill(userEmail, id_chat);
                            if (Objects.equals(result, "true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(PageFinishChat.this, "Скилл пользователя обновлен", Toast.LENGTH_SHORT).show();
                                        Log.e("111111111111111", "Скилл пользователя обновлен");
                                        User.userLevel = parameter;
                                        Intent intent = new Intent(PageFinishChat.this, MainActivity.class);// Перекидываем на MainActivity
                                        startActivity(intent);
                                        finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("111111111111111", "ошибка обновления Скила пользователя");
                                        //Toast.makeText(PageFinishChat.this, "Error. Скилл", Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = sp.edit(); // Создаем editor (редактирование) через него можно записывать в SharedPreferences
                                        editor.putInt("errorUserUpdateSkill", id_chat); // в editor Сохраняем данные в формате (ключь, значение)
                                        editor.apply(); // Применяем изменения
                                        Intent intent = new Intent(PageFinishChat.this, MainActivity.class);// Перекидываем на MainActivity
                                        startActivity(intent);
                                        finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }, 5000); // Задержка в миллисекундах (в данном случае 10 секунд)
    }



}