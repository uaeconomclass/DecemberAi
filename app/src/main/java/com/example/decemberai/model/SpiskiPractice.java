package com.example.decemberai.model;

public class SpiskiPractice {

    public int practiceId;
    public  String title, img, level, colorFon, colorText;




    public SpiskiPractice(int practiceId, String title, String img, String level, String colorFon, String colorText) {
        this.practiceId = practiceId;
        this.title = title;
        this.img = img;
        this.level = level;
        this.colorFon = colorFon;
        this.colorText = colorText;
    }


    public SpiskiPractice(int practiceId, String title) {
        this.practiceId = practiceId;
        this.title = title;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
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
