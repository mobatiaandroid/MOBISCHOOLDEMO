package com.mobatia.naisapp.activities.trips.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mobatia.naisapp.fragments.canteen.database.Canteen;
import com.mobatia.naisapp.fragments.canteen.database.CanteenDao;

@Database(entities ={Trip.class,Canteen.class}, version = 3 ,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao tripDao();
    public abstract CanteenDao canteenDao();
}

