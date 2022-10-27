package com.danehko.desafiocep.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.danehko.desafiocep.model.Address;

import java.util.List;

@Dao
public interface RoomAddressDAO {

    @Insert
    void save(Address local);

    @Delete
    void remove(Address chosenAddress);

    @Query("SELECT * FROM Address")
    List<Address> all();

    @Query("SELECT count(*)!=0 FROM Address WHERE cep = :cepTest")
    boolean containsPrimaryKey(String cepTest);
}
