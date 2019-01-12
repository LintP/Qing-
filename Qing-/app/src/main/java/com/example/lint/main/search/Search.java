package com.example.lint.main.search;

public class Search {
    private String url,info;
    private int imageID;

    public Search(String url,String info,int id){
        this.url=url;
        this.info=info;
        this.imageID=id;
    }

    public String getUrl() {
        return url;
    }

    public String getInfo() {
        return info;
    }

    public int getImageID() {
        return imageID;
    }
}
