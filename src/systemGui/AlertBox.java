package systemGui;

import com.google.common.base.Throwables;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AlertBox {

    private Alert alert;

    /**
     * goal : alert for current information
     *
     * @param title
     * @param headerMessage
     * @param message
     * @param alertType
     */
    public static void createAlert(String title, String headerMessage, String message, Alert.AlertType alertType) {
        if (title == null || headerMessage == null || message == null || alertType == null)
            throw new IllegalArgumentException();
        Alert alert;
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * goal : create alert for exception
     *
     * @param exception
     */
    public static void createAlert(Exception exception) {
        if (exception == null) throw new IllegalArgumentException();
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        /**
         * component to display exception containt
         */
        TextArea textArea = new TextArea(Throwables.getStackTraceAsString(exception));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(new Label("The exception stacktrace was :"), 0, 0);
        gridPane.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(gridPane);
        /**
         * show alert
         */
        alert.showAndWait();
    }
}
