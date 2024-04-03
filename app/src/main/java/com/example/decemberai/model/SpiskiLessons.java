package com.example.decemberai.model;

public class SpiskiLessons {

    public int lessonsId;
    public  String title, img, level, colorFon, colorText;




    public SpiskiLessons(int lessonsId, String title, String img, String level, String colorFon, String colorText) {
        this.lessonsId = lessonsId;
        this.title = title;
        this.img = img;
        this.level = level;
        this.colorFon = colorFon;
        this.colorText = colorText;
    }


    public SpiskiLessons(int lessonsId, String title) {
        this.lessonsId = lessonsId;
        this.title = title;
    }

    public int getLessonsId() {
        return lessonsId;
    }

    public void setLessonsId(int lessonsId) {
        this.lessonsId = lessonsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getColorFon() {
        return colorFon;
    }

    public void setColorFon(String colorFon) {
        this.colorFon = colorFon;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }
}
