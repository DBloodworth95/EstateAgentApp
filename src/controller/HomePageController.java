package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import model.properties.Flat;
import model.properties.House;
import model.properties.Property;

import java.io.*;
import java.util.ArrayList;

public class HomePageController {
    @FXML
    private Text loggedInTF, titleTF, adminTF, ghostSessionTF;
    @FXML
    private Button addBrBtn, editBrBtn;
    @FXML
    private TableView homeTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol;
    @FXML
    private VBox adminPanelHBox;

    public void setLoggedInTF(Session session) {
        loggedInTF.setText("Logged in as " + session.getUsername() + " branch secretary");
    }

    public void setTitleTF(Session session) {
        if (session.getAccessLevel() != "All") {
            titleTF.setText("All properties at Branch: " + session.getUsername());
        } else {
            titleTF.setText("All properties at all Branches.");
        }
    }

    public void openAddBranchView() throws IOException {
        FXMLLoader addBranchLoader = new FXMLLoader();
        addBranchLoader.setLocation(getClass().getResource("/view/addBranchView.fxml"));
        Scene scene = new Scene(addBranchLoader.load(), 491,426);
        Stage stage = new Stage();
        stage.setTitle("Add a Branch!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void openEditBranchView() throws Exception {
        FXMLLoader editBranchLoader = new FXMLLoader();
        editBranchLoader.setLocation(getClass().getResource("/view/editBranchView.fxml"));
        Scene scene = new Scene(editBranchLoader.load(), 718,463);
        Stage stage = new Stage();
        stage.setTitle("Edit a Branch!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = editBranchLoader.getController();
        EditBranchController controller = (EditBranchController) temp;
        controller.populateEditTable();
    }

    public void hideAdminPanel(Session session) {
        if (session.getAccessLevel() != "All") {
            adminPanelHBox.setVisible(false);
            adminTF.setVisible(false);
            addBrBtn.setVisible(false);
            editBrBtn.setVisible(false);
        }
    }

    public void populateTable() {
        ObservableList<Property> rows = FXCollections.observableArrayList();
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

            try {
                while ((allProperties = (ArrayList<Property>) ois.readObject()) != null) {
                    for (int i = 0; i < allProperties.size(); i++) {
                        if (property instanceof House) {
                            House house = (House) allProperties.get(i);
                            rows.add(house);

                        } else {
                            Property flat = allProperties.get(i);
                            rows.add(flat);
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

        homeTV.setItems(rows);
        homeTV.setEditable(true);
        roomCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sellPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        soldCol.setCellFactory(TextFieldTableCell.forTableColumn());
        floorsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        floorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        garageCol.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));
        gardenCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        soldPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    }

    public void openAddHouse() throws IOException {
        FXMLLoader addHouseLoader = new FXMLLoader();
        addHouseLoader.setLocation(getClass().getResource("/view/addHouseView.fxml"));
        Scene scene = new Scene(addHouseLoader.load(), 600,540);
        Stage stage = new Stage();
        stage.setTitle("Add a House!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = addHouseLoader.getController();
        AddHouseController controller = (AddHouseController) temp;
        controller.setSessionTF(getSessionName());
    }

    public void setGhostSession(Session session) {
        ghostSessionTF.setText(session.getUsername());
        ghostSessionTF.setVisible(false);
    }

    public String getSessionName() {
        String name = ghostSessionTF.getText();
        return name;
    }
}
