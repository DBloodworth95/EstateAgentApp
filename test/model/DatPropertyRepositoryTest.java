package model;

import model.properties.Flat;
import model.repositories.DatPropertyRepository;
import org.junit.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatPropertyRepositoryTest {

    @org.junit.Test
    public void testPut() throws Exception {
        Flat flat = new Flat(1, "branch", "test", "N", "flat", 1, 200, 200, 1, 200, "N" +
                "N", "N", 2);
        DatPropertyRepository datPropertyRepository = new DatPropertyRepository(Paths.get("test_files"));
        datPropertyRepository.put(flat);
        //Checks the file has been added.
        Assert.assertTrue(Files.exists(Paths.get("test_files").resolve("branch1")));
        Flat flatInFile = null;
        File directory = Paths.get("test_files/branch1").toFile();
        //Checks the file has been serialized properly.
        try (ObjectInput ois = new ObjectInputStream(new FileInputStream(directory))) {
            flatInFile = (Flat) ois.readObject();
        }
        Assert.assertEquals(flatInFile.getBranchName(), flat.getBranchName());
        //Delete file for future test runs.
        Files.delete(Paths.get("test_files").resolve("branch1"));
    }
}
