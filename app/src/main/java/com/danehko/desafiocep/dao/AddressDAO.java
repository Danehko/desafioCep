package com.danehko.desafiocep.dao;

import com.danehko.desafiocep.model.Address;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    private final static List<Address> adresses = new ArrayList<>();

    public void save(Address address) {
        adresses.add(address);
    }

    public List<Address> all() {
        return adresses;
    }

    public Address addrees(int i) {
        return adresses.get(i);
    }

}
