package com.yihuitek.mytts.readtxt;

import android.util.Log;

import com.iflytek.cloud.util.ResourceUtil;
import com.yihuitek.mytts.Config;
import com.yihuitek.mytts.MyApplication;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadTxt {
    public void initDataFile(){


    }
    public static String read( int from, int to) {
        String data = "";
        InputStream is=null;
        BufferedInputStream bis=null;
        String path=Config.filepath;
        byte[] result2 = new byte[Config.len];
        try {
            Config.offess = to;
             is = MyApplication.context.getResources().getAssets().open(path);
              bis = new BufferedInputStream(is);
              bis.skip(from);
            bis.read(result2, 0, Config.len);
             data = new String(result2, "utf-8");
         } catch (FileNotFoundException e) {
            Log.e("calm","文件异常");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("calm","文件异常");
            e.printStackTrace();
        } finally {
            if (is!=null){
                try {
                    is.close();
                }catch (Exception e){

                }
            }
            if (bis!=null){
                try {
                    bis.close();
                }catch (Exception e){

                }
            }
            return data;
        }

    }
}
