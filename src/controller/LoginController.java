package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Branch;
import model.Session;
import model.User;
import view.HelpAlert;
import view.ForgotPasswordAlert;
import view.InvalidPasswordAlert;
import java.io.*;
import java.util.ArrayList;

public class LoginController {
    @FXML
    private TextField usernameTF, branchTF;
    @FXML
    private PasswordField passwordTF, branchPwTF;
    @FXML
    private Button adminLoginBtn, helpBtn;
    private HelpAlert helpAlert;
    private ForgotPasswordAlert pwHelpAlert;
    private InvalidPasswordAlert invalidPwAlert;
    private Session session;

    public void showHelp() {
        helpAlert = new HelpAlert();
    }

    public void showForgottenPW() {
        pwHelpAlert = new ForgotPasswordAlert();
    }
    public void checkAdminLogin() throws Exception {
        boolean accountFound = false;
        try {
            FileInputStream fis = new FileInputStream("users.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            User obj = null;

            while ((obj = (User)ois.readObject())!=null) {
                if(usernameTF.getText().equals(obj.getUsername()) && passwordTF.getText().equals(obj.getPassword())) {
                    accountFound = true;
                    session = new Session(obj.getUsername(), obj.getPassword(), obj.getAccessLevel());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageView.fxml"));
                    Parent homePage = loader.load();
                    adminLoginBtn.getScene().setRoot(homePage);
                    Object temp = loader.getController();
                    HomePageController controller = (HomePageController) temp;
                    controller.setLoggedInTF(session);
                    controller.setGhostSession(session);
                    controller.populateTable();
                    break;
                } else {
                    accountFound = false;
                }
            }
            ois.close();
            fis.close();
        } catch (EOFException ex) {
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (accountFound == true) {
            System.out.println("Login successful");
        } else {
            invalidPwAlert = new InvalidPasswordAlert();
            usernameTF.clear();
            passwordTF.clear();
        }
    }

    public void checkBranchLogin() throws Exception {
        boolean branchFound = false;
        try {
            FileInputStream fis = new FileInputStream("branch.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Branch> obj = null;

            while ((obj = (ArrayList<Branch>)ois.readObject())!=null) {
                for (int i = 0; i < obj.size(); i++) {
                    Branch branch = obj.get(i);
                    if (branchTF.getText().equals(branch.getUsername()) && branchPwTF.getText().equals(branch.getPassword())) {
                        branchFound = true;
                        session = new Session(branch.getUsername(), branch.getPassword(), branch.getAccessLevel());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageView.fxml"));
                        Parent homePage = loader.load();
                        adminLoginBtn.getScene().setRoot(homePage);
                        Object temp = loader.getController();
                        HomePageController controller = (HomePageController) temp;
                        controller.setLoggedInTF(session);
                        controller.setTitleTF(session);
                        controller.hideAdminPanel(session);
                        controller.setGhostSession(session);
                        controller.populateTable();
                        break;
                    } else {
                        branchFound = false;
                        branchTF.clear();
                        branchPwTF.clear();
                    }
                }
            }
            ois.close();
            fis.close();
        } catch (EOFException ex) {
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (branchFound == true) {
            System.out.println("Login successful");
        } else {
            invalidPwAlert = new InvalidPasswordAlert();
        }
    }
}
