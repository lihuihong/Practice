package com.example.administrator.practice.bean;

    /**
     * Created by Administrator on 2017/5/19.
     * 日记实体类
     */

    public class DiaryBean {
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private String date;
        private String title;
        private String content;
        private String tag;

        public DiaryBean(String date, String title, String content) {
            this.date = date;
            this.title = title;
            this.content = content;
        }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
