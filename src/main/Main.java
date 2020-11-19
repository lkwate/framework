package main;

import controllers.ControllerLogin;
import dataBaseManager.DataBaseAdministrator;
import exceptions.IncorrectDataException;
import exceptions.NotUniqueTableInQueryException;
import exceptions.WrongContainsOfFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.SystemAdministrator;
import systemGui.GuiDataAdministrator;
import systemGui.TemplateDataForLogin;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    /**
     * attributes
     */
    private TemplateDataForLogin templateDataForLogin;
    /**
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws WrongContainsOfFile, IOException {


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/Login.fxml"));
        fxmlLoader.setControllerFactory(c->{return new ControllerLogin(primaryStage, this.templateDataForLogin);
        });
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    @Override
    public void init() throws WrongContainsOfFile, SQLException, IOException, ClassNotFoundException, NotUniqueTableInQueryException{
        /**
         * load login form
         */
        this.templateDataForLogin = GuiDataAdministrator.loadTemplateDataLoginForm();
    }

    @Override
    public void stop() {

    }

}
