package com.example.bitkilerinisula.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity
public class PlantModel {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "plant_name")
    public String plantName;

    @ColumnInfo(name = "start_date")
    public String startDate;

    @ColumnInfo(name = "start_time")
    public String startTime;

    @ColumnInfo(name = "routine")
    public String routine;

    @ColumnInfo(name ="photo_url")
    public String photoUrl;


    public PlantModel(int uid, String plantName, String startDate, String startTime, String routine, String photoUrl) {
        this.uid = uid;
        this.plantName = plantName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.routine = routine;
        this.photoUrl = photoUrl;
    }

    public int getUid() {
        return uid;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getRoutine() {
        return routine;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }
}


