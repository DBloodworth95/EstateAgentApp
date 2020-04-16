package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Branch;
import model.DatBranchRepository;
import model.DatPropertyRepository;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class AddBranchController {
    @FXML
    private Button closeWindowBtn;
    @FXML
    private TextField nameTF, addressTF, phoneTF, emailTF, webTF, usernameTF;
    @FXML
    private PasswordField passwordTF;
    private ArrayList<Branch> branches = new ArrayList<>();
    private Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
    private DatPropertyRepository propertyRepository = new DatPropertyRepository(Paths.get("property"));
    private DatBranchRepository branchRepository = new DatBranchRepository(Paths.get("branch"));


    public void addBranch() throws Exception {
        if(!validInput()) {
            Branch branch = new Branch(generateID(), nameTF.getText(), addressTF.getText(), phoneTF.getText(), emailTF.getText(), webTF.getText(), usernameTF.getText(), passwordTF.getText(), nameTF.getText(), propertyRepository.findByBranch(nameTF.getText()));
            branches.add(branch);
            branchRepository.put(branch);
            System.out.println(generateID());
            clearAll();
        }
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

    public void closeWindow() {
        Stage stage = (Stage) closeWindowBtn.getScene().getWindow();
        stage.close();
    }

    private boolean validInput() {
        TextField[] inputForms = new TextField[] {
                nameTF, addressTF, phoneTF, emailTF, webTF, usernameTF
        };
        for (TextField inputForm: inputForms) {
            if(inputForm.getText().trim().isEmpty()) {
                errorAlert.setTitle("Branch entry failed.");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Some fields were left empty!");
                errorAlert.showAndWait();
                return true;
            }
        }
        return false;
    }

    private int generateID() {
        Branch maxId = branchRepository.findAll()
                .stream()
                .max(Comparator.comparing(Branch::getId))
                .orElseThrow(NoSuchElementException::new);
        return maxId.getId() + 1;
    }
}
