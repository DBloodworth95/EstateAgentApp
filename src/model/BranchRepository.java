package model;

import java.io.IOException;
import java.util.List;

public interface BranchRepository {
    void put(Branch branch);

    void remove(Integer id) throws IOException, ClassNotFoundException;

    void removeAll() throws IOException, ClassNotFoundException;

    List<Branch> findAll();
}
