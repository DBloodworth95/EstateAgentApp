package view;

import javafx.scene.control.Alert;

public class ForgotPasswordAlert {
    private Alert pwHelpAlert;

    public ForgotPasswordAlert() {
        pwHelpAlert = new Alert(Alert.AlertType.INFORMATION);
        pwHelpAlert.setTitle("Forgotten Username/Password.");
        pwHelpAlert.setHeaderText(null);
        pwHelpAlert.setContentText("Please contact your Administrator if you are having issues logging in.");
        pwHelpAlert.show();
    }
}
