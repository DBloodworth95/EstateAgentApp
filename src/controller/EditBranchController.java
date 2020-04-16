package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Branch;
import model.DatBranchRepository;
import model.DatPropertyRepository;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class EditBranchController {
    @FXML
    private TableColumn idCol, nameCol, addressCol, phoneCol, emailCol, webCol;
    @FXML
    private TableView editTV;
    @FXML
    private Button closeBtn;
    ArrayList<Branch> updatedList = new ArrayList<>();
    private DatPropertyRepository propertyRepository = new DatPropertyRepository(Paths.get("property"));
    private DatBranchRepository branchRepository = new DatBranchRepository(Paths.get("branch"));
    //Populate the table which stores info about branches. Reads the ArrayList inside the file for all Branches, adds them as a row in the table.
    //Allow the columns to be editable.
    public void populateEditTable() throws Exception {
        ObservableList<Branch> rows = FXCollections.observableArrayList();
        idCol.setCellValueFactory(new PropertyValueFactory<Branch, Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("phoneNumber"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("email"));
        webCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("webAddress"));
         idCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, Integer>>) branchIntgegerCellEditEvent -> ((Branch) branchIntgegerCellEditEvent.getTableView().getItems().get(branchIntgegerCellEditEvent.getTablePosition().getRow())).setId(branchIntgegerCellEditEvent.getNewValue()));
        nameCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, String>>) branchStringCellEditEvent -> ((Branch) branchStringCellEditEvent.getTableView().getItems().get(branchStringCellEditEvent.getTablePosition().getRow())).setName(branchStringCellEditEvent.getNewValue()));
        addressCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, String>>) branchStringCellEditEvent -> ((Branch) branchStringCellEditEvent.getTableView().getItems().get(branchStringCellEditEvent.getTablePosition().getRow())).setAddress(branchStringCellEditEvent.getNewValue()));
        phoneCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, String>>) branchStringCellEditEvent -> ((Branch) branchStringCellEditEvent.getTableView().getItems().get(branchStringCellEditEvent.getTablePosition().getRow())).setPhoneNumber(branchStringCellEditEvent.getNewValue()));
        emailCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, String>>) branchStringCellEditEvent -> ((Branch) branchStringCellEditEvent.getTableView().getItems().get(branchStringCellEditEvent.getTablePosition().getRow())).setEmail(branchStringCellEditEvent.getNewValue()));
        webCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Branch, String>>) branchStringCellEditEvent -> ((Branch) branchStringCellEditEvent.getTableView().getItems().get(branchStringCellEditEvent.getTablePosition().getRow())).setWebAddress(branchStringCellEditEvent.getNewValue()));

        for (int i = 0; i < branchRepository.findAll().size(); i++) {
            Branch branch = branchRepository.findAll().get(i);
            rows.add(branch);
        }

        editTV.setItems(rows);
        editTV.setEditable(true);
        editTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        webCol.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    //Call this when save button is pressed.
    //Loop through each row in the table, store each row in a new instance of Branch.
    //Write to the branch.dat file and store the Branch objects.
    public void updateBranches() throws IOException, ClassNotFoundException {
        Branch branch;
        branchRepository.removeAll();
        for (int i = 0; i < editTV.getItems().size(); i++) {
           branch = new Branch((Integer) idCol.getCellData(i), nameCol.getCellData(i).toString(), addressCol.getCellData(i).toString(), phoneCol.getCellData(i).toString(), emailCol.getCellData(i).toString(), webCol.getCellData(i).toString(), nameCol.getCellData(i).toString(), nameCol.getCellData(i).toString(), nameCol.getCellData(i).toString(), propertyRepository.findByBranch(nameCol.getCellData(i).toString()));
           branchRepository.put(branch);
        }
    }
    //Deletes a selected row from the table.
    public void removeRow() throws IOException, ClassNotFoundException {
        Branch branch = (Branch) editTV.getSelectionModel().getSelectedItem();
        branchRepository.remove(branch.getId());
        ObservableList<Branch> allRows, singleRow;
        allRows = editTV.getItems();
        singleRow = editTV.getSelectionModel().getSelectedItems();
        singleRow.forEach(allRows::remove);
    }
    //Closes the window.
    public void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}


