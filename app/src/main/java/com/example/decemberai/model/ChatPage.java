package com.example.decemberai.model;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decemberai.Message;
import com.example.decemberai.PageFinishChat;
import com.example.decemberai.R;
import com.example.decemberai.TesterUserActivity;
import com.example.decemberai.adapter.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.jetbrains.annotations.NotNull;



public class ChatPage extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    TextView chatWelcomeTextView, spiskiChatTitle;
    EditText chatMessageEditText;
    ImageButton chatSayBatton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    LinearLayout chatDivUrovenClient;
    public static final MediaType JSON
            = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // время ожидания ответа от openai, по истечении ,если не прийдет ответ, выдастся ошибка исключение
            .build();

    private ApiRequestManager apiRequestManager = new ApiRequestManager();
    //public static final String OPENAI_API_KEY = User.openaiApiKey;
    //public static final String OPENAI_API_KEY = "";
    public static String ASSISTANT_ID = "";
    public static String testerVova = ""; //Если я то 1 , если валек то пусто

    private static ScheduledExecutorService scheduler;
    private ScheduledFuture<?> waitingLoopFuture;


    private int executionCount = 0; // Счетчик для цикла при опросе, когда будетготов ответ от Ассистента
    private String savedThreadId;// Переменная для хранения threadId
    private String typeOfChat, spiskiChatTitleString;
    private int id_chat;


    private static  final int REQUEST_AUDIO_PREMISSION_CODE=101;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    ImageView chatSayBattonNew, spiskiChatImageId;
    String path = null;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean recordingPremissionYes = false;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);


        // Кнопка Voice
        chatSayBattonNew= findViewById(R.id.chatSayBattonNew);








        ConstraintLayout spiskiChatBackGround = findViewById(R.id.chatPageContainer);// Создаем переменные которые ссылаются на объекты из дизайна привязыванием их по айди
        spiskiChatImageId = findViewById(R.id.chatPageImage);
        TextView spiskiChatTitle = findViewById(R.id.chatPageTitle);
        TextView chatPageLevelName = findViewById(R.id.chatPageLevelName);
        TextView chatPageLevel = findViewById(R.id.chatPageLevel);

        spiskiChatBackGround.setBackgroundColor(getIntent().getIntExtra("spiskiChatBackGround", 0)); // Получаем переданные параметры из адаптера и устанавливаем их значение куда нам нужно
        spiskiChatTitle.setTextColor(getIntent().getIntExtra("spiskiChatColorText", 0));
        chatPageLevelName.setTextColor(getIntent().getIntExtra("spiskiChatColorText", 0));
        chatPageLevel.setTextColor(getIntent().getIntExtra("spiskiChatColorText", 0));

        spiskiChatImageId.setImageResource(getIntent().getIntExtra("spiskiChatImageId", 0));
        spiskiChatTitle.setText(getIntent().getStringExtra("spiskiChatTitle"));
        chatPageLevel.setText(getIntent().getStringExtra("chatPageLevel"));

        id_chat = getIntent().getIntExtra("spiskiChatId", 0);
        typeOfChat = getIntent().getStringExtra("typeOfChat");
        spiskiChatTitleString = getIntent().getStringExtra("spiskiChatTitle");

        ASSISTANT_ID = getIntent().getStringExtra("assistantId");




        int colorFromIntent = getIntent().getIntExtra("spiskiChatColorText", Color.BLACK);
        // Получаем ресурс ID векторной картинки
        int vectorDrawableResourceId = R.drawable.button_speak4;
        // Получаем объект Drawable из ресурса
        Drawable drawable = ContextCompat.getDrawable(this, vectorDrawableResourceId);
        // Проверяем, к какому типу относится картинка и устанавливаем цвет, на разных устройствах может быть разный тип
        if (drawable instanceof VectorDrawableCompat) {
            VectorDrawableCompat vectorDrawable = (VectorDrawableCompat) drawable;
            // Устанавливаем новый цвет заполнения
            vectorDrawable.setTint(colorFromIntent); // Используем цвет из интента
        } else if (drawable instanceof VectorDrawable) {
            VectorDrawable vectorDrawable = (VectorDrawable) drawable;
            // Устанавливаем новый цвет заполнения
            vectorDrawable.setTint(colorFromIntent); // Используем цвет из интента
        }
        // Устанавливаем измененный Drawable на ImageView или другой виджет
        chatSayBattonNew.setImageDrawable(drawable);





        final Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation_on);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation_out);

        chatSayBattonNew.setOnTouchListener(new View.OnTouchListener() {//Обработка нажатия и отпускания кнопки
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Обработка нажатия кнопки
                        v.startAnimation(scaleAnimation);
                        startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Обработка отпускания кнопки
                        v.startAnimation(fadeOutAnimation);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopRecording(); // Вызов метода stopRecording() через 2 секунды
                            }
                        }, 1000); // 2 секунды задержки
                        return true;
                }
                return false;
            }
        });






        add_view_item_chat();



        messageList = new ArrayList<>();

        chatRecyclerView = findViewById(R.id.chatRecyclerView); //Окно диалога
        chatWelcomeTextView = findViewById(R.id.chatWelcomeTextView); // Приветственный текст
        chatMessageEditText = findViewById(R.id.chatMessageEditText); //Текст который ввел пользователь
        chatSayBatton = findViewById(R.id.chatSayBatton); //Кнопка отправить
        chatDivUrovenClient = findViewById(R.id.chatDivUrovenClient); //Строка об Уровнеклиента


        chatWelcomeTextView.setText(ASSISTANT_ID);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this );
        llm.setStackFromEnd(true); // Обратный порядок чата
        chatRecyclerView.setLayoutManager(llm);

        chatSayBatton.setOnClickListener((v)->{
            String question = chatMessageEditText.getText().toString().trim(); //Получаем вопрос пользователя
            spiskiChatImageId.setVisibility(View.GONE); //Прячем большую картинку страницы
            chatDivUrovenClient.setVisibility(View.GONE); //Прячем Строку об уровне клиента
            chatWelcomeTextView.setVisibility(View.GONE); //Прячем приветственный текст
            addToChat(question, Message.SENT_BY_ME); // Записываем вопрос пользователя в чат
            chatMessageEditText.setText(""); // Очищаем окно ввода вопросов пользователя
            callAPI(question);

        });

        scheduler = Executors.newScheduledThreadPool(1);

    }

    // Обработка нажатия на кнопку Voice
    public void startRecording() {

        if(recordingPremissionYes || checkRecordingPremission()){ // Проверка наличия разрешения на запись у пользователя
            recordingPremissionYes = true;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION); //Аудио устройство Микрофон
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // Формат записи MPEG-4
                    mediaRecorder.setOutputFile(getRecordingFilePath()); // Добавление расширения .mp3
                    path = getRecordingFilePath();

                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // Используем AAC кодирование для сохранения в MP3

                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        // Выведем сообщение об ошибке в лог для отладки
                        Log.e("MediaRecorder", "Ошибка при подготовке MediaRecorder: " + e.getMessage());
                        // Бросаем RuntimeException для того, чтобы приложение остановилось и мы увидели ошибку
                        throw new RuntimeException(e);
                    }

                    mediaRecorder.start();
                }
            });

        }else{
            requestRecordingPremession();//Получение разрешения на запись у пользователя
        }

    }

    // Обработка отпускания кнопки Voice
    public void stopRecording() {
        if (recordingPremissionYes) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            /*
            mediaPlayer = new MediaPlayer();

            if (path != null) {
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Ошибка создания медиа плеера ", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Ошибка получения файла записи ", Toast.LENGTH_SHORT).show();
                return;
            }

            */

            spiskiChatImageId.setVisibility(View.GONE); //Прячем большую картинку страницы
            chatDivUrovenClient.setVisibility(View.GONE); //Прячем Строку об уровне клиента
            chatWelcomeTextView.setVisibility(View.GONE); //Прячем приветственный текст


            uploadFileToServer(path, new UploadCallback() {
                @Override
                public void onSuccess(String result) {
                    // Обработать успешную загрузку в основном потоке
                    //Log.d(TAG, "Получили ответ : " + result);
                    String question2 = String.valueOf(result); //Получаем вопрос пользователя

                    addToChat(question2, Message.SENT_BY_ME); // Записываем вопрос пользователя в чат
                    runOnUiThread(new Runnable() { // Этот код для вызова операции в основном потоке находясь на второстипенном
                        @Override
                        public void run() {
                            // Ваш код для обновления UI здесь
                            chatMessageEditText.setText(""); // Очищаем окно ввода вопросов пользователя
                        }
                    });

                    callAPI(question2);
                }

                @Override
                public void onFailure(String error) {
                    // Обработать ошибку загрузки в основном потоке
                    addResponse("Проверьте интернет соединение и повторите");
                }
            });
        }
    }



    // Отправка ответа на сервер и принятие аудио файла
    public void toVoiceAssistantResponse(String text) {

   // Создание JSON объекта с текстом для отправки на сервер
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("text", text);
            jsonBody.put("userId", User.userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Создание тела запроса
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        // Создание запроса
        Request request = new Request.Builder()
                .url("https://yourtalker.com/hendlers_api/voice_response.php")
                .post(body)
                .build();

        // Отправка запроса асинхронно
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Обработка ошибки при отправке запроса
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    // Получаем тип содержимого из заголовка ответа
                    String contentType = response.header("Content-Type");

                    // Проверяем тип содержимого
                    if (contentType != null && contentType.startsWith("audio/mpeg")) {
                        // Если тип содержимого указывает на аудио, сохраняем файл
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody != null) {
                                InputStream inputStream = responseBody.byteStream();
                                // Сохранение файла, например, на устройство
                                saveAudioFile(inputStream);
                                Log.d(TAG, "Получили аудио файл");
                            }
                        }
                    } else{
                        // Если тип содержимого указывает на JSON, читаем JSON ответ
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody != null) {
                                String jsonString = responseBody.string();
                                // Обработка JSON ответа
                                JSONObject jsonObject = new JSONObject(jsonString);
                                String message = jsonObject.getString("error");
                                Log.d(TAG, "Получили текстовое сообщение: " + message);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else {
                    // Обработка неуспешного ответа
                    Log.d(TAG, "Получили ответ 222222222222222 : " + response);
                    //System.out.println("Voice assistant response request failed: " + response.code());
                }
            }
        });

    }


    // Метод для сохранения аудио файла
    private void saveAudioFile(InputStream inputStream) {
        String filePath = getRecordingFilePath(); // Получаем путь к файлу
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            Log.d(TAG, "Файл успешно сохранен: " + filePath);

            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.d(TAG, "Ошибка при воспроизведении файла полученного с сервера ");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            Log.d(TAG, "Возникла проблема при сохранении файла полученного с сервера ");
            e.printStackTrace();
        }
    }





    public interface UploadCallback {
        void onSuccess(String result);
        void onFailure(String error);
    }

    private static final String TAG = "FileUploadTask";
    private static final String UPLOAD_URL = "https://yourtalker.com/hendlers_api/upload_user_voiсe.php";

    // Метод для загрузки файла на сервер
    private void uploadFileToServer(final String filePath, final UploadCallback callback) {
        OkHttpClient client = new OkHttpClient();
        File file = new File(filePath);

        String string_userId = String.valueOf(User.userId);// Перевели цифру в текст так как цифру не получится передать

        // Создаем тело запроса для файла
        RequestBody fileRequestBody = RequestBody.create(file, MediaType.parse("audio/*"));

        // Строим многочастичное тело запроса
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileRequestBody)
                .addFormDataPart("userId", string_userId)
                .build();

        // Создаем запрос к серверу
        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        // Отправляем запрос асинхронно и получаем ответ
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse called 111111111111111");
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "Получили ответ : " + responseData);

                    try {
                        // Парсинг JSON-строки
                        JSONObject jsonObject = new JSONObject(responseData);

                        // Получение значения ключа "text"
                        String textValue = jsonObject.getString("text");

                        callback.onSuccess(textValue);
                    } catch (JSONException e) {
                        // Обработка ошибок парсинга JSON
                        Log.e(TAG, "Ошибка при разборе JSON: " + e.getMessage());
                        callback.onFailure("Ошибка при разборе JSON: " + e.getMessage());
                    }
                } else {
                    callback.onFailure("Ошибка загрузки файла: " + response.code());
                }
            }
        });
    }









    //Проверяем есть ли разрешение на использование микрофона
    public  boolean checkRecordingPremission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED){
            requestRecordingPremession();
            return false;
        }
        Toast.makeText(getApplicationContext(), "Разрешение на использование микрофона получено", Toast.LENGTH_SHORT);
        return true;
    }



    //Получение разрешения на запись у пользователя
    private void requestRecordingPremession(){
        ActivityCompat.requestPermissions(ChatPage.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PREMISSION_CODE);
    }


    //Стандартная функция вызывается когда пользователь или дал разрешение или не дал, в любом случае вызывается
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_AUDIO_PREMISSION_CODE){
            // Проверяем, что массив grantResults не пустой и содержит хотя бы один элемент
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(permissionToRecord){
                    Toast.makeText(getApplicationContext(), "Permission Given", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT);
                }
            } else {
                // Если grantResults пустой, выведите сообщение об ошибке
                Log.e("Permissions", "Пришло пустое значение grantResults array");
            }
        }
    }


    // адресс для файла записи
    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "recVoice_" + User.userId + ".mp3"); // testFile вместо этого можно добавлять рандомное имя и можно будет много файлов сохранять
        return file.getPath();
    }










    public void add_view_item_chat(){
        if ("practice".equals(typeOfChat)) {//  функционал что бы записывать в разные списки(в практики и в лекции)
            User.practice_item_id.add(id_chat); // Записываем айди лекции в просмотренные

        } else if("lessons".equals(typeOfChat)){
            User.lessons_item_id.add(id_chat);
        }

       // Toast.makeText(this, "Добавлено в просмотренные :)", Toast.LENGTH_LONG).show();
    }

    public void addToChat(String message, String sendBy){ // Добавление текста в чат
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                chatRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
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




    public void searchCommands(String response){
        // Создаем список для хранения найденных значений внутри кавычек【】
        ArrayList<String> values = new ArrayList<>();

        // Паттерн для поиска значений внутри кавычек【】
        Pattern pattern = Pattern.compile("【([^】]+)】");
        Matcher matcher = pattern.matcher(response);

        // Поиск и добавление найденных значений в список
        while (matcher.find()) {
            values.add(matcher.group(1));
        }

        // Перебор списка и выполнение действий с каждым значением
        for (String value : values) {

            System.out.println("Пришла команда" + value);
            String type = value.substring(0, 3); // Получаем первые три символа
            String parameter = value.substring(4);
            System.out.println("Разбили её на type " + type + " parameter " + parameter);
            // Если это команда, то только тогда выполняем переход на другую страницу
            if(type.equals("100") || type.equals("200") ||type.equals("300")) {
                Intent intent = new Intent(ChatPage.this, PageFinishChat.class);// Создаем условие для переадрессации на LessonsPage
                intent.putExtra("comand", value);
                intent.putExtra("typeOfChat", typeOfChat);
                intent.putExtra("id_chat", id_chat);
                intent.putExtra("spiskiChatTitleString", spiskiChatTitleString);

                startActivity(intent);
                finish(); // Завершаем текущую активность, чтобы пользователь не мог вернуться назад
            }

        }
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
                    .url("https://yourtalker.com/hendlers_api/get_thread_id" + testerVova + ".php")
                    //.header("Authorization", "Bearer " + OPENAI_API_KEY)
                    //.header("OpenAI-Beta", "assistants=v1")
                    //.post(body)
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
             jsonBody.put("threadId", threadId);// Добавил, если на прямую, то этот параметр не нужен

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);




        Request request = new Request.Builder()
                .url("https://yourtalker.com/hendlers_api/add_message" + testerVova + ".php")
                .header("Content-Type", "application/json")
                //.header("Authorization", "Bearer " + OPENAI_API_KEY)
                //.header("OpenAI-Beta", "assistants=v1")
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
            jsonBody.put("threadId", threadId);// Добавил, если на прямую, то этот параметр не нужен

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://yourtalker.com/hendlers_api/assistant_run" + testerVova + ".php")
                //.header("Authorization", "Bearer " + OPENAI_API_KEY)
                //.header("OpenAI-Beta", "assistants=v1")
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

        JSONObject jsonBody = new JSONObject();
        try {
            //jsonBody.put("assistant_id", assistantId);
            jsonBody.put("threadId", threadId);// Добавил, если на прямую, то этот параметр не нужен
            jsonBody.put("run_id", run_id);// Добавил, если на прямую, то этот параметр не нужен

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://yourtalker.com/hendlers_api/assistant_response_status" + testerVova + ".php")
                //.url("https://api.openai.com/v1/threads/" + threadId + "/runs/" + run_id)
                //.header("Authorization", "Bearer " + OPENAI_API_KEY)
                //.header("OpenAI-Beta", "assistants=v1")
                //.get()
                .post(body)
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

        JSONObject jsonBody = new JSONObject();
        try {
            //jsonBody.put("assistant_id", assistantId);
            jsonBody.put("threadId", threadId);// Добавил, если на прямую, то этот параметр не нужен


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);


        Request request = new Request.Builder()
                .url("https://yourtalker.com/hendlers_api/get_assistant_response" + testerVova + ".php")
                //.url("https://api.openai.com/v1/threads/" + threadId + "/messages")
                //.header("Authorization", "Bearer " + OPENAI_API_KEY)
                //.header("OpenAI-Beta", "assistants=v1")
                //.get()
                .post(body)
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

                                    //toVoiceAssistantResponse(otvetAssistant);

                                    searchCommands(otvetAssistant);
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