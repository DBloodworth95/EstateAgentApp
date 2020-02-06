package view;

import controller.HomePageController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmEntryDeleteAlert {
    private Alert alert;

    public ConfirmEntryDeleteAlert() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion.");
        alert.setHeaderText("Warning! You are about to delete a property.");
        alert.setContentText("Are you sure? This change cannot be reverted.");
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesBtn, noBtn, cancelBtn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesBtn) {

        } else if (result.get() == null) {
            // ... user chose "Two"
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
}
