package com.example.decemberai.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decemberai.Message;
import com.example.decemberai.R;
import com.example.decemberai.adapter.MessageAdapter;

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

public class PracticePage extends AppCompatActivity {
    RecyclerView practiceRecyclerView;
    TextView practiceWelcomeTextView;
    EditText practiceMessageEditText;
    ImageButton practiceSayBatton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // время ожидания ответа от openai, по истечении ,если не прийдет ответ, выдастся ошибка исключение
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_page);

        ConstraintLayout spiskiPracticeBackGround = findViewById(R.id.practicePageContainer);// Создаем переменные которые ссылаются на объекты из дизайна привязыванием их по айди
        ImageView spiskiPracticeImageBigId = findViewById(R.id.practicePageImage);
        TextView spiskiPracticeTitle = findViewById(R.id.practicePageTitle);
        TextView practicePageLevel = findViewById(R.id.practicePageLevel);

        spiskiPracticeBackGround.setBackgroundColor(getIntent().getIntExtra("spiskiPracticeBackGround", 0)); // Получаем переданные параметры из адаптера и устанавливаем их значение куда нам нужно
        spiskiPracticeImageBigId.setImageResource(getIntent().getIntExtra("spiskiPracticeImageBigId", 0));
        spiskiPracticeTitle.setText(getIntent().getStringExtra("spiskiPracticeTitle"));
        practicePageLevel.setText(getIntent().getStringExtra("practicePageLevel"));

        int id_practice = getIntent().getIntExtra("spiskiPracticeId", 0);
        add_view_item_practice(id_practice);



        messageList = new ArrayList<>();

        practiceRecyclerView = findViewById(R.id.practiceRecyclerView);
        practiceWelcomeTextView = findViewById(R.id.practiceWelcomeTextView);
        practiceMessageEditText = findViewById(R.id.practiceMessageEditText);
        practiceSayBatton = findViewById(R.id.practiceSayBatton);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        practiceRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this );
        llm.setStackFromEnd(true); // Обратный порядок чата
        practiceRecyclerView.setLayoutManager(llm);

        practiceSayBatton.setOnClickListener((v)->{
            String question = practiceMessageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            practiceMessageEditText.setText("");
            callAPI(question);
            practiceWelcomeTextView.setVisibility(View.GONE);
        });


    }
    public void add_view_item_practice(int id_practice){
        User.practice_item_id.add(id_practice); // Записываем айди лекции в просмотренные
        Toast.makeText(this, "Добавлено в просмотренные :)", Toast.LENGTH_LONG).show();
    }

    void addToChat(String message, String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                practiceRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void  addResponse(String response){
        messageList.remove(messageList.size()-1);// удаляем текст что ПОДОЖДИТЕ печатает
        addToChat(response,Message.SENT_BY_BOT);
    }
    void callAPI(String question){
        //   https://square.github.io/okhttp/  gpt-3.5-turbo-instruct
        messageList.add(new Message("Typing... ",Message.SENT_BY_BOT)); // текст что ПОДОЖДИТЕ печатает

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
                addResponse("Failed to load response due to " +e.getMessage());
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
    }
}