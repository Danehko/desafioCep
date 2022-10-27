package com.danehko.desafiocep.dao;

import com.danehko.desafiocep.model.Address;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    private final static List<Address> adresses = new ArrayList<>();

    public boolean save(Address address) {
        for (Address example : adresses) {
            if(address.getCep().equals(example.getCep())){
                return false;
            }
        }
        adresses.add(address);
        return true;
    }

    public List<Address> all() {
        return adresses;
    }

    public Address addrees(int i) {
        return adresses.get(i);
    }

    public void remove(Address address) {
        adresses.remove(address);
    }
}
