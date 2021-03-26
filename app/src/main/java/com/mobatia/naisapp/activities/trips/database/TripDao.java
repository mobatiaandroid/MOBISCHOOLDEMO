package com.mobatia.naisapp.activities.trips.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip")
    List<Trip> getAll();

    @Query("SELECT * FROM Trip WHERE trip_id = :trip_id AND student_id = :student_id AND users_id = :users_id ")
    public Trip getTripWithId(String trip_id, String student_id, String users_id);

    @Query("DELETE FROM Trip WHERE order_id = :order_id AND users_id = :users_id")
    public void deleteTripWithOrderId(String order_id, String users_id);

    @Query("DELETE FROM Trip WHERE trip_id = :trip_id AND order_id = :student_id AND users_id = :users_id")
    public void deleteTripWithStudentId(String trip_id, String student_id, String users_id);

    @Query("SELECT * FROM Trip WHERE status = :status AND student_id = :student_id  AND users_id = :users_id")
    public List<Trip>  getTripUnsyncWithId(String status, String student_id, String users_id);

    @Query("SELECT * FROM Trip WHERE status = :status AND users_id = :users_id")
    public List<Trip>  getTripUnsync(String status, String users_id);

    @Insert
    void insert(Trip task);

    @Delete
    void delete(Trip task);

    @Update
    void update(Trip status);

}
