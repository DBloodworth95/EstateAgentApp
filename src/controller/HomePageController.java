package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;

public class HomePageController {
    @FXML
    private Text loggedInTF, titleTF, adminTF, ghostSessionTF;
    @FXML
    private Button addBrBtn, editBrBtn;
    @FXML
    private TableView homeTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol, idCol;
    @FXML
    private VBox adminPanelHBox;
    @FXML
    private TextField searchTF;
    private ArrayList<Property> properties = new ArrayList<>();

    public void setLoggedInTF(Session session) {
        loggedInTF.setText("Logged in as " + session.getUsername() + " branch secretary");
    }

    public void setTitleTF(Session session) {
        if (!session.getAccessLevel().equals("All")) {
            titleTF.setText("All properties for sale at Branch: " + session.getUsername());
        } else {
            titleTF.setText("All properties for sale at all Branches.");
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
        if (!session.getAccessLevel().equals("All")) {
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
            idCol.setCellValueFactory(new PropertyValueFactory<Property, Integer>("id"));

            try {
                while ((allProperties = (ArrayList<Property>) ois.readObject()) != null) {
                    for (Property allProperty : allProperties) {
                        if (allProperty instanceof House) {
                            House house = (House) allProperty;
                            if (house.getBranchName().equals(ghostSessionTF.getText()) && house.getSold().equals("N") || house.getSold().equals("n")) {
                                System.out.println(house.getBranchName());
                                rows.add(house);
                            }
                        }
                        if (allProperty instanceof Flat) {
                            Flat flat = (Flat) allProperty;
                            if (allProperty.getBranchName().equals(ghostSessionTF.getText()) && allProperty.getSold().equals("N") || allProperty.getSold().equals("n")) {
                                System.out.println(allProperty.getBranchName());
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

    public void openAddFlat() throws IOException {
        FXMLLoader addFlatLoader = new FXMLLoader();
        addFlatLoader.setLocation(getClass().getResource("/view/addFlatView.fxml"));
        Scene scene = new Scene(addFlatLoader.load(), 600,540);
        Stage stage = new Stage();
        stage.setTitle("Add a Flat!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = addFlatLoader.getController();
        AddFlatController controller = (AddFlatController) temp;
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

    public void refresh() {
        homeTV.getItems().clear();
        populateTable();
    }

    public void updateTable() {
        fetchFile();
        House house;
        Flat flat;
        for (int i = 0; i < homeTV.getItems().size(); i++) {
            House replaceHouse = new House(Integer.parseInt((String) idCol.getCellData(i).toString()), ghostSessionTF.getText(), addressCol.getCellData(i).toString(), soldCol.getCellData(i).toString(), typeCol.getCellData(i).toString(),
                    Integer.parseInt((String) roomCol.getCellData(i).toString()), Double.parseDouble((String) sellPrCol.getCellData(i).toString()), Double.parseDouble((String) soldPrCol.getCellData(i).toString()), Integer.parseInt((String) floorCol.getCellData(i).toString()), Double.parseDouble((String) monthCol.getCellData(i).toString()),
                    gardenCol.getCellData(i).toString(), garageCol.getCellData(i).toString(), Integer.parseInt((String) floorsCol.getCellData(i).toString()));

            Flat replaceFlat = new Flat(Integer.parseInt((String) idCol.getCellData(i).toString()), ghostSessionTF.getText(), addressCol.getCellData(i).toString(), soldCol.getCellData(i).toString(), typeCol.getCellData(i).toString(),
                    Integer.parseInt((String) roomCol.getCellData(i).toString()), Double.parseDouble((String) sellPrCol.getCellData(i).toString()), Double.parseDouble((String) soldPrCol.getCellData(i).toString()), Integer.parseInt((String) floorCol.getCellData(i).toString()), Double.parseDouble((String) monthCol.getCellData(i).toString()),
                    gardenCol.getCellData(i).toString(), garageCol.getCellData(i).toString(), Integer.parseInt((String) floorsCol.getCellData(i).toString()));

            if(typeCol.getCellData(i).toString().equals("House")) {
                for (int p = 0; p < properties.size(); p++) {
                    if (properties.get(p).getId() == Integer.parseInt((String) idCol.getCellData(i).toString())) {
                        if(!soldPrCol.getCellData(i).toString().equals("0.0")) {
                            replaceHouse.setSold("Y");
                        }
                        properties.set(p, replaceHouse);
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
                    }
                }
            }
        }
        try {
            OutputStream fstream = new FileOutputStream("properties.dat");
            ObjectOutput oos = new ObjectOutputStream(fstream);
            try {
                oos.writeObject(properties);
            } finally {
                oos.close();
            }
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public void fetchFile() {
        properties.removeAll(properties);
        try {
            InputStream fis = new FileInputStream("properties.dat");
            ObjectInput ois = new ObjectInputStream(fis);
            ArrayList<Property> obj = null;
            try {
                while ((obj = (ArrayList<Property>) ois.readObject()) != null) {
                    for (Property property : obj) {
                        properties.add(property);
                    }
                }
            }
            finally {
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
    }
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
            fetchFile();
            for (int p = 0; p < properties.size(); p++) {
                if (properties.get(p).getId() == selectedItems.getId()) {
                    properties.remove(p);
                }
            }
            try {
                OutputStream fstream = new FileOutputStream("properties.dat");
                ObjectOutput oos = new ObjectOutputStream(fstream);
                try {
                    oos.writeObject(properties);
                } finally {
                    oos.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            singleRow.forEach(allRows::remove);
        } else if (result.get() == null) {

        } else {

        }

    }
    public void sendQuery() {
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
            idCol.setCellValueFactory(new PropertyValueFactory<Property, Integer>("id"));

            try {
                while ((allProperties = (ArrayList<Property>) ois.readObject()) != null) {
                    for (Property allProperty : allProperties) {
                        if (allProperty instanceof House) {
                            House house = (House) allProperty;
                            if (house.getBranchName().equals(ghostSessionTF.getText()) && house.getSold().equals("N") || house.getSold().equals("n")) {
                                if (house.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                    System.out.println(house.getBranchName());
                                    rows.add(house);
                                }
                            }
                        }
                        if (allProperty instanceof Flat) {
                            Flat flat = (Flat) allProperty;
                            if (allProperty.getBranchName().equals(ghostSessionTF.getText()) && allProperty.getSold().equals("N") || allProperty.getSold().equals("n")) {
                                if (allProperty.getAddress().toLowerCase().contains(searchTF.getText().toLowerCase())) {
                                    System.out.println(allProperty.getBranchName());
                                    rows.add(flat);
                                }
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

    public void openViewHouses() throws IOException {
        FXMLLoader allHouseLoader = new FXMLLoader();
        allHouseLoader.setLocation(getClass().getResource("/view/AllHousesView.fxml"));
        Scene scene = new Scene(allHouseLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Houses!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = allHouseLoader.getController();
        AllHousesController controller = (AllHousesController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable();
    }

    public void openViewFlats() throws IOException {
        FXMLLoader allFlatsLoader = new FXMLLoader();
        allFlatsLoader.setLocation(getClass().getResource("/view/AllFlatsView.fxml"));
        Scene scene = new Scene(allFlatsLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Flats!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = allFlatsLoader.getController();
        AllFlatsController controller = (AllFlatsController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable();
    }

    public void openViewSold() throws IOException {
        FXMLLoader allSoldLoader = new FXMLLoader();
        allSoldLoader.setLocation(getClass().getResource("/view/AllSoldView.fxml"));
        Scene scene = new Scene(allSoldLoader.load(), 895,667);
        Stage stage = new Stage();
        stage.setTitle("View all Sold!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Object temp = allSoldLoader.getController();
        AllSoldController controller = (AllSoldController) temp;
        controller.setSessionTF(getSessionName());
        controller.populateTable();
    }
}
