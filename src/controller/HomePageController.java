package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import model.properties.Flat;
import model.properties.House;
import model.properties.Property;
import model.repositories.DatPropertyRepository;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomePageController {
    @FXML
    private Text loggedInTF, titleTF, adminTF, ghostSessionTF;
    @FXML
    private Button addBrBtn, editBrBtn, logoutBtn;
    @FXML
    private TableView homeTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol, idCol;
    @FXML
    private VBox adminPanelHBox;
    @FXML
    private TextField searchTF;
    private ArrayList<Property> properties = new ArrayList<>();
    private DatPropertyRepository propertyRepository = new DatPropertyRepository(Paths.get("property"));
    //Fetches the username of the session, sets the loggedin Textfield to the users name to let them know who's signed in.
    public void setLoggedInTF(Session session) {
        if(session.isAdmin()) {
            loggedInTF.setText("Logged in as " + session.getBranch().getUsername() + " branch secretary");
        } else {
            loggedInTF.setText("Logged in as admin");
        }
    }
    //Sets the main title of the window based on the current sesssion's username.
    public void setTitleTF(Session session) {
        if (session.isAdmin()) {
            titleTF.setText("All properties for sale at Branch: " + session.getBranch().getUsername());
        } else {
            titleTF.setText("All properties for sale at all Branches.");
        }
    }
    //Opens the window to add a branch.
    public void openAddBranchView() throws IOException {
        FXMLLoader addBranchLoader = new FXMLLoader();
        addBranchLoader.setLocation(getClass().getClassLoader().getResource("view/addBranchView.fxml"));
        Scene scene = new Scene(addBranchLoader.load(), 491,426);
        Stage stage = new Stage();
        stage.setTitle("Add a Branch!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    //Opens the window to edit a branch.
    public void openEditBranchView() throws Exception {
        FXMLLoader editBranchLoader = new FXMLLoader();
        editBranchLoader.setLocation(getClass().getClassLoader().getResource("view/editBranchView.fxml"));
        Scene scene = new Scene(editBranchLoader.load(), 823,463);
        Stage stage = new Stage();
        stage.setTitle("Edit a Branch!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = editBranchLoader.getController();
        EditBranchController controller = (EditBranchController) temp;
        controller.populateEditTable();
    }
    //This gets called when login is successfull.
    //Check the access level of the user logged in, if the access level isn't "all" then hide the admin panel.
    public void hideAdminPanel(Session session) {
        if (session.isAdmin()) {
            adminPanelHBox.setVisible(false);
            adminTF.setVisible(false);
            addBrBtn.setVisible(false);
            editBrBtn.setVisible(false);
        }
    }
    //Populates the table for the Home page window, setup each column to accept the appropriate field from a property.
    //Loop through the list of properties returning from PropertyRepository.
    //Check that the branch name of the house/flat matches the branch of the user logged in, and the flat/house is not sold.
    //If true then add the property as a row in the table.
    public void populateTable(Session session) {
        ObservableList<Property> rows = FXCollections.observableArrayList();
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
        if(!session.isAdmin()) {
            for (Property allProperty : propertyRepository.findAll()) {
                if (allProperty instanceof House) {
                    House house = (House) allProperty;
                    if (house.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("n")) {
                            rows.add(house);
                        }
                    }
                }
                if (allProperty instanceof Flat) {
                    Flat flat = (Flat) allProperty;
                    if (allProperty.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (flat.getSold().toLowerCase().equals("n")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        } else {
            for (Property allProperty : session.getBranch().getPropertyList()) {
                if (allProperty instanceof House) {
                    House house = (House) allProperty;
                    if (house.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("n")) {
                            rows.add(house);
                        }
                    }
                }
                if (allProperty instanceof Flat) {
                    Flat flat = (Flat) allProperty;
                    if (allProperty.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (flat.getSold().toLowerCase().equals("n")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        }

    homeTV.setItems(rows);
    homeTV.setEditable(true);
    addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
    roomCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    sellPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    soldCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
    floorsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    floorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    garageCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
    gardenCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
    monthCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    soldPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    setColumnsEditable();
    }
    //Gets called when populating the table, allows the columns in the table to be editable during the session.
    public void setColumnsEditable() {

        addressCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, String>>) propertyStringCellEditEvent -> ((Property) propertyStringCellEditEvent.getTableView().getItems().get(propertyStringCellEditEvent.getTablePosition().getRow())).setAddress(propertyStringCellEditEvent.getNewValue()));
        floorCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Integer>>) propertyIntCellEditEvent -> ((Property) propertyIntCellEditEvent.getTableView().getItems().get(propertyIntCellEditEvent.getTablePosition().getRow())).setFloorNumber(propertyIntCellEditEvent.getNewValue()));
        roomCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Integer>>) propertyIntCellEditEvent -> ((Property) propertyIntCellEditEvent.getTableView().getItems().get(propertyIntCellEditEvent.getTablePosition().getRow())).setRoomAmount(propertyIntCellEditEvent.getNewValue()));
        floorsCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Integer>>) propertyIntCellEditEvent -> ((Property) propertyIntCellEditEvent.getTableView().getItems().get(propertyIntCellEditEvent.getTablePosition().getRow())).setFloorAmount(propertyIntCellEditEvent.getNewValue()));
        sellPrCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Double>>) propertyDoubleCellEditEvent -> ((Property) propertyDoubleCellEditEvent.getTableView().getItems().get(propertyDoubleCellEditEvent.getTablePosition().getRow())).setSellPrice(propertyDoubleCellEditEvent.getNewValue()));
        soldPrCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Double>>) propertyDoubleCellEditEvent -> ((Property) propertyDoubleCellEditEvent.getTableView().getItems().get(propertyDoubleCellEditEvent.getTablePosition().getRow())).setSoldPrice(propertyDoubleCellEditEvent.getNewValue()));
        monthCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, Double>>) propertyDoubleCellEditEvent -> ((Property) propertyDoubleCellEditEvent.getTableView().getItems().get(propertyDoubleCellEditEvent.getTablePosition().getRow())).setMonthlyRate(propertyDoubleCellEditEvent.getNewValue()));
        gardenCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, String>>) propertyStringCellEditEvent -> ((Property) propertyStringCellEditEvent.getTableView().getItems().get(propertyStringCellEditEvent.getTablePosition().getRow())).setGarden(propertyStringCellEditEvent.getNewValue()));
        garageCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, String>>) propertyStringCellEditEvent -> ((Property) propertyStringCellEditEvent.getTableView().getItems().get(propertyStringCellEditEvent.getTablePosition().getRow())).setGarage(propertyStringCellEditEvent.getNewValue()));
    }
    //Opens the window to add a house.
    public void openAddHouse() throws IOException {
        FXMLLoader addHouseLoader = new FXMLLoader();
        addHouseLoader.setLocation(getClass().getClassLoader().getResource("view/addHouseView.fxml"));
        Scene scene = new Scene(addHouseLoader.load(), 600,540);
        Stage stage = new Stage();
        stage.setTitle("Add a House!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = addHouseLoader.getController();
        AddHouseController controller = (AddHouseController) temp;
        controller.setSessionTF(getSessionName());
    }
    //Opens the window to add a flat.
    public void openAddFlat() throws IOException {
        FXMLLoader addFlatLoader = new FXMLLoader();
        addFlatLoader.setLocation(getClass().getClassLoader().getResource("view/addFlatView.fxml"));
        Scene scene = new Scene(addFlatLoader.load(), 600,540);
        Stage stage = new Stage();
        stage.setTitle("Add a Flat!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = addFlatLoader.getController();
        AddFlatController controller = (AddFlatController) temp;
        controller.setSessionTF(getSessionName());
    }
    //Sets an invisible TextField to the users username, this can be used as a reference when checking which properties to show based on the name of the branch.
    public void setGhostSession(Session session) {
        if(session.isAdmin()) {
            ghostSessionTF.setText(session.getBranch().getUsername());
            ghostSessionTF.setVisible(false);
        }
        else {
            ghostSessionTF.setText(session.getAdmin().getUsername());
            ghostSessionTF.setVisible(false);
        }
    }
    //Returns the sessions username.
    public String getSessionName() {
        String name = ghostSessionTF.getText();
        return name;
    }
    //Clears all items in the main table, re-populates.
    public void refresh() {
        homeTV.getItems().clear();
        ObservableList<Property> rows = FXCollections.observableArrayList();
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
        if(!isAdmin()) {
            for (Property allProperty : propertyRepository.findByBranch(ghostSessionTF.getText())) {
                if (allProperty instanceof House) {
                    House house = (House) allProperty;
                    if (house.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("n")) {
                            rows.add(house);
                        }
                    }
                }
                if (allProperty instanceof Flat) {
                    Flat flat = (Flat) allProperty;
                    if (allProperty.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (flat.getSold().toLowerCase().equals("n")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        } else {
            for (Property allProperty : propertyRepository.findAll()) {
                if (allProperty instanceof House) {
                    House house = (House) allProperty;
                    if (house.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("n")) {
                            rows.add(house);
                        }
                    }
                }
                if (allProperty instanceof Flat) {
                    Flat flat = (Flat) allProperty;
                    if (allProperty.getBranchName().toLowerCase().equals(ghostSessionTF.getText().toLowerCase()) || isAdmin()) {
                        if (flat.getSold().toLowerCase().equals("n")) {
                            rows.add(flat);
                        }
                    }
                }
            }
        }
        homeTV.setItems(rows);
        homeTV.setEditable(true);
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roomCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sellPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        soldCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        floorsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        floorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        garageCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        gardenCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        monthCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        soldPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        setColumnsEditable();
    }
    //Call this when save button is pressed.
    //Fetch the previous state of the properties file, add it to the global ArrayList.
    //Loop through each row in the table, store each row in a new instance of either house or flat, based on the value in the "Type" column.
    //Add these instances to the global ArrayList so we can update the table with the new list.
    //Before writing, check if the sold price value has been changed, if the value is above 0, set the sold value to "Y".
    //Overwrite the files for any changes.
    public void updateTable() {
        House house;
        Flat flat;
        String garage = garageCol.getCellValueFactory().toString();
        for (int i = 0; i < homeTV.getItems().size(); i++) {
            House replaceHouse = new House(Integer.parseInt((String) idCol.getCellData(i).toString()), ghostSessionTF.getText(), addressCol.getCellData(i).toString(), soldCol.getCellData(i).toString(), typeCol.getCellData(i).toString(),
                    Integer.parseInt((String) roomCol.getCellData(i).toString()), Double.parseDouble((String) sellPrCol.getCellData(i).toString()), Double.parseDouble((String) soldPrCol.getCellData(i).toString()), Integer.parseInt((String) floorCol.getCellData(i).toString()), Double.parseDouble((String) monthCol.getCellData(i).toString()),
                    gardenCol.getCellData(i).toString(), garageCol.getCellData(i).toString(), Integer.parseInt((String) floorsCol.getCellData(i).toString()));

            Flat replaceFlat = new Flat(Integer.parseInt((String) idCol.getCellData(i).toString()), ghostSessionTF.getText(), addressCol.getCellData(i).toString(), soldCol.getCellData(i).toString(), typeCol.getCellData(i).toString(),
                    Integer.parseInt((String) roomCol.getCellData(i).toString()), Double.parseDouble((String) sellPrCol.getCellData(i).toString()), Double.parseDouble((String) soldPrCol.getCellData(i).toString()), Integer.parseInt((String) floorCol.getCellData(i).toString()), Double.parseDouble((String) monthCol.getCellData(i).toString()),
                    gardenCol.getCellData(i).toString(), garageCol.getCellData(i).toString(), Integer.parseInt((String) floorsCol.getCellData(i).toString()));
            properties.add(replaceHouse);
            properties.add(replaceFlat);
            if(typeCol.getCellData(i).toString().equals("House")) {
                for (int p = 0; p < properties.size(); p++) {
                    if (properties.get(p).getId() == Integer.parseInt((String) idCol.getCellData(i).toString())) {
                        if(!soldPrCol.getCellData(i).toString().equals("0.0")) {
                            replaceHouse.setSold("Y");
                        }
                        properties.set(p, replaceHouse);
                        propertyRepository.remove(properties.get(p).getId());
                        propertyRepository.put(replaceHouse);
                    }
                }
            }
            if(typeCol.getCellData(i).toString().equals("Flat")) {
                for (int p = 0; p < properties.size(); p++) {
                    if (properties.get(p).getId() == Integer.parseInt((String) idCol.getCellData(i).toString())) {
                        if(!soldPrCol.getCellData(i).toString().equals("0.0")) {
                            replaceFlat.setSold("Y");
                        }
                        properties.set(p, replaceFlat);
                        propertyRepository.remove(properties.get(p).getId());
                        propertyRepository.put(replaceFlat);
                    }
                }
            }
        }
    }

    //Removes the selected row from the table and file.
    //Prompts the user with a warning that this action cannot be reverted, would they like to proceed?
    //If yes, then find the file from the PropertyRepository by referencing its ID.
    //Delete the file.
    public void removeRow() {
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion.");
        alert.setHeaderText("Warning! You are about to delete a property.");
        alert.setContentText("Are you sure? This change cannot be reverted.");
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesBtn, noBtn, cancelBtn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesBtn) {
            ObservableList<Property> allRows, singleRow;
            allRows = homeTV.getItems();
            singleRow = homeTV.getSelectionModel().getSelectedItems();
            Property selectedItems = (Property) homeTV.getSelectionModel().getSelectedItems().get(0);
            String firstCol = selectedItems.toString().split(",")[0];
            for (int p = 0; p < properties.size(); p++) {
                if (properties.get(p).getId() == selectedItems.getId()) {
                    propertyRepository.remove(properties.get(p).getId());
                    properties.remove(p);
                }
            }
            singleRow.forEach(allRows::remove);
        } else if (result.get() == null) {

        } else {

        }

    }
    //Similar to populateTable, except this re-populates the table with any property names that match the value in the search field.
    //During this, it will perform checks such as only displaying properties that aren't sold and are from the branch as the secretary logged in.
    public void sendQuery() {
        ObservableList<Property> rows = FXCollections.observableArrayList();
        List<Property> allProperties = propertyRepository.findAll();
        List<Property> allBranchProperties = propertyRepository.findByBranch(ghostSessionTF.getText());
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
                        if (house.getSold().toLowerCase().equals("n")) {
                            if (house.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                rows.add(house);
                            }
                        }
                    }
                }
                if (property instanceof Flat) {
                    Flat flat = (Flat) property;
                    if (property.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (property.getSold().toLowerCase().equals("n")) {
                            if (property.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                rows.add(flat);
                            }
                        }
                    }
                }
            }
        } else {
            for (Property property : allBranchProperties) {
                if (property instanceof House) {
                    House house = (House) property;
                    if (house.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (house.getSold().toLowerCase().equals("n")) {
                            if (house.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                rows.add(house);
                            }
                        }
                    }
                }
                if (property instanceof Flat) {
                    Flat flat = (Flat) property;
                    if (property.getBranchName().equals(ghostSessionTF.getText()) || isAdmin()) {
                        if (property.getSold().toLowerCase().equals("n")) {
                            if (property.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                rows.add(flat);
                            }
                        }
                    }
                }
            }
        }
        homeTV.setItems(rows);
        homeTV.setEditable(true);
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roomCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sellPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        soldCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        floorsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        floorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        garageCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        gardenCol.setCellFactory(ComboBoxTableCell.forTableColumn("Y", "N"));
        monthCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        soldPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        setColumnsEditable();
    }
    //Opens the window that shows houses.
    public void openViewHouses() throws IOException {
        FXMLLoader allHouseLoader = new FXMLLoader();
        allHouseLoader.setLocation(getClass().getClassLoader().getResource("view/AllHousesView.fxml"));
        Scene scene = new Scene(allHouseLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Houses!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = allHouseLoader.getController();
        AllHousesController controller = (AllHousesController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable(ghostSessionTF.getText());
    }
    //Opens the window that shows flats.
    public void openViewFlats() throws IOException {
        FXMLLoader allFlatsLoader = new FXMLLoader();
        allFlatsLoader.setLocation(getClass().getClassLoader().getResource("view/AllFlatsView.fxml"));
        Scene scene = new Scene(allFlatsLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Flats!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = allFlatsLoader.getController();
        AllFlatsController controller = (AllFlatsController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable(ghostSessionTF.getText());
    }
    //Opens the window that shows all sold properties.
    public void openViewSold() throws IOException {
        FXMLLoader allSoldLoader = new FXMLLoader();
        allSoldLoader.setLocation(getClass().getClassLoader().getResource("view/AllSoldView.fxml"));
        Scene scene = new Scene(allSoldLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Sold!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Object temp = allSoldLoader.getController();
        AllSoldController controller = (AllSoldController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable(ghostSessionTF.getText());
    }
    //Checks if the current user is an admin.
    private boolean isAdmin() {
        return ghostSessionTF.getText().toLowerCase().equals("admin");
    }
    //Called then logout button is pressed.
    //Closes the current window and displays the login window.
    public void handleLogout() throws IOException {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/loginView.fxml"));
        stage.setTitle("National Estate Agents Application");
        stage.setScene(new Scene(root, 1200, 800));
        stage.setResizable(false);
        stage.show();
    }
}
