//Interface for our Property Repository.
//With this it is possible to create Repositories using many data sources
//such as files, database etc.
package model.repositories;

import model.properties.Property;

import java.util.List;

public interface PropertyRepository {
    void put(Property property);

    void remove(int id);

    List<Property> findByBranch(String name);

    List<Property> findAll();
}
