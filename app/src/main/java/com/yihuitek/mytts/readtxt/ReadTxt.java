package com.yihuitek.mytts.readtxt;

import android.util.Log;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.util.ResourceUtil;
import com.yihuitek.mytts.Config;
import com.yihuitek.mytts.MyApplication;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadTxt {
    static int state = 0; // 0:无  1:第   2:第*章 3:第*章 目录

    public void initDataFile() {

    }

    /**
     * 读物固定长度的文本
     *
     * @param from
     * @param to
     * @return
     */
    public static String read(int from, int to) {
        String data = "";
        InputStream is = null;
        BufferedInputStream bis = null;
        String path = Config.filepath;
        byte[] result2 = new byte[to - from];
        try {
            is = MyApplication.context.getResources().getAssets().open(path);
            bis = new BufferedInputStream(is);
            bis.skip(from);
            bis.read(result2, 0, to - from);
            data = new String(result2, "utf-8");
        } catch (FileNotFoundException e) {
            Log.e("calm", "文件异常");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("calm", "文件异常");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {

                }
            }
            return data;
        }

    }

    public static List<String> readChapters() {
        List<String> list = new ArrayList<String>();
        try {
            int off = 0;
            int len = 1000;
            String data;
            do {
                data = read(off, off + len);
                findChapters(data);

                off = off + len;
            } while (data.length() < len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String findChapters(String str) {
        int pos = str.indexOf("------------");
        if (pos < 0) {
            return "";
        }

        String str1 = str.substring(pos);
        int pos1 = str1.indexOf("章");

        if ((pos1 + 20) > str1.length() || (pos1 < 10)) {
            return "";
        }
        String str2 = str1.substring(pos1 - 10, pos1 + 20);
        String str3 = str2.replace("-", "");
        Log.e("calm", "目录:" + str3);
        return str2;
    }

    private static String findChapters2(String str) {

        String mulu = "";

        int pos = str.indexOf("第");
        if (pos < 0) {
            state = 0;
            return "";
        }

        int pos1 = str.indexOf("章");
        if (pos1 < 0) {
            state = 1;
            return "";
        }
        int pos2 = str.indexOf("  ");
        if (pos2 < 0) {
            state = 2;
            return "";
        }
        if ((pos1 - pos) < 10) {//存在“第**章”
            if ((str.length() - pos1) < 30) { //字符串不完整

            } else {//完整
                mulu = str.substring(pos, pos1);
            }
        }else{

        }

        String str1 = str.substring(pos);


        if ((pos1 + 20) > str1.length() || (pos1 < 10)) {
            return "";
        }
        String str2 = str1.substring(pos1 - 10, pos1 + 20);
        String str3 = str2.replace("-", "");
        Log.e("calm", "目录:" + str3);
        return str2;
    }
}
