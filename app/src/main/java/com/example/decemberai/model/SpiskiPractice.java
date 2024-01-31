package com.example.decemberai.model;

public class SpiskiPractice {
    int id, poleZapasnoe;
    String title, imgSmall, imgBig, level, color;

    public SpiskiPractice(int id, int poleZapasnoe, String title, String imgSmall, String imgBig, String level, String color) {
        this.id = id;
        this.poleZapasnoe = poleZapasnoe;
        this.title = title;
        this.imgSmall = imgSmall;
        this.imgBig = imgBig;
        this.level = level;
        this.color = color;
    }

    public int getPoleZapasnoe() {
        return poleZapasnoe;
    }

    public void setPoleZapasnoe(int poleZapasnoe) {
        this.poleZapasnoe = poleZapasnoe;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
