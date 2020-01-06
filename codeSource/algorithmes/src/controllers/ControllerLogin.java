package controllers;

import com.jfoenix.controls.JFXButton;
import dataDefinition.SpreadSheet;
import exceptions.IncorrectDataException;
import exceptions.MissingRubricException;
import exceptions.NotUniqueTableInQueryException;
import exceptions.WrongContainsOfFile;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import system.SystemAdministrator;
import systemGui.AlertBox;
import systemGui.FieldDescription;
import systemGui.TemplateDataForLogin;
import systemGui.TextFieldEditor;


public class ControllerLogin implements Initializable {

    /**
     * attributes
     */
    private TemplateDataForLogin templateDataForLogin;
    private LinkedHashMap<String, StringProperty> data = new LinkedHashMap<>();

    @FXML // fx:id="bordPane"
    private BorderPane bordPane; // Value injected by FXMLLoader

    @FXML // fx:id="bAnnuler"
    private JFXButton bAnnuler; // Value injected by FXMLLoader

    @FXML // fx:id="bValider"
    private JFXButton bValider; // Value injected by FXMLLoader

    @FXML // fx:id="scrollPane"
    private ScrollPane scrollPane; // Value injected by FXMLLoader

    @FXML // fx:id="gridPane"
    private GridPane gridPane; // Value injected by FXMLLoader

    private Stage stage;

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
        this.gridPane.setAlignment(Pos.TOP_CENTER);
        this.gridPane.setVgap(40);
        this.gridPane.setHgap(40);
        this.bValider.setOnAction(c -> {
            try {
                this.validation();
            } catch (SQLException | IOException e) {
                AlertBox.createAlert(e);
            } catch (IncorrectDataException e) {
                AlertBox.createAlert("Warning", "Paramètres incorrects", e.getMessage(), Alert.AlertType.WARNING);
            } catch (ClassNotFoundException e) {
                AlertBox.createAlert(e);
            }
        });
        this.bAnnuler.setOnAction(c -> {
            this.discard();
        });
        /**
         *  generation of formula
         */
        int index = 0;
        Label label;
        FieldDescription fieldDescription;
        TextFieldEditor textFieldEditor;
        /**
         * attributes ot handle required value in login form
         */
        BooleanBinding b = null;
        for (String key : this.templateDataForLogin.getFormLoginDescription().keySet()) {
            label = new Label(key);
            fieldDescription = this.templateDataForLogin.getFormLoginDescription().get(key);
            textFieldEditor = ControllerSystem.getEditor(fieldDescription);
            this.data.put(fieldDescription.getFieldInformation().get(FieldDescription.COLUMNNAME).toLowerCase(), textFieldEditor.valueProperty());
            this.gridPane.add(label, 0, index);
            this.gridPane.add((Node) textFieldEditor.getEditor(), 1, index++);
            if (fieldDescription.getFieldDesingInformation().get(FieldDescription.REQUIRED).equals(FieldDescription.TRUE)) {
                if (b == null) {
                    b = Bindings.isEmpty(textFieldEditor.valueProperty());
                } else {
                    b = b.or(Bindings.isEmpty(textFieldEditor.valueProperty()));
                }
            }
        }
        /**
         * if b is not null, it means that there is at least one field required , then
         */
        this.bValider.disableProperty().bind(b);
    }


    public ControllerLogin(Stage stage, TemplateDataForLogin templateDataForLogin) {
        this.templateDataForLogin = templateDataForLogin;
        this.stage = stage;
    }

    /**
     * goal : define action perform when user validate its parameters
     *
     * @throws SQLException
     * @throws IncorrectDataException
     * @throws NotUniqueTableInQueryException
     * @throws MissingRubricException
     * @throws IOException
     */
    public void validation() throws SQLException, IncorrectDataException, IOException, ClassNotFoundException {
        SystemAdministrator.loadUser(this.data);
        /**
         * load system
         */
        try {
            SystemAdministrator.loadSystem();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotUniqueTableInQueryException e) {
            e.printStackTrace();
        } catch (WrongContainsOfFile wrongContainsOfFile) {
            wrongContainsOfFile.printStackTrace();
        }


        FXMLLoader loader = new FXMLLoader();
        ControllerAccueil controllerAccueil = new ControllerAccueil(this.stage);
        loader.setLocation(getClass().getResource("/views/Accueil.fxml"));
        loader.setControllerFactory(c -> controllerAccueil);
        Scene scene = new Scene(loader.load());
        this.stage.close();
        this.stage.setScene(scene);
        controllerAccueil.setScene(scene);
        this.stage.show();
    }

    /**
     * goal : define action perfrom where user discard session at validation parameters step
     */

    public void discard() {
        this.stage.close();
    }
}
