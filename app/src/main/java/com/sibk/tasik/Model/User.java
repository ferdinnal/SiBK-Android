package com.sibk.tasik.Model;

public class User {
    private int userid;
    private String fullname;
    private String email;
    private int usertypeid;

    public int getuserid() {
        return userid;
    }

    public void setuserid(int userid) {
        this.userid = userid;
    }

    public String getfullname() {
        return fullname;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public int getusertypeid() {
        return usertypeid;
    }

    public void setusertypeid(int usertypeid) {
        this.usertypeid = usertypeid;
    }


}
