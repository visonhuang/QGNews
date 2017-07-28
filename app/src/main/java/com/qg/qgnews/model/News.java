package com.qg.qgnews.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class News implements Serializable{
    private  int newsId;     // 新闻Id
    private String managerName ; // 发布新闻的人
    private  String newsTitle ;     // 新闻标题
    private String newsBody;     //新闻主要内容
    private String newsAuthor;      // 新闻作者
    private  String newsTime ;      //新闻发布时间
    private String newsFace;        //新闻封面路径
    private String filesUuid;     //新闻UuId
    private List<ViceFile> fileList;//一个新闻对应的附件集合

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public void setNewsAuthor(String newsAuthor) {
        this.newsAuthor = newsAuthor;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsFace() {
        return newsFace;
    }

    public void setNewsFace(String newsFace) {
        this.newsFace = newsFace;
    }

    public String getFilesUuid() {
        return filesUuid;
    }

    public void setFilesUuid(String filesUuid) {
        this.filesUuid = filesUuid;
    }

    public List<ViceFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<ViceFile> fileList) {
        this.fileList = fileList;
    }
}
