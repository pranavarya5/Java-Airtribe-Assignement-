package com.ridewise.service;

import com.ridewise.model.Location;
import com.ridewise.model.Rider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderService {
    private final Map<String, Rider> riders = new HashMap<>();

    public Rider registerRider(String id, String name, Location location) {
        if (riders.containsKey(id)) {
            throw new IllegalArgumentException("Rider with ID '" + id + "' already exists.");
        }
        Rider rider = new Rider(id, name, location);
        riders.put(id, rider);
        return rider;
    }

    public Rider getRiderById(String id) {
        return riders.get(id);
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders.values());
    }
}
