package com.yihuitek.mytts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.util.ResourceUtil;
import com.yihuitek.mytts.readtxt.ReadTxt;

import java.io.InputStream;
import java.util.function.ToDoubleBiFunction;

public class MainActivity extends Activity {
    String TAG = "MainActivity TTS";
    TTS tts;
    String filePath = "";
    int office = 0;
   EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("calm", "--------------------");
        tts = TTS.getInstanceTTS(MainActivity.this);
        String path = "txt/dldl.txt";
        Config.filepath = path;
        //tts.speak("");
        editText=(EditText)findViewById(R.id.ed);


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    Log.e("calm", "kaishi  " + i);
//                    String str = ReadTxt.read(Config.offess, Config.offess + Config.len);
////               String str= ReadTxt.read(Config.filepath,Config.offess,Config.len);
//                    Log.e("calm", "jieshu   " + i);
//                }
//            }
//        }).start();
    }

    public void speak(View view) {
       String ed=editText.getText().toString();
       Config.offess=Integer.parseInt(ed);
        boolean bool = tts.speak("");
    }

    public void stop(View view) {
        tts.stop();
        //TODO:保存位置
        Toast.makeText(MyApplication.context,"位置: "+Config.offess,Toast.LENGTH_LONG).show();
    }
}
