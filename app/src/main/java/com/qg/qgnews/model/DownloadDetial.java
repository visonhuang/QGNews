package com.qg.qgnews.model;

/**
 * Created by 小吉哥哥 on 2017/7/31.
 */

public class DownloadDetial {
    private String downloader;
    private int fileId;
    private String downloadTime;

    public int getFileId() {
        return fileId;
    }

    public String getDownloader() {
        return downloader;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
}
