package view;

import javafx.scene.control.Alert;

public class InvalidPasswordAlert {

    private Alert invalidPwAlert;

    public InvalidPasswordAlert() {
        invalidPwAlert = new Alert(Alert.AlertType.INFORMATION);
        invalidPwAlert.setTitle("Invalid Username/Password!");
        invalidPwAlert.setHeaderText(null);
        invalidPwAlert.setContentText("Invalid Username or Password, please try again!");
        invalidPwAlert.show();
    }
}
