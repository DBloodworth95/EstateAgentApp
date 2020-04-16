package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;
import view.HelpAlert;
import view.ForgotPasswordAlert;
import view.InvalidPasswordAlert;
import java.io.*;
import java.nio.file.Paths;

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
    private DatPropertyRepository propertyRepository = new DatPropertyRepository(Paths.get("property"));
    private DatBranchRepository branchRepository = new DatBranchRepository(Paths.get("branch"));
    //Displays an alert to help the user with any login issues.
    public void showHelp() {
        helpAlert = new HelpAlert();
    }
    //Displays an alert to help the user if they've forgotten their password.
    public void showForgottenPW() {
        pwHelpAlert = new ForgotPasswordAlert();
    }
    //Processes an admin login, reads the user.dat file for any credentials matching that in the login forms.
    //If true, display the home page, populate the home page table and set the "Logged in as" text to show the users name.
    //If false, display invalid username/password.
    public void checkAdminLogin() {
        boolean accountFound = false;
        try {
            FileInputStream fis = new FileInputStream("users.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Admin obj = null;
            while ((obj = (Admin)ois.readObject())!=null) {
                if(usernameTF.getText().toLowerCase().equals(obj.getUsername()) && passwordTF.getText().toLowerCase().equals(obj.getPassword())) {
                    accountFound = true;
                    session = new Session(obj, null);
                    session.getAdmin().setPropertyList(branchRepository.findAll());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageView.fxml"));
                    Parent homePage = loader.load();
                    adminLoginBtn.getScene().setRoot(homePage);
                    Object temp = loader.getController();
                    HomePageController controller = (HomePageController) temp;
                    controller.setLoggedInTF(session);
                    controller.setGhostSession(session);
                    controller.populateTable(session);
                    break;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (accountFound) {
            System.out.println("Login successful");
        } else {
            invalidPwAlert = new InvalidPasswordAlert();
            usernameTF.clear();
            passwordTF.clear();
        }
    }

    //Processes an branch secretary login, reads the branch.dat file for any credentials matching that in the login forms.
    //If true, display the home page, populate the home page table and set the "Logged in as" text to show the users name.
    //If false, display invalid username/password.
    public void checkBranchLogin() throws Exception {
        boolean branchFound = false;
        Branch branch = branchRepository.find(branchTF.getText(), branchPwTF.getText());
        if (branch != null) {
            branchFound = true;
            session = new Session(null, branch);
            session.getBranch().setPropertyList(propertyRepository.findByBranch(session.getBranch().getName()));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageView.fxml"));
            Parent homePage = loader.load();
            adminLoginBtn.getScene().setRoot(homePage);
            Object temp = loader.getController();
            HomePageController controller = (HomePageController) temp;
            controller.setLoggedInTF(session);
            controller.setTitleTF(session);
            controller.hideAdminPanel(session);
            controller.setGhostSession(session);
            controller.populateTable(session);
        } else {
            branchFound = false;
            branchTF.clear();
            branchPwTF.clear();
        }
        if (branchFound) {
            System.out.println("Login successful");
        } else {
            invalidPwAlert = new InvalidPasswordAlert();
        }
    }
}
