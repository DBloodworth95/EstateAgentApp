package model;

import model.properties.Property;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatBranchRepositoryTest {
    @Test
    public void testPut() throws Exception {
        List<Property> testList = new ArrayList<>();
        Branch branch = new Branch(999, "TestBranch", "Test", "Test", "Test", "test", "test", "Test",
        "Test", testList);
        DatBranchRepository datBranchRepository = new DatBranchRepository(Paths.get("test_files"));
        datBranchRepository.put(branch);
        //Checks the file has been added.
        Assert.assertTrue(Files.exists(Paths.get("test_files").resolve("TestBranch")));
        Branch branchInFile = null;
        File directory = Paths.get("test_files/TestBranch").toFile();
        //Checks the file has been serialized properly.
        try (ObjectInput ois = new ObjectInputStream(new FileInputStream(directory))) {
            branchInFile = (Branch) ois.readObject();
        }
        Assert.assertEquals(branchInFile.getName(), branch.getName());
        //Delete file for future test runs.
        Files.delete(Paths.get("test_files").resolve("TestBranch"));
    }

    @Test
    public void testRemove() throws Exception {
        List<Property> testList = new ArrayList<>();
        Branch branch = new Branch(999, "TestBranch", "Test", "Test", "Test", "test", "test", "Test",
                "Test", testList);
        DatBranchRepository datBranchRepository = new DatBranchRepository(Paths.get("test_files"));
        datBranchRepository.put(branch);
        datBranchRepository.remove(branch.getId());
        //Checks the file has been removed.
        Assert.assertFalse(Files.exists(Paths.get("test_files").resolve("TestBranch")));
    }

    @Test
    public void testRemoveAll() throws Exception {
        List<Property> testList = new ArrayList<>();
        Branch branch = new Branch(999, "TestBranch", "Test", "Test", "Test", "test", "test", "Test",
                "Test", testList);
        DatBranchRepository datBranchRepository = new DatBranchRepository(Paths.get("test_files"));
        datBranchRepository.put(branch);
        datBranchRepository.removeAll();
        //Checks the file has been removed.
        Assert.assertFalse(Files.exists(Paths.get("test_files").resolve("TestBranch")));
    }

    @Test
    public void testFindAll() throws Exception {
        List<Property> dummyBranchList = new ArrayList<>();
        List<Branch> foundList = new ArrayList<>();
        Branch branch = new Branch(999, "TestBranch", "Test", "Test", "Test", "test", "test", "Test",
                "Test", dummyBranchList);
        DatBranchRepository datBranchRepository = new DatBranchRepository(Paths.get("test_files"));
        datBranchRepository.put(branch);
        foundList.add(branch);

        Assert.assertEquals(datBranchRepository.findAll(), foundList);
        Files.delete(Paths.get("test_files").resolve("TestBranch"));
    }
}
