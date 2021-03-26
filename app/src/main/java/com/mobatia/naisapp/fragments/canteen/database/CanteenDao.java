package com.mobatia.naisapp.fragments.canteen.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CanteenDao {
    @Query("SELECT * FROM Canteen")
    List<Canteen> getAll();

    @Query("SELECT * FROM Canteen WHERE student_id = :student_id AND users_id = :users_id ")
    public Canteen getTripWithId(String student_id, String users_id);

    @Query("DELETE FROM Canteen WHERE keycode = :order_id AND users_id = :users_id")
    public void deleteTripWithOrderId(String order_id, String users_id);

    @Query("DELETE FROM Canteen WHERE  keycode = :student_id AND users_id = :users_id")
    public void deleteTripWithStudentId(String student_id, String users_id);

    @Query("SELECT * FROM Canteen WHERE status = :status AND student_id = :student_id  AND users_id = :users_id AND amount = :amount AND keycode = :keycode ")
    public List<Canteen>  getTripUnsyncWithId(String status, String student_id, String users_id, String amount, String keycode );

    @Query("SELECT * FROM Canteen WHERE student_id = :student_id AND users_id = :users_id AND amount = :amount AND keycode = :order_id ")
    public List<Canteen>  getCateenUnsyncWithId(String student_id, String users_id, String amount, String order_id );

    @Query("SELECT * FROM Canteen WHERE status = :status AND users_id = :users_id")
    public List<Canteen>  getTripUnsync(String status, String users_id);

    @Insert
    void insert(Canteen task);

    @Delete
    void delete(Canteen task);

    @Update
    void update(Canteen status);

}
