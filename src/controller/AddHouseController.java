package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.repositories.DatPropertyRepository;
import model.properties.House;
import model.properties.Property;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class AddHouseController {
    @FXML
    private TextField addressTF, roomAmountTF, floorAmountTF, garageTF, gardenTF, sellPrTF, soldPrTF, soldTF;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;
    private Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
    private ArrayList<Property> properties = new ArrayList<>();
    private DatPropertyRepository datPropertyRepository = new DatPropertyRepository(Paths.get("property"));
    //Create an instance of House, construct using fields from the input forms.
    //Put the new House into the PropertyRepository and create a file for this.
    public void addHouse() throws IOException {
        if(validInput()) {
            House house = new House(generateID(), ghostSessionTF.getText(), addressTF.getText(), soldTF.getText().toUpperCase(), "House", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
                    Double.parseDouble(soldPrTF.getText()), 0, 0, gardenTF.getText(), garageTF.getText(), Integer.parseInt(floorAmountTF.getText()));
            datPropertyRepository.put(house);
            clearAll();
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
        floorAmountTF.clear();
        garageTF.clear();
        gardenTF.clear();
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
                addressTF, roomAmountTF, floorAmountTF, garageTF, gardenTF, sellPrTF, soldPrTF, soldPrTF
        };
        for (TextField inputForm: inputForms) {
            if(inputForm.getText().trim().isEmpty()) {
                errorAlert.setTitle("House entry failed.");
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
