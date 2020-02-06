package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
    private Button addBrBtn, editBrBtn, refreshBtn;
    @FXML
    private TableView homeTV;
    @FXML
    private TableColumn addressCol, floorCol, roomCol, floorsCol, sellPrCol, soldPrCol, monthCol, gardenCol, garageCol, typeCol, soldCol, idCol;
    @FXML
    private VBox adminPanelHBox;
    private ArrayList<Property> properties = new ArrayList<>();

    public void setLoggedInTF(Session session) {
        loggedInTF.setText("Logged in as " + session.getUsername() + " branch secretary");
    }

    public void setTitleTF(Session session) {
        if (!session.getAccessLevel().equals("All")) {
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
                            if (house.getBranchName().equals(ghostSessionTF.getText())) {
                                System.out.println(house.getBranchName());
                                rows.add(house);
                            }
                        }
                        if (allProperty instanceof Flat) {
                            Flat flat = (Flat) allProperty;
                            if (allProperty.getBranchName().equals(ghostSessionTF.getText())) {
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
        soldCol.setCellFactory(TextFieldTableCell.forTableColumn());
        floorsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        floorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        garageCol.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));
        gardenCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        soldPrCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        setColumnsEditable();
    }

    public void setColumnsEditable() {
        addressCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Property, String>>) propertyStringCellEditEvent -> ((Property) propertyStringCellEditEvent.getTableView().getItems().get(propertyStringCellEditEvent.getTablePosition().getRow())).setAddress(propertyStringCellEditEvent.getNewValue()));
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
                        properties.set(p, replaceHouse);
                    }
                }
            }
            if(typeCol.getCellData(i).toString().equals("Flat")) {
                for (int p = 0; p < properties.size(); p++) {
                    if (properties.get(p).getId() == Integer.parseInt((String) idCol.getCellData(i).toString())) {
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


}
