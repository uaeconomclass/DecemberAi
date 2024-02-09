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
import android.widget.LinearLayout;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;



public class PracticePage extends AppCompatActivity {
    RecyclerView practiceRecyclerView;
    TextView practiceWelcomeTextView;
    EditText practiceMessageEditText;
    ImageButton practiceSayBatton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    LinearLayout practiceDivUrovenClient;
    public static final MediaType JSON
            = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // время ожидания ответа от openai, по истечении ,если не прийдет ответ, выдастся ошибка исключение
            .build();

    private ApiRequestManager apiRequestManager = new ApiRequestManager();
    public static final String ASSISTANT_ID = "asst_A4Vsth3ovJQQTmA8TA4JQmAB";
    public static final String OPENAI_API_KEY = "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";
    private static ScheduledExecutorService scheduler;
    private ScheduledFuture<?> waitingLoopFuture;


    private int executionCount = 0; // Счетчик для цикла при опросе, когда будетготов ответ от Ассистента
    private String savedThreadId;// Переменная для хранения threadId

// Протестить счетчик выполнений что бы срабатывал и цикл выключался


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

        practiceRecyclerView = findViewById(R.id.practiceRecyclerView); //Окно диалога
        practiceWelcomeTextView = findViewById(R.id.practiceWelcomeTextView); // Приветственный текст
        practiceMessageEditText = findViewById(R.id.practiceMessageEditText); //Текст который ввел пользователь
        practiceSayBatton = findViewById(R.id.practiceSayBatton); //Кнопка отправить
        practiceDivUrovenClient = findViewById(R.id.practiceDivUrovenClient); //Строка об Уровнеклиента

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        practiceRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this );
        llm.setStackFromEnd(true); // Обратный порядок чата
        practiceRecyclerView.setLayoutManager(llm);

        practiceSayBatton.setOnClickListener((v)->{
            String question = practiceMessageEditText.getText().toString().trim(); //Получаем вопрос пользователя
            spiskiPracticeImageBigId.setVisibility(View.GONE); //Прячем большую картинку страницы
            practiceDivUrovenClient.setVisibility(View.GONE); //Прячем Строку об уровне клиента
            practiceWelcomeTextView.setVisibility(View.GONE); //Прячем приветственный текст
            addToChat(question, Message.SENT_BY_ME); // Записываем вопрос пользователя в чат
            practiceMessageEditText.setText(""); // Очищаем окно ввода вопросов пользователя
            callAPI(question);

        });

        scheduler = Executors.newScheduledThreadPool(1);

    }
    public void add_view_item_practice(int id_practice){
        User.practice_item_id.add(id_practice); // Записываем айди лекции в просмотренные
        Toast.makeText(this, "Добавлено в просмотренные :)", Toast.LENGTH_LONG).show();
    }

    public void addToChat(String message, String sendBy){ // Добавление текста в чат
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                practiceRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    public void  addResponse(String response){ //Запись ответа Бота
        //messageList.remove(messageList.size()-1);// удаляем текст что ПОДОЖДИТЕ печатает
        addToChat(response,Message.SENT_BY_BOT);
    }
    void callAPI(String question) {
        //   https://square.github.io/okhttp/  gpt-3.5-turbo-instruct
        messageList.add(new Message("Typing... ", Message.SENT_BY_BOT)); // текст что ПОДОЖДИТЕ печатает
        runWorkflow(question);
    }





    public interface ThreadCreationCallback {
        void onThreadCreated(String threadId);

        void onThreadCreationFailed(String errorMessage);

    }

    public interface QuestionRequestCallback {
        void onQuestionRequestSuccess(String questionSuccess);

        void onQuestionRequestFailure(String errorMessage);
    }

    public interface AssistRequestCallback {
        void onAssistRequestSuccess(String run_id);

        void onAssistRequestFailure(String errorMessage);
    }

    public interface WaitForResponseCallback {
        void onResponseSuccess(String runStatus);

        void onResponseFailure(String errorMessage);
    }

    public void createThread(final ThreadCreationCallback callback) {
        if (savedThreadId != null) {
            callback.onThreadCreated(savedThreadId);
        }else {
            JSONObject jsonBody = new JSONObject();
            try {

                jsonBody.put("messages", new JSONArray());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/threads")
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("OpenAI-Beta", "assistants=v1")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    callback.onThreadCreationFailed("createThread Failed 1 " + e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String jsonResponse = response.body().string();

                            // Разбор JSON-ответа
                            JSONObject jsonObject = new JSONObject(jsonResponse);

                            // Получение значения id
                            String threadId = jsonObject.getString("id");
                            savedThreadId = threadId;
                            // Успешный вызов колбэка
                            callback.onThreadCreated(threadId);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onThreadCreationFailed("createThread Error parsing JSON response");
                        }
                    } else {
                        callback.onThreadCreationFailed("createThread Failed 2 " + response.body().string());
                    }
                }
            });
        }
    }

    public void addQuestionRequest(String question, String threadId, final QuestionRequestCallback callback) {
        // Ваш код для шага 2


        JSONObject jsonBody = new JSONObject();
        try {
             jsonBody.put("role", "user");
             jsonBody.put("content", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/" + threadId + "/messages")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("OpenAI-Beta", "assistants=v1")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onQuestionRequestFailure("addQuestionRequest Failed 1 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String questionSuccess = response.body().string();

                        // Успешный вызов колбэка
                        callback.onQuestionRequestSuccess(questionSuccess);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onQuestionRequestFailure("addQuestionRequest Error parsing JSON response");
                    }
                } else {
                    callback.onQuestionRequestFailure("addQuestionRequest Failed 2 " + response.body().string());
                }
            }
        });

    }

    public void addAssistRequest(String threadId, String assistantId, final AssistRequestCallback callback) {
        // Ваш код для шага 3


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("assistant_id", assistantId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/" + threadId + "/runs")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("OpenAI-Beta", "assistants=v1")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onAssistRequestFailure("addAssistRequest Failed 1 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse2 = response.body().string();

                         //Разбор JSON-ответа
                        JSONObject jsonObject2 = new JSONObject(jsonResponse2);

                        // Получение значения id
                        String run_id = jsonObject2.getString("id");
                        addResponse("Получили run_id 1  " + run_id);
                        // Успешный вызов колбэка
                        callback.onAssistRequestSuccess(run_id);

                    } catch (Exception e) {
                        addResponse("Получили ошибку run_id");
                        e.printStackTrace();

                        callback.onAssistRequestFailure("addAssistRequest Error parsing JSON response");
                    }
                } else {
                    callback.onAssistRequestFailure("addAssistRequest Failed 2 " + response.body().string());
                }
            }
        });

    }


    public void waitForResponse(String run_id, String threadId, final WaitForResponseCallback callback) {
        // Ваш код для шага 4


        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/" + threadId + "/runs/" + run_id)
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("OpenAI-Beta", "assistants=v1")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onResponseFailure("waitForResponse Failed 1 " + e.getMessage());
                stopWaitingLoop(); // Останавливаем цикл
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse3 = response.body().string();

                        // Разбор JSON-ответа
                        JSONObject jsonObject3 = new JSONObject(jsonResponse3);

                        // Получение значения id
                        String runStatus = jsonObject3.getString("status");

                        // Успешный вызов колбэка
                        callback.onResponseSuccess(runStatus);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onResponseFailure("waitForResponse Error parsing JSON response");
                        stopWaitingLoop(); // Останавливаем цикл
                    }
                } else {
                    callback.onResponseFailure("waitForResponse Failed 2 " + response.body().string());
                    stopWaitingLoop(); // Останавливаем цикл
                }
            }
        });


    }

    public void getAssistantResponse(String threadId, String run_id) {
        // Ваш код для шага 5



        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/" + threadId + "/messages")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("OpenAI-Beta", "assistants=v1")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                addResponse("getAssistantResponse Failed 1 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String jsonResponse = response.body().string();

                        // Разбор JSON-ответа
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        // Получаем первый элемент массива данных, который содержит последний ответ ассистента
                        JSONObject lastMessageObject = dataArray.getJSONObject(0);
                        String role = lastMessageObject.getString("role");

                        // Проверяем роль сообщения (должно быть "assistant")
                        if (role.equals("assistant")) {
                            JSONArray contentArray = lastMessageObject.getJSONArray("content");

                            // Проходим по всем элементам контента
                            for (int j = 0; j < contentArray.length(); j++) {
                                JSONObject contentObject = contentArray.getJSONObject(j);

                                // Проверяем тип контента (должен быть "text")
                                if (contentObject.getString("type").equals("text")) {
                                    JSONObject textObject = contentObject.getJSONObject("text");
                                    String otvetAssistant = textObject.getString("value");
                                    addResponse(otvetAssistant);
                                }
                            }
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        addResponse("getAssistantResponse Error parsing JSON response");
                    }
                } else {
                    addResponse("getAssistantResponse Failed 2 " + response.body().string());
                }
            }
        });




    }

    public void runWorkflow(String question) {
        executionCount = 0;

        // Использование методов с колбэками
        addResponse("question: " + question);


        createThread( new ThreadCreationCallback() {
            @Override
            public void onThreadCreated(String threadId) {
                addResponse("Thread ID: " + threadId);





                addQuestionRequest(question, threadId, new QuestionRequestCallback() {
                    @Override
                    public void onQuestionRequestSuccess(String questionSuccess) {
                        addResponse(" onQuestionRequest - Success " + questionSuccess);





                        addAssistRequest(threadId, ASSISTANT_ID, new AssistRequestCallback() {
                            @Override
                            public void onAssistRequestSuccess(String run_id) {
                                addResponse(" onAssistRequest - Success " + run_id);



                                // Запускаем цикл ожидания ответа
                                    waitingLoopFuture = startWaitingLoop(run_id, threadId, new WaitForResponseCallback() {
                                        @Override
                                        public void onResponseSuccess(String runStatus) {
                                           // Цикл работает так: сначало выполняется все что внутри функции startWaitingLoop, после её выполнения вызывается колбек WaitForResponseCallback
                                            // и он вызывает onResponseSuccess после чего цикл начинается заново если его не остановит функция stopWaitingLoop()
                                            addResponse(" waitForResponse - Success  status = " + runStatus);



                                            if (runStatus.equals("completed")) {
                                                // Если получен статус "completed", останавливаем цикл ожидания
                                                stopWaitingLoop();
                                                getAssistantResponse(threadId, run_id);//Последний шаг, получение ответа от Ассистента

                                            } else if (executionCount >= 20) { // После выполнения 20 раз
                                                stopWaitingLoop(); // Останавливаем цикл
                                                addResponse("The waiting time has been exceeded, repeat");
                                            }

                                        }

                                        @Override
                                        public void onResponseFailure(String errorMessage) {
                                            // Обработка ошибки при ожидании ответа Ассистента
                                            //addResponse(errorMessage);
                                            addResponse("Say it again");
                                        }
                                    });



                            }

                            @Override
                            public void onAssistRequestFailure(String errorMessage) {
                                // Обработка ошибки при запуске ассистента
                                //addResponse(errorMessage);
                                addResponse("Say it again");
                            }
                        });
                    }

                    @Override
                    public void onQuestionRequestFailure(String errorMessage) {
                        // Обработка ошибки при отправке вопроса
                        //addResponse(errorMessage);
                        addResponse("Say it again");
                    }
                });
            }

            @Override
            public void onThreadCreationFailed(String errorMessage) {
                // Обработка ошибки при создании ветки
                //addResponse(errorMessage);
                addResponse("Say it again");
            }
        });
    }


    private void stopWaitingLoop() {
        if (waitingLoopFuture != null) {
            waitingLoopFuture.cancel(true);
        }
    }



    // Методы startWaitingLoop и stopWaitingLoop для управления циклом ожидания
    private ScheduledFuture<?> startWaitingLoop(String run_id, String threadId, WaitForResponseCallback callback) {
        return scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                waitForResponse(run_id, threadId, callback); // Функция проверки готов ли ответ Ассистента
                executionCount++; // Увеличиваем счетчик выполнений
            }
        }, 0, 3, TimeUnit.SECONDS);// Проверка каждеы 3 секунды
    }





}