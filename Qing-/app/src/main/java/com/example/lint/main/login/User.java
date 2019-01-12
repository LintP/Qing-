package com.example.lint.main.login;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser{//扩展了用户类
    private String qq,wechat;
    private BmobFile head;
    private boolean sex;
    private Date birthday;

    public void setQq(String qq){
        this.qq=qq;
    }

    public void setWechat(String wechat){
        this.wechat=wechat;
    }

    public void setHead(BmobFile head){
        this.head=head;
    }

    public void setSex(boolean sex){
        this.sex=sex;
    }

    public void setBirthday(Date birthday){
        this.birthday=birthday;
    }

    public String getQq() {
        return qq;
    }

    public String getWechat() {
        return wechat;
    }

    public BmobFile getHead() {
        return head;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean getSex() {
        return sex;
    }
}
