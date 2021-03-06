package com.yihuitek.mytts.okhttp.headerparams;

import java.io.File;

/**
 * Fileparam实体类
 * Created by jlccl on 2017/2/19.
 */

public class FileParam {
    private String key;
    private File file;

    public FileParam(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
