package com.example.bitkilerinisula.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bitkilerinisula.model.PlantModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PlantDao {
    @Query("SELECT * FROM plantmodel")
    List<PlantModel> getAll();

    @Insert
    void insertAll(PlantModel... plantModels);

    @Query("DELETE FROM plantmodel WHERE uid = :id")
    void delete(int id);

    @Update
    void update(PlantModel plantModel);

}
