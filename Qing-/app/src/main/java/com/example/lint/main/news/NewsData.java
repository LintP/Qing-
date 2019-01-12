package com.example.lint.main.news;

public class NewsData {


    /** json
     * uniquekey": "1a5a20441de022dfd1a8100ccd3c7d83",
     "title": "2019惠民新政有哪些？",
     "date": "2019-01-05 11:20",
     "category": "国内",
     "author_name": "解放网",
     "url": "http://mini.eastday.com/mobile/190105112042060.html",
     "thumbnail_pic_s": "http://01imgmini.eastday.com/mobile/20190105/20190105112042_12acbb6f4570574b72aed0aec0e5ed98_1_mwpm_03200403.jpg"
     * @return
     */

    public String uniquekey,title,date,category,author_name,url,thumbnail_pic_s;
    public String id;
    public String getUniquekey() {
        return uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }
}
