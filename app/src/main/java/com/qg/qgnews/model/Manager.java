package com.qg.qgnews.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class Manager implements Serializable {
    private  int managerId;     // 管理员Id
    private  String managerAccount;    // 管理员账号
    private  String managerPassword;    // 管理员密码
    private  String managerName;        //管理员姓名
    private  int managerSuper;          //0为普通管理员，1为超级管理员
    private String managerStatus;       //管理员的状态，，“待审批”，“未激活”，“正常”，“被封号”
    private List<News> newsList;        //管理员所对应的新闻

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getManagerAccount() {
        return managerAccount;
    }

    public void setManagerAccount(String managerAccount) {
        this.managerAccount = managerAccount;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getManagerSuper() {
        return managerSuper;
    }

    public void setManagerSuper(int managerSuper) {
        this.managerSuper = managerSuper;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "managerId=" + managerId +
                ", managerAccount='" + managerAccount + '\'' +
                ", managerPassword='" + managerPassword + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerSuper=" + managerSuper +
                ", managerStatus='" + managerStatus + '\'' +
                ", newsList=" + newsList +
                '}';
    }
}
