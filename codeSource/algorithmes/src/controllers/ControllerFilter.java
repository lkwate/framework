package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFilter implements Initializable {

    @FXML // fx:id="bAjouterClause"
    private Button bAjouterClause; // Value injected by FXMLLoader

    @FXML // fx:id="bValider"
    private Button bValider; // Value injected by FXMLLoader

    @FXML // fx:id="Annuler"
    private Button Annuler; // Value injected by FXMLLoader

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
