package controller;

import javafx.fxml.FXML;
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

    private ArrayList<Property> properties = new ArrayList<>();

    public void addHouse() throws IOException {
        House house = new House(0,ghostSessionTF.getText(), addressTF.getText(), soldTF.getText(), "House", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
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
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
        clearAll();
    }

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

    public void setSessionTF(String name) {
        ghostSessionTF.setText(name);
        ghostSessionTF.setVisible(false);
    }

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

    public void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
