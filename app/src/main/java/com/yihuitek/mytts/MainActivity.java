package com.yihuitek.mytts;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends  Activity {
    String TAG="MainActivity TTS";
    TTS tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("calm","--------------------");
      tts=  TTS.getInstanceTTS(MainActivity.this);

    }
    public void speak(View view){
      boolean bool= tts.speak("移汇科技");
      Log.e("calm","speak res："+bool);
    }
}
