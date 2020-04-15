package model;

import model.properties.Property;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class DatPropertyRepository implements PropertyRepository {
    private Path path;


    public DatPropertyRepository(Path path) {
        this.path = path;
    }

    @Override
    public void put(Property property) {
        try (ObjectOutput oos = new ObjectOutputStream(new FileOutputStream(String.valueOf(path.resolve(property.getBranchName() + property.getId()))))) {
            oos.writeObject(property);
        } catch (IOException e) {
            System.out.println("Warning! Error when writing property to file!");
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String name) {

    }

    @Override
    public List<Property> findAll(String branchName) {
        File directory = Paths.get("property/").toFile();
        File[] files = directory.listFiles();
        Property property = null;
        List<Property> propertyList = new ArrayList<>();
        if(branchName.toLowerCase().equals("admin")) {
            for(File file: files) {
                try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                    try {
                        while ((property = (Property) ois.readObject()) != null) {
                            propertyList.add(property);
                        }
                    } catch (EOFException e) {
                        System.out.println("End of file");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (File file : files) {
                if (file.getPath().contains(branchName)) {
                    try (ObjectInput ois = new ObjectInputStream(new FileInputStream(file))) {
                        try {
                            while ((property = (Property) ois.readObject()) != null) {
                                propertyList.add(property);
                            }
                        } catch (EOFException e) {
                            System.out.println("End of file");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return propertyList;
    }
}
