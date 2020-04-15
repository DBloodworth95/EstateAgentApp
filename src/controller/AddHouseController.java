package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.properties.House;
import model.properties.Property;
import java.io.*;
import java.util.ArrayList;

public class AddHouseController {
    @FXML
    private TextField addressTF, roomAmountTF, floorAmountTF, garageTF, gardenTF, sellPrTF, soldPrTF, soldTF;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;

    private Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);

    private ArrayList<Property> properties = new ArrayList<>();
    //Adds a house to the properties.dat file.
    //Check that all forms are appropriate, if true then create an instance of House.
    //Load the previous state of the file, overwrite the file with the previous state and the additional instance of House that's just been created.
    //Clear the forms, ready for another addition.
    public void addHouse() throws IOException {
        if(validInput()) {
            House house = new House(0, ghostSessionTF.getText(), addressTF.getText(), soldTF.getText(), "House", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
                    Double.parseDouble(soldPrTF.getText()), 0, 0, gardenTF.getText(), garageTF.getText(), Integer.parseInt(floorAmountTF.getText()));
            loadPrevious();
            house.setId(properties.size());
            properties.add(house);
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
            clearAll();
        }
    }
    //Read the properties.dat file and fetch all of the properties in the file. Add them to the global ArrayList during runtime.
    //This can be used whenever a new property needs to be written to file, the current state of the file must be re-written with the additional
    //property being added.
    public void loadPrevious() {
        try {
            InputStream fis = new FileInputStream("properties.dat");
            ObjectInput ois = new ObjectInputStream(fis);
            ArrayList<Property> obj = null;
            try {
                while ((obj = (ArrayList<Property>) ois.readObject()) != null) {
                    for(int i = 0; i<obj.size(); i++) {
                        properties.add(obj.get(i));
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
}
