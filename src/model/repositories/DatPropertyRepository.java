package model.repositories;

import model.properties.Property;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatPropertyRepository implements PropertyRepository {
    private Path path;
    //Constructs an instance of Repository, takes a path as an argument so this can be passed to the file read/writing methods.
    public DatPropertyRepository(Path path) {
        this.path = path;
    }
    //Writes instances of a Property into it's own file.
    @Override
    public void put(Property property) {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream(String.valueOf(path.resolve(property.getBranchName() + property.getId()))))) {
            oos.writeObject(property);
        } catch (IOException e) {
            System.out.println("Warning! Error when writing property to file!");
            e.printStackTrace();
        }
    }
    //Removes a Property file based on the ID given.
    //Loops through each file from the given file path when constructing the Repository instance.
    //Searches through each file found for any Property instances.
    //If a Property instance is found, check the ID with the ID given.
    //If true, delete file.
    @Override
    public void remove(int id) {
        File directory = Paths.get(path.toString()).toFile();
        File[] files = directory.listFiles();
        Property foundProperty = null;
        for (File file : files) {
            try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                while ((foundProperty = (Property) ois.readObject()) != null) {
                    if (foundProperty.getId() == id) {
                        file.delete();
                        break;
                    }
                }
            } catch(EOFException | FileNotFoundException e) {
                System.out.println("End of file");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //Finds a property based on branch name.
    //Loops through the file directory.
    //Due to the naming conventions, it is possible to check if a file contains a certain branch name.
    //If true, add the property to the list.
    //Once finished, return the list of properties.
    @Override
    public List<Property> findByBranch(String branchName) {
        File directory = Paths.get("property/").toFile();
        File[] files = directory.listFiles();
        Property property = null;
        List<Property> propertyList = new ArrayList<>();
            for (File file : files) {
                if (file.getPath().contains(branchName)) {
                    try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                        try {
                            while ((property = (Property) ois.readObject()) != null) {
                                propertyList.add(property);
                                break;
                            }
                        } catch (EOFException e) {
                            System.out.println("End of file");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        return propertyList;
    }
    //Finds all Properties that are in files within the given file path when constructing the Repository.
    //Collates all found Properties into a List.
    //Returns the List.
    @Override
    public List<Property> findAll() {
        File directory = Paths.get("property/").toFile();
        File[] files = directory.listFiles();
        Property property = null;
        List<Property> propertyList = new ArrayList<>();
        for (File file : files) {
            try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                try {
                    while ((property = (Property) ois.readObject()) != null) {
                        propertyList.add(property);
                        break;
                    }
                } catch (EOFException e) {
                    System.out.println("End of file");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return propertyList;
    }
}
