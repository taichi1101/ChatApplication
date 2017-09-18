package com.example.android.sample.new3;

/**
 * Created by endoutaichi on 2017/07/07.
 */

public class Attendance {

    public String arrive;
    public String leave;

    public void setLeave(String leave){
        this.leave = leave;
    }


    public void setArrive(String arrive){
        this.arrive = arrive;
    }

    public String getLeave(){
        return leave;
    }

    public String getArrive(){
        return arrive;
    }

}
