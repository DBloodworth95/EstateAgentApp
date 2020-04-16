package model;

import model.properties.Property;

import java.util.List;

public interface PropertyRepository {
    void put(Property property);

    void remove(int id);

    List<Property> findByBranch(String name);

    List<Property> findAll();
}
