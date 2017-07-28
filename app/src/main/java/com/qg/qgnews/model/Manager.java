package com.qg.qgnews.model;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class Manager {
    private  int managerId;     // 管理员Id
    private  String managerAccount;    // 管理员账号
    private  String managerPassword;    // 管理员密码
    private  String managerName;        //管理员姓名
    private  int managerSuper;          //0为普通管理员，1为超级管理员
    private String managerStatus;       //管理员的状态，，“待审批”，“未激活”，“正常”，“被封号”
    private List<News> newsList;        //管理员所对应的新闻
}
