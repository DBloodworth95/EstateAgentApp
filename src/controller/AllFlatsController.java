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
import model.properties.Flat;
import model.properties.House;
import model.properties.Property;

import java.io.*;
import java.util.ArrayList;

public class AllFlatsController {
    @FXML
    private TableView flatsTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol, idCol;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;
    private ObservableList<Property> rows = FXCollections.observableArrayList();

    public void populateTable() {

        try {
            InputStream fis = new FileInputStream("properties.dat");
            ObjectInput ois = new ObjectInputStream(fis);
            ArrayList<Property> allProperties = null;
            Property property = null;
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

            try {
                while ((allProperties = (ArrayList<Property>) ois.readObject()) != null) {
                    for (Property allProperty : allProperties) {
                        if (allProperty instanceof Flat) {
                            Flat flat = (Flat) allProperty;
                            if (flat.getBranchName().equals(ghostSessionTF.getText()) && flat.getSold().equals("N") || flat.getSold().equals("n")) {
                                System.out.println(flat.getBranchName());
                                rows.add(flat);
                            }
                        }
                    }
                }
            } finally {

                ois.close();
            }
        } catch (EOFException ex) {
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        flatsTV.setItems(rows);
    }

    public void setSessionTF(String name) {
        ghostSessionTF.setText(name);
        ghostSessionTF.setVisible(false);
    }

    public void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
