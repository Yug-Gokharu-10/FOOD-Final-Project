import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;

public class CodePopupTypeShit {

    public static String showPopup(String correctCode) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Exit Code Required");
        dialog.setHeaderText("Enter the 2-letter exit code IN ALPHABETICAL ORDER:");
        dialog.setContentText("Code:");

        Platform.runLater(() -> dialog.getEditor());

        return dialog.showAndWait().orElse(null);
    }
}
