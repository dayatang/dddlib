package org.dddlib.image.api.pojo;

import java.io.Serializable;

/**
 * Created by lingen on 14-7-16.
 * 代表一个图片文件
 */
public class ImageFile implements Serializable{

    /**
     * 图片文件的byte[]内容
     */
    private byte[] content;

    /**
     * 图片文件名
     */
    private String fileName;


    public ImageFile() {

    }

    public ImageFile(byte[] content,String fileName){
        this.content = content;
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
