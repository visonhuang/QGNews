package com.qg.qgnews.model;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class News {
    private  int newsId;     // 新闻Id
    private String managerName ; // 发布新闻的人
    private  String newsTitle ;     // 新闻标题
    private String newsAuthor;      // 新闻作者
    private  String newsTime ;      //新闻发布时间
    private String newsFace;        //新闻封面路径
    private String filesUuid;     //新闻UuId
    private List<ViceFile> fileList;   //一个新闻对应的附件集合
}
