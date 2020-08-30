package com.sibk.tasik.Model;

public class DashboardModel {
    String title;
    String img;
    int idDs;

    public int getIdDs() {
        return idDs;
    }

    public void setIdDs(int idDs) {
        this.idDs = idDs;
    }

    public DashboardModel(String title, String img, int idDs){
        this.title = title;
        this.img = img;
        this.idDs = idDs;

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


}
