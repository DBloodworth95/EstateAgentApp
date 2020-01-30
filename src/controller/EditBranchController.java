package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Branch;
import java.io.*;
import java.util.ArrayList;

public class EditBranchController {
    @FXML
    private TableColumn nameCol, addressCol, phoneCol, emailCol, webCol;
    @FXML
    private TableView editTV;
    ArrayList<Branch> updatedList = new ArrayList<>();

    public void populateEditTable() throws Exception {
        ObservableList<Branch> rows = FXCollections.observableArrayList();
        try {
            InputStream fis = new FileInputStream("branch.dat");
            ObjectInput ois = new ObjectInputStream(fis);
            ArrayList<Branch> obj = null;
            nameCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("address"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("phoneNumber"));
            emailCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("email"));
            webCol.setCellValueFactory(new PropertyValueFactory<Branch, String>("webAddress"));
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
            try {
                while ((obj = (ArrayList<Branch>) ois.readObject()) != null) {
                    for (int i = 0; i < obj.size(); i++) {
                        Branch branch = obj.get(i);
                        rows.add(branch);
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
        editTV.setItems(rows);
        editTV.setEditable(true);
        editTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        webCol.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void updateBranches() {
        Branch branch;
        for (int i = 0; i < editTV.getItems().size(); i++) {
            branch = new Branch(nameCol.getCellData(i).toString(), addressCol.getCellData(i).toString(), phoneCol.getCellData(i).toString(), emailCol.getCellData(i).toString(), webCol.getCellData(i).toString(), nameCol.getCellData(i).toString(), nameCol.getCellData(i).toString(), nameCol.getCellData(i).toString());
            updatedList.add(branch);
            try {
                OutputStream fstream = new FileOutputStream("branch.dat");
                ObjectOutput oos = new ObjectOutputStream(fstream);
                try {
                    oos.writeObject(updatedList);
                } finally {
                    oos.close();
                }
            }
            catch(IOException exception) {
                exception.printStackTrace();
            }
        }

    }

    public void removeRow() {
        ObservableList<Branch> allRows, singleRow;
        allRows = editTV.getItems();
        singleRow = editTV.getSelectionModel().getSelectedItems();
        singleRow.forEach(allRows::remove);
    }
}


