package com.yihuitek.mytts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yihuitek.mytts.okhttp.OkUtils;
import com.yihuitek.mytts.readtxt.ReadTxt;
import com.yihuitek.mytts.util.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends Activity {
    String TAG = "MainActivity TTS";
    TTS tts;
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRequetPermission();
        EventBus.getDefault().register(MainActivity.this);
        Log.e("calm", "--------------------");
        tts = TTS.getInstanceTTS(MainActivity.this);
        String path = "txt/dldl.txt";
        Config.filepath = path;

        //tts.speak("");
        editText = (EditText) findViewById(R.id.ed);
        editText.clearFocus();
        textView=(TextView)findViewById(R.id.chapters);
//        textView.
//        OkUtils.getWeather();
    }

    public void speak(View view) {
        try {
            String ed = editText.getText().toString();
            if (ed == null || ed.length() < 1) {
                Config.offess = SpUtil.getInt(MyApplication.context, "offess", 0);
            } else {
                Config.offess = Integer.parseInt(ed) - 50;
            }
            if (Config.offess < 50) {
                Config.offess = 0;
            }
            boolean bool = tts.speak("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void speed(View view){
         String speed=tts.speed();
        ((Button)view).setText(speed);
//       ReadTxt.readChapters();
    }
    public void stop(View view) {
        tts.stop();
        SpUtil.putInt(MyApplication.context, "offess", Config.offess);
        Toast.makeText(MyApplication.context, "位置: " + Config.offess, Toast.LENGTH_LONG).show();
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
    public void onEventMainThread(Message message){
          String txt= message.getData().getString("txt");
          textView.setText(txt);
    }




    protected void onDestroy(){
        SpUtil.putInt(MyApplication.context, "offess", Config.offess);
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
