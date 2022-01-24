package com.yihuitek.mytts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yihuitek.mytts.ThreadTest.MyTestThread;
import com.yihuitek.mytts.okhttp.OkUtils;
import com.yihuitek.mytts.readtxt.ReadTxt;
import com.yihuitek.mytts.util.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    String TAG = "MainActivity TTS";
//    TTS tts;
    EditText editText;
    TextView textView;
    TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyTestThread my = new MyTestThread();
        Log.e("calm", "_onCreate__hello");
        IflytekTTS.getInstanceTTS();


//        my.initMyTestThread();
//        my.sendMessage(1001);
//        Log.e("calm","___hello");
        myRequetPermission();
        EventBus.getDefault().register(MainActivity.this);
//        tts = TTS.getInstanceTTS(MainActivity.this);
        String path = "txt/dldl3.txt";
        Config.filepath = path;
//
        editText = (EditText) findViewById(R.id.ed);
        Config.offset=SpUtil.getInt(MyApplication.context, "offess", 0);
        if( Config.offset>Config.len){
            Config.offset=Config.offset-Config.len;
        }
        editText.setText( Config.offset+"");
        editText.clearFocus();
        textView=(TextView)findViewById(R.id.chapters);
        IflytekTTS.getInstanceTTS().readFile();
//   mTTS.setEngineByPackageName("com.iflytek.tts");

    }

    public void speak(View view) {
        try {
            String ed = editText.getText().toString();
            if (ed == null || ed.length() < 1) {
                Config.offset = 0;
            } else {
                Config.offset = Integer.parseInt(ed) - 50;
            }
            if (Config.offset < 50) {
                Config.offset = 0;
            }
//            boolean bool = tts.speak();
            IflytekTTS.getInstanceTTS().speak();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void speed(View view) {
        String speed =   IflytekTTS.getInstanceTTS().speed();
        ((Button) view).setText(speed);
//       ReadTxt.readChapters();
    }

    public void stop(View view) {
        IflytekTTS.getInstanceTTS().stop();
//        tts.stop();
        SpUtil.putInt(MyApplication.context, "offess", Config.offset);
        Toast.makeText(MyApplication.context, "位置: " + Config.offset, Toast.LENGTH_LONG).show();
    }

    public void next(View view) {
//        tts.stop();
//        tts.speak();
        IflytekTTS.getInstanceTTS().next();
    }

    private void myRequetPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Message message) {
        String txt = message.getData().getString("txt");
        textView.setText(txt);
        editText.setText(Config.offset + "");
    }

    @Override
    protected void onDestroy() {
        SpUtil.putInt(MyApplication.context, "offess", Config.offset);
        EventBus.getDefault().unregister(this);
       IflytekTTS.getInstanceTTS().destory();
        super.onDestroy();

    }

}
