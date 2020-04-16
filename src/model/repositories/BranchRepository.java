//Interface for our Branch Repository.
//With this it is possible to create Repositories using many data sources
//such as files, database etc.

package model.repositories;

import model.Branch;
import java.io.IOException;
import java.util.List;

public interface BranchRepository {

    void put(Branch branch);

    void remove(Integer id) throws IOException, ClassNotFoundException;

    void removeAll() throws IOException, ClassNotFoundException;

    List<Branch> findAll();

    Branch find(String name, String password);
}
