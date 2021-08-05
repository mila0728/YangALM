package com.yihuitek.mytts.ThreadTest;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyTestThread {

    private HandlerThread handlerThread;
    private   Handler handler ;

    private  Handler.Callback callback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("calm","___callback_________:msg");

            return true;
        }
    };
    public void initMyTestThread(){
        handlerThread=new HandlerThread("mytest");
        handlerThread.start();

         handler=new Handler(Looper.getMainLooper(),callback){
             @Override
             public void handleMessage(Message msg) {
                 Log.e("calm","___handler________:msg");


             }
         };

    }

    public void sendMessage(int what){
        Message msg =  handler.obtainMessage(what);
        msg.sendToTarget();
    }

}
