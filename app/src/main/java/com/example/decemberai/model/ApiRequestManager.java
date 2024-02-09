package com.example.decemberai.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiRequestManager {

    // Реализация методов создания ветки, отправки запросов и обработки ответов


}


/*
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");

            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role","user");
            obj.put("content",question);
            messageArr.put(obj);

            jsonBody.put("messages",messageArr);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
                //.url("\n" +
                    //"https://api.openai.com/v1/chat/completions")
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {//Отправляем запрос, ждем ответ, потом ответ обрабатываем, прийдет или ошибка или ответ
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " +e.getMessage()); //Обработка ошибочного ответа
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject jsonObject = null; //Подучили ответ в формате json
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices"); // выбираем по ключу choices
                        String result = jsonArray.getJSONObject(0)// выбираем первый эллемент
                                .getJSONObject("message")
                                    .getString("content");
                        addResponse(result.trim());// выводим результат, trim() че то лишнее обрезает
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    addResponse("Failed to load response due to " +response.body().toString()); //не будет показываться ошибка, вернуть toString вместо string
                }
            }
        });

        */
