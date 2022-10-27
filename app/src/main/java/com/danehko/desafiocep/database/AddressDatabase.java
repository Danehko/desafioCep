package com.danehko.desafiocep.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.danehko.desafiocep.database.dao.RoomAddressDAO;
import com.danehko.desafiocep.model.Address;

@Database(entities = {Address.class}, version = 1, exportSchema = false)
public abstract class AddressDatabase extends RoomDatabase {
    public abstract RoomAddressDAO getRoomAlunoDAO();
}
