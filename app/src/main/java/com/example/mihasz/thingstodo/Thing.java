package com.example.mihasz.thingstodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by mihasz on 07.10.17.
 */

public class Thing implements Serializable {
    private String name;
    private PendingIntent pendingIntent;
    private Date date;
    private Date remind_date;
    private int remind_sel;
    private String place;
    private String description;
    private boolean remind;
    private boolean state; //if true -> done


    public Thing(String name, Date date, Date remind_date, String place,String description,int remind_sel) {
        this.name = name;
        this.date = date;
        if(remind_date==null)
            remind=false;
        else
            remind=true;
        this.remind_date = remind_date;
        this.place = place;
        pendingIntent = null;
        this.description=description;
        this.state = false;
        this.date.setSeconds(0);
        this.remind_sel=remind_sel;
    }


    public void setPendingIntent (PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public PendingIntent getPendingIntent () {
        return this.pendingIntent;
    }
    @Override
    public String toString() {
        return name + " " + state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean b) {
        this.state = b;
    }

    // int [] = [dni,godz,min,sek]
    public int [] getTimetoThing() {
        int [] wynik = new int[4];
        wynik[0] =getRemind_date().getDay() - Calendar.getInstance().getTime().getDay();
        wynik[1] =getRemind_date().getHours() - Calendar.getInstance().getTime().getHours();
        wynik[2] =getRemind_date().getMinutes() - Calendar.getInstance().getTime().getMinutes();
        wynik[3] =getRemind_date().getSeconds() - Calendar.getInstance().getTime().getSeconds();
        return  wynik;
    }

    // X dni, Y godz. , Z min , A sek
    public String getTTT(String dniCzyDzien) {
        String outcome = new String();
        int [] ttt = getTimetoThing();
        System.out.println("Przyp. za " + ttt[0] + "dni, " + ttt[1]
        + " godz. " + ttt[2] + " min " + ttt[3] + " s ");
        if (ttt[0]>0) {
            outcome += ttt[0] + " " ;
            if(ttt[0] ==1) outcome+=" dzień";
            else   outcome+=dniCzyDzien;
            outcome+=" ";

        }
        if ((ttt[1]>0)&&(ttt[1] != 24)&&(ttt[0])<2)
        outcome+=ttt[1]+" godz." ;
        if (((ttt)[2]>0)&&((ttt[0]==0)))
            outcome+=ttt[2]+" min." ;
        if ((ttt[3]>0)&&((((ttt[1]==0))&&(ttt[0]==0))||(ttt[0]==0))||(((ttt[1]==24))&&(ttt[0]==0)))
            outcome+=ttt[3]+ " sek.";
        return outcome;

    }

    public int compareTo(Thing thing) {
       int s = this.getDate().compareTo(thing.getDate());
        if (s == 0)
            return this.getName().compareTo(thing.getName());
        else
            return s;

    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRemind_date() {
        return remind_date;
    }

    public void setRemind_date(Date remind_date) {
        this.remind_date = remind_date;
    }

    public int getRemind_sel() {
        return remind_sel;
    }

    public void setRemind_sel(int remind_sel) {
        this.remind_sel = remind_sel;
    }
}
