package com.qg.qgnews.model;

import java.io.Serializable;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class ViceFile implements Serializable {
    private int fileId;     //附件Id
    private int newsId;     //附件对应的新闻Id
    private String fileName;   //附件名称
    private String filePath;     //文件保存路径
    private String fileDownLoadTime;   //附件上传时间
    private String filesUuId;      //附件的UuId；

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDownLoadTime() {
        return fileDownLoadTime;
    }

    public void setFileDownLoadTime(String fileDownLoadTime) {
        this.fileDownLoadTime = fileDownLoadTime;
    }

    public String getFilesUuId() {
        return filesUuId;
    }

    public void setFilesUuId(String filesUuId) {
        this.filesUuId = filesUuId;
    }
}
