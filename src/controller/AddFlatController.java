package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.repositories.DatPropertyRepository;
import model.properties.Flat;
import model.properties.Property;
import java.io.*;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class AddFlatController {
    @FXML
    private TextField addressTF, roomAmountTF, floorNumberTF, monthlyRTF, sellPrTF, soldPrTF, soldTF;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;

    private Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
    private DatPropertyRepository datPropertyRepository = new DatPropertyRepository(Paths.get("property"));
    //Create an instance of Flat, construct using fields from the input forms.
    //Put the new Flat into the PropertyRepository and create a file for this.
    public void addFlat() throws IOException {
        if(validInput()) {
            Flat flat = new Flat(generateID(), ghostSessionTF.getText(), addressTF.getText(), soldTF.getText().toUpperCase(), "Flat", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
                    Double.parseDouble(soldPrTF.getText()), Integer.parseInt(floorNumberTF.getText()), Double.parseDouble(monthlyRTF.getText()), "N/A", "N/A", 0);
            clearAll();
            datPropertyRepository.put(flat);
        }

    }
    //Stores the username of the user logged in, this can be used as a reference to perform any checks against the user logged in.
    public void setSessionTF(String name) {
        ghostSessionTF.setText(name);
        ghostSessionTF.setVisible(false);
    }
    //Clears all input forms.
    private void clearAll() {
        addressTF.clear();
        roomAmountTF.clear();
        floorNumberTF.clear();
        monthlyRTF.clear();
        sellPrTF.clear();
        soldPrTF.clear();
        soldTF.clear();
    }
    //Closes the Add Flat window.
    public void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
    //Input validation, checks all TextFields for any empty/inappropriate values, returns true if not empty, returns false if any issues.
    //This gets called when adding a Flat, if any fields are empty/inappropriate it will alert the user.
    private boolean validInput() {
        TextField[] inputForms = new TextField[] {
                addressTF, roomAmountTF, floorNumberTF, monthlyRTF, sellPrTF, soldPrTF, soldTF
        };
        for (TextField inputForm: inputForms) {
            if(inputForm.getText().trim().isEmpty()) {
                errorAlert.setTitle("Flat entry failed.");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Some fields were left empty!");
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }
    //Generate a unique ID for a new Branch based on the highest ID currently existing in the PropertyRepository, increment by 1.
    private int generateID() {
        Property maxId = datPropertyRepository.findAll()
                .stream()
                .max(Comparator.comparing(Property::getId))
                .orElseThrow(NoSuchElementException::new);
        return maxId.getId() + 1;
    }
}
