package view;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class HelpAlert {

    private Alert helpAlert;

    public HelpAlert() {
        helpAlert = new Alert(Alert.AlertType.INFORMATION);
        helpAlert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        helpAlert.setTitle("Help");
        helpAlert.setHeaderText(null);
        helpAlert.setContentText("Enter your Username and Password in the appropriate fields above. Click the \"Login\" button to proceed.");
        helpAlert.show();
    }
}
