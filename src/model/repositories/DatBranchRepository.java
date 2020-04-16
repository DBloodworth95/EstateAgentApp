package model.repositories;

import model.Branch;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatBranchRepository implements BranchRepository {
    private Path path;
    //Constructs an instance of Repository, takes a path as an argument so this can be passed to the file read/writing methods.
    public DatBranchRepository(Path path) {
        this.path = path;
    }
    //Writes instances of a Branch into it's own file.
    @Override
    public void put(Branch branch) {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream(String.valueOf(path.resolve(branch.getName()))))) {
            oos.writeObject(branch);
        } catch (IOException e) {
            System.out.println("Warning! Error when writing property to file!");
            e.printStackTrace();
        }
    }
    //Removes a Branch file based on the ID given.
    //Loops through each file from the given file path when constructing the Repository instance.
    //Searches through each file found for any Branch instances.
    //If a Branch instance is found, check the ID with the ID given.
    //If true, delete file.
    @Override
    public void remove(Integer id) throws IOException, ClassNotFoundException {
        File directory = Paths.get(path.toString()).toFile();
        File[] files = directory.listFiles();
        Branch foundBranch = null;
        for (File file : files) {
            try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                while ((foundBranch = (Branch) ois.readObject()) != null) {
                    if (foundBranch.getId() == id) {
                        file.delete();
                    }
                    break;
                }
            } catch(EOFException e) {
                System.out.println("End of file");
            }
        }
    }
    //Removes all Branches from the system. Useful for updates as you can overwrite files using this.
    //Loops through each file from the given file path when constructing the Repository instance.
    //Searches through each file found for any Branch instances.
    //If a Branch instance is found delete the file.
    @Override
    public void removeAll() throws IOException, ClassNotFoundException {
        File directory = Paths.get(path.toString()).toFile();
        File[] files = directory.listFiles();
        Branch foundBranch = null;
        for (File file : files) {
            try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                while ((foundBranch = (Branch) ois.readObject()) != null) {
                        file.delete();
                }
                break;
            } catch(EOFException e) {
                System.out.println("End of file");
            }
        }
    }
    //Finds all Branches that are in files within the given file path when constructing the Repository.
    //Collates all found Branches into a List.
    //Returns the List.
    @Override
    public List<Branch> findAll() {
        File directory = Paths.get(path.toString()).toFile();
        File[] files = directory.listFiles();
        Branch branch = null;
        List<Branch> branchList = new ArrayList<>();
        for(File file: files) {
            try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                try {
                    while ((branch = (Branch) ois.readObject()) != null) {
                        branchList.add(branch);
                        break;
                    }
                } catch (EOFException e) {
                    System.out.println("End of file");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return branchList;
    }
    //Finds a branch based on name and password. Useful for validating a login.
    //Calls findAll(); to return a list of all Branches.
    //Loop through the list and find any branches that match the username and password given.
    //If true, return branch instance, else return null.
    @Override
    public Branch find(String name, String password) {
        List<Branch> branches = findAll();
        for(Branch b: branches) {
            if (b.getName().toLowerCase().equals(name.toLowerCase()) && b.getPassword().toLowerCase().equals(password.toLowerCase()))
                return b;
        }
        return null;
    }
}
