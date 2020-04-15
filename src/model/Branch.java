package model;

import model.properties.Property;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Branch implements Serializable {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String webAddress;
    private String username;
    private String password;
    private String accessLevel;
    private List<Property> propertyList;

    public Branch(int id, String name, String address, String phoneNumber, String email, String webAddress, String username, String password, String accessLevel, List<Property> propertyList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.webAddress = webAddress;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.propertyList = propertyList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }
}
