package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.properties.Flat;
import model.properties.Property;

import java.io.*;
import java.util.ArrayList;

public class AddFlatController {
    @FXML
    private TextField addressTF, roomAmountTF, floorNumberTF, monthlyRTF, sellPrTF, soldPrTF, soldTF;
    @FXML
    private Text ghostSessionTF;
    private ArrayList<Property> properties = new ArrayList<>();

    public void addFlat() throws IOException {
        Flat flat = new Flat(0,ghostSessionTF.getText(), addressTF.getText(), soldTF.getText(), "Flat", Integer.parseInt(roomAmountTF.getText()), Double.parseDouble(sellPrTF.getText()),
                Double.parseDouble(soldPrTF.getText()), Integer.parseInt(floorNumberTF.getText()), Double.parseDouble(monthlyRTF.getText()), "N/A", "N/A", 0);
        loadPrevious();
        flat.setId(properties.size());
        properties.add(flat);
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
}
