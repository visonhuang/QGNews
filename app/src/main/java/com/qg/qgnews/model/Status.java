package com.qg.qgnews.model;

import com.qg.qgnews.util.Tool;

/**
 * Created by linzongzhan on 2017/7/31.
 */

public class Status {
    public static void statusResponse (int status) {
        if (status == 1) {

        } else if (status == 2) {
            Tool.toast("邮箱已存在");
        } else if (status == 3) {
            Tool.toast("邮箱不存在");
        } else if (status == 4) {
            Tool.toast("邮箱为空");
        } else if (status == 5) {
            Tool.toast("密码为空");
        } else if (status == 6) {
            Tool.toast("邮箱格式不正确");
        } else if (status == 8) {
            Tool.toast("密码错误");
        } else if (status == 9) {
            Tool.toast("账户未激活");
        } else if (status == 10) {
            Tool.toast("账户未审批");
        } else if (status == 11) {
            Tool.toast("账户已被封");
        } else if (status == 12) {
            Tool.toast("新闻标题为空");
        } else if (status == 13) {
            Tool.toast("新闻主题内容为空");
        } else if (status == 14) {
            Tool.toast("新闻作者为空");
        } else if (status == 15) {
            Tool.toast("新闻封面为空");
        }
    }
}
