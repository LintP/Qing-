package com.example.lint.main.center;
import cn.bmob.v3.BmobObject;

public class CenterBomb extends BmobObject implements Comparable<CenterBomb>{
    private String title;
    private String user;
    private String tel;
    private String body;
    private String addr;
    private boolean state;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }


    @Override
    public int compareTo(CenterBomb o) {
        if(this.state==false){
            return -1;
        }else
            return 1;
    }
}
