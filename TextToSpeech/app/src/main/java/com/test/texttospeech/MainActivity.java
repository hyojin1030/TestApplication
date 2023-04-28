package com.test.texttospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_tts);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int state) {
                if (state == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.KOREAN);
                    textToSpeech.setPitch(1.0F);
                    textToSpeech.setSpeechRate(1.0F);

                    if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("TEST_LOG", "한국어 미지원");
                    } else {
                        Log.d("TEST_LOG", "한국어 지원");
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public void onClickPlay(View view) {
        String inputText = editText.getText().toString();
        if (inputText != null) {
            int speechState = textToSpeech.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, "TESTID");
            if (speechState == TextToSpeech.ERROR) {
                Log.d("TEST_LOG", "TTS 에러");
            }
        }

    }
}