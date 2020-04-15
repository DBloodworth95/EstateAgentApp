package model;

import model.properties.Property;

import java.util.List;

public interface PropertyRepository {
    void put(Property property);

    void remove(String name);

    List<Property> findAll(String name);
}
