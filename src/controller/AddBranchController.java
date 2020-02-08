package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Branch;
import java.io.*;
import java.util.ArrayList;

public class AddBranchController {
    @FXML
    private Button closeWindowBtn;
    @FXML
    private TextField nameTF, addressTF, phoneTF, emailTF, webTF, usernameTF;
    @FXML
    private PasswordField passwordTF;
    private ArrayList<Branch> branches = new ArrayList<>();


    public void addBranch() throws IOException {
        Branch branch = new Branch(nameTF.getText(), addressTF.getText(), phoneTF.getText(), emailTF.getText(), webTF.getText(), usernameTF.getText(), passwordTF.getText(), nameTF.getText());
        branches.add(branch);
        loadPrevious();
        try {
            OutputStream fstream = new FileOutputStream("branch.dat");
            ObjectOutput oos = new ObjectOutputStream(fstream);
            try {
                oos.writeObject(branches);
            } finally {
                oos.close();
            }
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
        clearAll();
    }

    public void clearAll() {
        nameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        emailTF.clear();
        webTF.clear();
        usernameTF.clear();
        passwordTF.clear();
    }

    public void loadPrevious() {
        try {
            InputStream fis = new FileInputStream("branch.dat");
            ObjectInput ois = new ObjectInputStream(fis);
            ArrayList<Branch> obj = null;
            try {
                while ((obj = (ArrayList<Branch>) ois.readObject()) != null) {
                    for(int i = 0; i<obj.size(); i++) {
                        branches.add(obj.get(i));
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

    public void closeWindow() {
        Stage stage = (Stage) closeWindowBtn.getScene().getWindow();
        stage.close();
    }

}
