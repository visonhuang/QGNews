package com.qg.qgnews.model;

/**
 * Created by linzongzhan on 2017/7/31.
 */

public class VisitorDownload {

    private int fileId;
    private String downloader;
    private String downloadTime;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
}
