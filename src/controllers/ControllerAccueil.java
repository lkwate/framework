package controllers;

import dataDefinition.SpreadSheet;
import exceptions.MissingRubricException;
import exceptions.NotUniqueTableInQueryException;
import exceptions.PermissionRequiredException;
import exceptions.WrongContainsOfFile;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import system.SingleMenu;
import system.SystemAdministrator;
import systemGui.AlertBox;
import systemGui.GuiDataAdministrator;
import systemGui.TemplateDataForLogin;


public class ControllerAccueil implements Initializable, IControllerAccueil {

    /**
     * attributes
     */
    private Stage stage;
    private Scene scene;
    private SpreadSheet activeSpreasheet;
    private LinkedHashMap<String, Tab> activeTap = new LinkedHashMap<>();

    /**
     * FXML attributes
     */

    FXMLLoader loader = new FXMLLoader();

    @FXML // fx:id="borderPane"
    private BorderPane borderPane; // Value injected by FXMLLoader

    @FXML // fx:id="menuBar"
    private MenuBar menuBar; // Value injected by FXMLLoader

    @FXML // fx:id="tabPane"
    private TabPane tabPane; // Value injected by FXMLLoader

    @FXML // fx:id="menuButton"
    private MenuButton menuButton; // Value injected by FXMLLoader

    @FXML // fx:id="bDeconnexion"
    private MenuItem bDeconnexion; // Value injected by FXMLLoader

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
        /**
         * initialization of menuBar
         */
        this.loadListRootMenu(SystemAdministrator.getMenuTree().getRootMenu());

        /**
         * design borderPane
         */
        this.borderPane.setRight(null);
        this.borderPane.setBottom(null);
        /**
         * allow closable tab in tabPane
         */
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        /**
         * set Action on button
         */
        this.bDeconnexion.setOnAction(c->{
            this.logOut();
        });
    }

    public ControllerAccueil(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void loadRubric(String rubricName) {
        /**
         * retrieve rubric on dataSet and load it in form
         */
        try {
            Tab tab;
            if (!this.activeTap.containsKey(rubricName)) {
                this.loader = new FXMLLoader();
                /**
                 * thread
                 */
                this.activeSpreasheet = SystemAdministrator.loadRubric(rubricName);
                this.loader.setLocation(getClass().getResource("/views/Form.fxml"));
                this.loader.setControllerFactory(c-> new ControllerForm(rubricName, this.activeSpreasheet, this.tabPane, this.activeTap));
                tab = new Tab(rubricName);
                tab.setId(rubricName);
                tab.setContent(this.loader.load());
                this.tabPane.getTabs().add(tab);
                this.activeTap.put(rubricName, tab);
                this.tabPane.getSelectionModel().select(tab);
                /**
                 * remove the tab in LinkedHashMap of active tab when a tab is closed
                 */
                tab.setOnClosed(c->{
                    activeTap.remove(rubricName);
                });
            }
            else {
                tab = activeTap.get(rubricName);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabPane.getSelectionModel().select(tab);
                    }
                });
            }
        } catch(PermissionRequiredException e) {
            AlertBox.createAlert("Permission required", "Permission required", "you don't have right to acces to this rubric", Alert.AlertType.WARNING);
        } catch (MissingRubricException e) {
            AlertBox.createAlert(e);
        } catch (SQLException e) {
            AlertBox.createAlert(e);
        } catch (NotUniqueTableInQueryException e) {
            AlertBox.createAlert(e);
        } catch (IOException e) {
            AlertBox.createAlert(e);
        }
    }

    public void loadListRootMenu(List<SingleMenu> singleMenuList) {
        Menu menu;
        for (SingleMenu singleMenu : singleMenuList) {
            menu = new Menu(singleMenu.getNameMenu());
            this.menuBar.getMenus().add(menu);
            this.loadInternalMenu(menu, singleMenu);
        }
    }

    private void loadInternalMenu(Menu menu, SingleMenu singleMenu) {
        if (singleMenu == null) throw new IllegalArgumentException();
        if (singleMenu.isLeaf() ) {
            if (singleMenu.getTableName() != null) {
                MenuItem menuItem = new MenuItem(singleMenu.getNameMenu());
                menuItem.setOnAction(c->{loadRubric(singleMenu.getNameMenu());});
                menu.getItems().add(menuItem);
                return;
            }
        }
        else {
            Menu menu1;
            for (SingleMenu singleMenu1 : singleMenu.getChildMenus().values()) {
                if (singleMenu1.isLeaf())
                    loadInternalMenu(menu, singleMenu1);
                else {
                    menu1 = new Menu(singleMenu1.getNameMenu());
                    menu.getItems().add(menu1);
                    loadInternalMenu(menu1, singleMenu1);
                }
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void logOut() {
        /**
         * clear System information
         */
        SystemAdministrator.logOutUser();
        /**
         * build view
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/Login.fxml"));
        try {
            TemplateDataForLogin templateDataForLogin = GuiDataAdministrator.loadTemplateDataLoginForm();
            loader.setControllerFactory(c->{return new ControllerLogin(this.stage, templateDataForLogin);});
            this.stage.close();
            this.stage.setScene(new Scene(loader.load()));
            this.stage.show();
            this.stage.centerOnScreen();
            this.stage.setResizable(false);
        } catch (WrongContainsOfFile wrongContainsOfFile) {
            AlertBox.createAlert(wrongContainsOfFile);
        } catch (IOException e) {
            AlertBox.createAlert(e);
        }
    }
}
