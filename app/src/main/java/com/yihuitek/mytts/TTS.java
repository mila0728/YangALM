package com.yihuitek.mytts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.yihuitek.mytts.readtxt.ReadTxt;

import org.greenrobot.eventbus.EventBus;

public class TTS {
    // 语音合成对象
    private SpeechSynthesizer mTts;
    private SharedPreferences mSharedPreferences;
    // 默认云端发音人
    public static String voicerCloud = "xiaoyan";
    // 默认本地发音人
    public static String voicerLocal = "xiaoyan";

    public static String voicerXtts = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_LOCAL;
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;
     String text = "";//剩余字符
     String speaktext="";//播放字符
    private Context context;

    private static TTS tts;

    private TTS() {
        Log.e("calm", "init tts");
        this.context = MyApplication.context;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=576fe70f");
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        setParam();
    }

    public static TTS getInstanceTTS(Context context) {
        if (tts == null) {
            synchronized (TTS.class) {
                if (tts == null) {

                    tts = new TTS();
                }
            }
        }
        return tts;

    }

    public boolean speak() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speaktext = ReadTxt.read(Config.offset, Config.offset + Config.len);
                text=text+speaktext;
               int index=text.lastIndexOf("。");
                speaktext=text.substring(0,index+1).trim();
               text=text.substring(index+1).trim();
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("txt",speaktext);
                message.setData(bundle);
                EventBus.getDefault().post(message);
                Config.offset = Config.offset + Config.len;
                int code = mTts.startSpeaking(speaktext, mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                } else {
                }
            }
        }).start();
        return true;
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



    public void stop() {
        try {
            if (mTts != null) {
                mTts.stopSpeaking();
            }
        } catch (Exception e) {
            Log.e("calm", "" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     */
        public void pause(){
            try {
                if (mTts != null) {
                    mTts.pauseSpeaking();
                }
            } catch (Exception e) {
                Log.e("calm", "" + e.getMessage());
                e.printStackTrace();
            }
    }
    /**
     * 倍速
     */
    public String  speed(){
        mTts.stopSpeaking();
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
        mTts.setParameter(SpeechConstant.SPEED, Config.speed);
        mTts.startSpeaking(speaktext, mTtsListener);
       return data;
    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {

            if (code != ErrorCode.SUCCESS) {
                Log.e("calm", "初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");

            } else {
                Log.e("calm", "初始化成功");
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
            Log.d("calm", "开始播放：" + System.currentTimeMillis());
        }

        @Override
        public void onSpeakPaused() {
            Log.d("calm", "暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d("calm", "继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            Log.d("calm", String.format(context.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
//            Log.d("calm", String.format(context.getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.e("calm", "播放完成:"+ System.currentTimeMillis());
            } else if (error != null) {
                Log.e("calm", error.getPlainDescription(true));
            }
            speak();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                Log.d("calm", "session id =" + sid);
            }

            //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
        }
    };

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);

        } else if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerXtts);
        }
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");


    }

    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        String type = "tts";
        if (mEngineType.equals(SpeechConstant.TYPE_XTTS)) {
            type = "xtts";
        }
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        if (mEngineType.equals(SpeechConstant.TYPE_XTTS)) {
            tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voicerLocal + ".jet"));
        } else {
            tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voicerLocal + ".jet"));
        }

        return tempBuffer.toString();
    }

}
