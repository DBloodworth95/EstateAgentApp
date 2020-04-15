package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DatPropertyRepository;
import model.properties.Flat;
import model.properties.Property;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AddFlatController {
    @FXML
    private TextField addressTF, roomAmountTF, floorNumberTF, monthlyRTF, sellPrTF, soldPrTF, soldTF;
    @FXML
    private Text ghostSessionTF;
    @FXML
    private Button closeBtn;

    private Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
    private DatPropertyRepository datPropertyRepository = new DatPropertyRepository(Paths.get("property"));
    private ArrayList<Property> properties = new ArrayList<>();
    //Adds a flat to the properties.dat file.
    //Check that all forms are appropriate, if true then create an instance of Flat.
    //Load the previous state of the file, overwrite the file with the previous state and the additional instance of Flat that's just been created.
    //Clear the forms, ready for another addition.
    public void addFlat() throws IOException {
        if(validInput()) {
            Flat flat = new Flat(0, ghostSessionTF.getText(), addressTF.getText(), soldTF.getText(), "Flat", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
                    Double.parseDouble(soldPrTF.getText()), Integer.parseInt(floorNumberTF.getText()), Double.parseDouble(monthlyRTF.getText()), "N/A", "N/A", 0);
            loadPrevious();
            flat.setId(properties.size());
            clearAll();
            datPropertyRepository.put(flat);
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
}
