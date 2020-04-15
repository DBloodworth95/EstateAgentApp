package model;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatBranchRepository implements BranchRepository {
    private Path path;

    public DatBranchRepository(Path path) {
        this.path = path;
    }

    @Override
    public void put(Branch branch) {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream(String.valueOf(path.resolve(branch.getName()))))) {
            oos.writeObject(branch);
        } catch (IOException e) {
            System.out.println("Warning! Error when writing property to file!");
            e.printStackTrace();
        }
    }

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
                }
            } catch(EOFException e) {
                System.out.println("End of file");
            }
        }
    }

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
            } catch(EOFException e) {
                System.out.println("End of file");
            }
        }
    }

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
}
