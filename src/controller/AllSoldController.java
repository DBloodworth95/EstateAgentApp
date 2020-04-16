package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DatPropertyRepository;
import model.properties.Flat;
import model.properties.House;
import model.properties.Property;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllSoldController {
    @FXML
    private TableView soldTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol, idCol;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;
    private ObservableList<Property> rows = FXCollections.observableArrayList();
    private DatPropertyRepository datPropertyRepository = new DatPropertyRepository(Paths.get("property"));

    //Populates the table for the All Sold window, setup each column to accept the appropriate field from a sold property.
    //Read the properties.dat file and read the properties ArrayList, look for any properties in the list that are an instance of house or flat.
    //Check that the branch name of the house/flat matches the branch of the user logged in, and the flat/house is sold.
    //If true then add the property as a row in the table.
    public void populateTable(String branchName) {
        List<Property> allBranchProperties = datPropertyRepository.findByBranch(branchName);
        List<Property> allProperties = datPropertyRepository.findAll();
        addressCol.setCellValueFactory(new PropertyValueFactory<Property, String>("address"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Property, String>("roomAmount"));
        sellPrCol.setCellValueFactory(new PropertyValueFactory<Property, Double>("sellPrice"));
        soldPrCol.setCellValueFactory(new PropertyValueFactory<Property, Double>("soldPrice"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Property, String>("type"));
        soldCol.setCellValueFactory(new PropertyValueFactory<Property, String>("sold"));
        floorsCol.setCellValueFactory(new PropertyValueFactory<House, String>("floorAmount"));
        gardenCol.setCellValueFactory(new PropertyValueFactory<House, String>("garden"));
        garageCol.setCellValueFactory(new PropertyValueFactory<House, String>("garage"));
        monthCol.setCellValueFactory(new PropertyValueFactory<Flat, Double>("monthlyRate"));
        floorCol.setCellValueFactory(new PropertyValueFactory<Flat, String>("floorNumber"));
        idCol.setCellValueFactory(new PropertyValueFactory<Property, Integer>("id"));
        if(isAdmin()) {
            for (Property property : allProperties) {
                if (property instanceof House) {
                    House house = (House) property;
                    if (house.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("y")) {
                            rows.add(house);
                        }
                    }
                }
                if (property instanceof Flat) {
                    Flat flat = (Flat) property;
                    if (property.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (property.getSold().toLowerCase().equals("y")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        } else {
            for (Property property : allBranchProperties) {
                if (property instanceof House) {
                    House house = (House) property;
                    if (house.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("y")) {
                            rows.add(house);
                        }
                    }
                }
                if (property instanceof Flat) {
                    Flat flat = (Flat) property;
                    if (property.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (property.getSold().toLowerCase().equals("y")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        }
        soldTV.setItems(rows);
    }
    //Sets an invisible TextField as the username of the user logged in, this can then be used as a reference when populating the table.
    public void setSessionTF(String name) {
        ghostSessionTF.setText(name);
        ghostSessionTF.setVisible(false);
    }
    //Closes the window.
    public void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    //Checks if the user is an admin.
    private boolean isAdmin() {
        return ghostSessionTF.getText().toLowerCase().equals("admin");
    }
}
