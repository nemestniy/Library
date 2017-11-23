package com.nemestniy.library;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nemestniy.library.API.TranslateApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Translator extends AppCompatActivity {

    private TextView tvTranslate;
    private EditText getStr;
    private Button translate;

    private final String URL = "https://translate.yandex.net";
    private final String Api = "trnsl.1.1.20171117T213449Z.b7af4f205122cc9f.1c439ae78b426e7d28e646c4c4418cf6fd3a134f";

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private TranslateApi inter = retrofit.create(TranslateApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        tvTranslate = (TextView) findViewById(R.id.tvTranslate);
        getStr = (EditText) findViewById(R.id.editTranslate);
        translate = (Button) findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> jsonMap = new HashMap<>();    // собираем запрос к API
                jsonMap.put("key", Api);
                jsonMap.put("lang", "en-ru");
                jsonMap.put("text", getStr.getText().toString());

                Call<Object> call = inter.translate(jsonMap);         //делаем запрос

                try {
                    Response<Object> response = call.execute();

                    Map<String, String> map = gson.fromJson(response.body().toString(), Map.class); // Здесь получаем ответ из Json в Gson

                    for(Map.Entry ent : map.entrySet()) {
                        if(ent.getKey().equals("text")){
                            tvTranslate.setText(ent.getValue().toString());
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    Toast.makeText(Translator.this, "Не работает", Toast.LENGTH_SHORT).show();
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
