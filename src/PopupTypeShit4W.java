import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class PopupTypeShit4W {

    private static final String CORRECT = "AX";
    private static final String CORRECT2 = "XA";

    public static boolean showPopup() {

        SimpleBooleanProperty result = new SimpleBooleanProperty(false);

        Stage window = new Stage();
        window.setTitle("Passcode Required");

        window.initModality(Modality.APPLICATION_MODAL);

        Label question = new Label("Enter Passcode:");
        PasswordField passInput = new PasswordField();
        Button submit = new Button("Submit");

        submit.setOnAction(e -> {
            String entered = passInput.getText();
            result.set(entered.equals(CORRECT) && entered.equals(CORRECT2));
            window.close();
        });

        VBox layout = new VBox(12, question, passInput, submit);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 260, 150);
        window.setScene(scene);

        //window.showAndWait();
        PauseTransition pause = new PauseTransition(Duration.seconds(20));
        

        return result.get();
    }
}