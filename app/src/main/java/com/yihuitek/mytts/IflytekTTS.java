package com.yihuitek.mytts;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import com.yihuitek.mytts.readtxt.ReadTxt;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Locale;
public class IflytekTTS {
    private static IflytekTTS iflytekTTS;
    TextToSpeech mTTS;
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;
    String text = "";//剩余字符
    String speaktext = "";//播放字符

    public static IflytekTTS getInstanceTTS() {
        if (iflytekTTS == null) {
            synchronized (IflytekTTS.class) {
                if (iflytekTTS == null) {
                    iflytekTTS = new IflytekTTS();
                    iflytekTTS.initTTs();
                }
            }
        }
        return iflytekTTS;
    }

    private void initTTs() {
        mTTS = new TextToSpeech(MyApplication.context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Log.e("TAG", "初始化成功");
                    //设置语言为中文
//                    int supported = mTTS.isLanguageAvailable(Locale.CHINA);
//                    Log.d("TAG", "supported:"+supported);
                    int languageCode = mTTS.setLanguage(Locale.CHINESE);
                    Log.e("TAG", "onInit: 是否支持这种语言:"+languageCode);
                    //判断是否支持这种语言，Android原生不支持中文，使用科大讯飞的tts引擎就可以了
                    if (languageCode == TextToSpeech.LANG_NOT_SUPPORTED) {

                    } else {
                        //不支持就改成英文
                        mTTS.setLanguage(Locale.US);
                    }
                    Log.e("TAG", "设置listwrn");


                } else {
                    Log.e("TAG", "初始化失败");
                }
            }
        }, "com.iflytek.speechcloud");

        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.e("TAG", "开始播放");
            }
            @Override
            public void onDone(String utteranceId) {
                Log.e("TAG", "开始播放onDone");
                next();
            }
            @Override
            public void onError(String utteranceId) {
                Log.e("TAG", "开始播放onDoneon Error");
            }
        });

    }

    public boolean speak() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speaktext = ReadTxt.read(Config.offset, Config.offset + Config.len);
                text = text + speaktext;
                int index = text.lastIndexOf("。");
                speaktext = text.substring(0, index + 1).trim();
                text = text.substring(index + 1).trim();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("txt", speaktext);
                message.setData(bundle);
                EventBus.getDefault().post(message);
                Config.offset = Config.offset + Config.len;
                speak(speaktext);
            }
        }).start();
        return true;
    }


    public void speak(String text) {
        //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
//        mTTS.setPitch(1.0f);
//        //设置语速
//        mTTS.setSpeechRate(1.0f);
        Log.e("calm", "speak");
        final HashMap ttsOptions = new HashMap<>();
        ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance");//utterance，这个参数随便写，用于监听播报完成的回调中


        mTTS.speak(text, TextToSpeech.QUEUE_ADD, ttsOptions);
    }
    public void stop(){
        mTTS.stop();
    }
    /**
     * 倍速
     */
    public String  speed(){
        mTTS.stop();
        String data="1.0";
        if(Config.speed.equals("50")){
            Config.speed="60";
            data="1.25";
        }else if(Config.speed.equals("60")){
            Config.speed="75";
            data="1.5";
        }else if(Config.speed.equals("75")){
            Config.speed="100";
            data="2.0";
        }else if(Config.speed.equals("100")){
            Config.speed="50";
            data="1.0";
        }
        speaktext=speaktext.substring(speaktext.length()*mPercentForPlaying/100);
        Log.e("calm","重新播放:"+speaktext);
        mTTS.setSpeechRate(Float.valueOf(data));
        final HashMap ttsOptions = new HashMap<>();
        ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance");//utterance，这个参数随便写，用于监听播报完成的回调中
        mTTS.speak(text, TextToSpeech.QUEUE_ADD, ttsOptions);
        return data;
    }

    public void next() {
        mTTS.stop();
        speak();

    }
    public void destory(){
        mTTS.shutdown();
        mTTS = null;
    }


    public boolean readFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speaktext = ReadTxt.read(Config.offset, Config.offset + Config.len);
                text=text+speaktext;
                int index=text.lastIndexOf("。");
                speaktext=text.substring(0,index+1).trim();
                text=text.substring(index+1);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("txt",speaktext);
                message.setData(bundle);
                EventBus.getDefault().post(message);
                Config.offset = Config.offset + Config.len;

            }
        }).start();
        return true;
    }
}
