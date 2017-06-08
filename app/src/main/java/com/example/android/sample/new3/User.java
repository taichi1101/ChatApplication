package com.example.android.sample.new3;

/**
 * Created by endoutaichi on 2017/05/27.
 */


import java.util.Date;

public class User {
    long id;

    String data;
    String username;
    String comment;
    String idnumber;



    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
