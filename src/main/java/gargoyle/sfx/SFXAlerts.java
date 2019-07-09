package gargoyle.sfx;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

class SFXAlerts {
    void alert(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText("");
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
        alert.setTitle(stage.getTitle());
        alert.initStyle(stage.getStyle());
        alert.showAndWait();
    }

    boolean confirm(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.setHeaderText("");
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
        alert.setTitle(stage.getTitle());
        alert.initStyle(stage.getStyle());
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent() && buttonType.get() == ButtonType.OK;
    }

    String prompt(Stage stage, String message, String defaultAnswer) {
        TextInputDialog alert = new TextInputDialog(defaultAnswer);
        alert.setHeaderText("");
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
        alert.setTitle(stage.getTitle());
        alert.initStyle(stage.getStyle());
        alert.setContentText(message);
        return alert.showAndWait().orElse(defaultAnswer);
    }

    void error(Stage stage, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
        alert.setTitle(stage.getTitle());
        alert.initStyle(stage.getStyle());
        alert.setHeaderText(e.getClass().getName());
        alert.setContentText(e.getLocalizedMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("...");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
