package controllers;

import com.jfoenix.controls.JFXButton;
import dataBaseManager.KeyGenerator;
import dataDefinition.*;
import exceptions.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import manageMomento.CareTaker;
import manageMomento.Originator;
import system.SystemAdministrator;
import systemGui.*;

public class ControllerForm implements Initializable, IControllerForm {

    /**
     * model attribute
     */
    private LinkedHashMap<String, StringProperty> data = new LinkedHashMap<>();
    private SpreadSheet spreadSheet;
    private String rubric;
    private double hgap;
    private double vgap;
    private Row selectedRow;
    private LinkedHashMap<String, Tab> activeTab;
    private LinkedHashMap<String, ListEditor> mapColumnListEditor = new LinkedHashMap<>();
    private Relation relation;
    private String valueFieldSource;
    /**
     * attributes to handle revert operation on row of tableView
     */
    private CareTaker<Row> rowCareTaker = new CareTaker<>();
    private Originator<Row> rowOriginator = new Originator<>();

    /**
     * attributes to handle insertion operation
     */
    private boolean insertionFlag = false;
    private boolean isFormEmpty = true;

    /**
     * attributes FXML
     */

    @FXML // fx:id="bInsertion"
    private JFXButton bInsertion; // Value injected by FXMLLoader

    @FXML // fx:id="bSuppression"
    private JFXButton bSuppression; // Value injected by FXMLLoader

    @FXML // fx:id="bAnnuler"
    private JFXButton bAnnuler; // Value injected by FXMLLoader

    @FXML // fx:id="bValider"
    private JFXButton bValider; // Value injected by FXMLLoader

    @FXML // fx:id="tableView"
    private TableView<Row> tableView; // Value injected by FXMLLoader

    @FXML // fx:id="gridPane"
    private GridPane gridPane; // Value injected by FXMLLoader

    @FXML // fx:id="relations"
    private MenuButton relations; // Value injected by FXMLLoader

    @FXML // fx:id="traitements"
    private MenuButton traitements; // Value injected by FXMLLoader

    @FXML
    private TabPane tabPane;

    private FXMLLoader fxmlLoader;

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
         * static initialization
         */
        this.hgap = 20;
        this.vgap = 20;
        /**
         * Building form
         */
        List<TableColumn<Row, String>> columns = new LinkedList<>();
        ColumnMetaData columnMetaData;
        String columnName;
        String labelName;
        BooleanBinding b = null;
        Editor editor = null;
        ForeignKey fk;

        this.gridPane.setHgap(this.hgap);
        this.gridPane.setVgap(this.vgap);
        /**
         * load tableColumn for numbering
         */
        columns.add(ControllerSystem.getTableColumnNumbering(this.tableView));
        /**
         * load editor
         */
        for (int index = 0; index < this.spreadSheet.numberOfColumn(); index++) {
            columnMetaData = this.spreadSheet.getColumnMetaData().get(this.spreadSheet.getIndexColumnName().getKeyByValue(index));
            columns.add(ControllerSystem.getTableColumnFromColumnMetaData(columnMetaData));
            columnName = columnMetaData.getName();
            labelName = TextEditorFactory.removeUnderScrore(columnName);
            /**
             * retrieve listEditor or TextFieldEditor
             */
            if (this.spreadSheet.getForeignKeys().containsKey(columnName)) {
                fk = this.spreadSheet.getForeignKeys().get(columnName);
                try {
                    editor = ListEditorFactory.getListEditor(columnMetaData, fk.getFkTableName(), columnName, fk.getPkTableName(), null);
                    this.mapColumnListEditor.put(columnName, (ListEditor)editor);
                } catch (SQLException e) {
                    AlertBox.createAlert(e);
                } catch (NotUniqueTableInQueryException e) {
                    AlertBox.createAlert(e);
                } catch (NotYetEditorException e) {
                    editor = ControllerSystem.getEditorFromColumnMetaData(columnMetaData, null);
                }
            } else {
                editor = ControllerSystem.getEditorFromColumnMetaData(columnMetaData, null);
            }
            this.data.put(columnName, editor.valueProperty());
            this.gridPane.add(new Label(labelName), index % 2 == 0 ? 2 : 0, index / 2);
            this.gridPane.add(editor.getEditor(), index % 2 == 0 ? 3 : 1, index / 2);
            if (!columnMetaData.isNullable() && !KeyGenerator.generateKey.contains(columnName)) {
                if (b == null) {
                    b = Bindings.isEmpty(editor.valueProperty());
                } else {
                    b = b.or(Bindings.isEmpty(editor.valueProperty()));
                }
            }

            /**
             * add a listener of implicant field
             */
        }
        this.tableView.getColumns().addAll(columns);
        this.tableView.setItems(this.spreadSheet);

        /**
         * logical activation of button
         */
        this.bValider.disableProperty().bind(b);

        /**
         * handle selection of row onto the tableview
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Row>() {
                    @Override
                    public void changed(ObservableValue<? extends Row> observable, Row oldValue, Row newValue) {
                        selectedRow = tableView.getSelectionModel().getSelectedItem();
                        if (selectedRow != null) {
                            try {
                                fillDataInForm(selectedRow);
                                rowOriginator.setState((Row) selectedRow.clone());
                                rowCareTaker.save(rowOriginator.originatorMomento());
                                insertionFlag = false;
                                bSuppression.setDisable(false);
                            } catch (CloneNotSupportedException e) {
                                AlertBox.createAlert(e);
                            }
                        }
                    }
                });
            }
        });

        /**
         * set action of button
         */
        this.bValider.setOnAction(c -> {
            this.valider();
        });
        this.bAnnuler.setOnAction(c -> {
            this.annuler();
        });
        this.bInsertion.setOnAction(c -> {
            this.insertion();
        });
        this.bSuppression.setOnAction(c -> {
            this.suppression();
        });

        /**
         * disable button of suppression
         */
        this.bSuppression.setDisable(true);

        /**
         * loadRelation in Menubutton
         */
        if (this.spreadSheet.getRelations() != null) {
            MenuItem menuItem;
            for (String key : this.spreadSheet.getRelations().keySet()) {
                menuItem = new MenuItem(this.spreadSheet.getRelations().get(key).getLibelle());
                this.relations.getItems().add(menuItem);
                /**
                 * set handle action
                 */
                menuItem.setOnAction(c -> {
                    try {
                        loadRelationalForm(this.spreadSheet.getRelations().get(key));
                    } catch (SQLException e) {
                        AlertBox.createAlert(e);
                    } catch (NotUniqueTableInQueryException | IOException e) {
                        AlertBox.createAlert(e);
                    }
                });
            }
        }
        /**
         * execute function to add listener on implicant field
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addListenerOnImplicantField();
            }
        });
    }

    public ControllerForm(String rubric, SpreadSheet spreadSheet, TabPane tabPane, LinkedHashMap<String, Tab> activeTab) {
        if (spreadSheet == null) throw new IllegalArgumentException();
        this.spreadSheet = spreadSheet;
        this.rubric = rubric;
        this.tabPane = tabPane;
        this.activeTab = activeTab;
    }

    private void fillDataInForm(Row row) {
        if (row == null) throw new IllegalArgumentException();
        String columnName;
        for (int index = 0; index < row.size(); index++) {
            columnName = this.spreadSheet.getIndexColumnName().getKeyByValue(index);
            this.data.get(columnName).set(row.getStringValue(index));
        }
        this.isFormEmpty = false;
    }

    private void cleanForm() {
        for (String columnName : this.data.keySet()) {
            this.data.get(columnName).set(null);
        }
        this.isFormEmpty = true;
    }

    /**
     * Perform some action when button 'Annuler' is clicked
     */
    @Override
    public void annuler() {
        if (this.insertionFlag) {
            this.cleanForm();
            this.insertionFlag = false;
            this.bInsertion.setDisable(false);
            return;
        }
        if (this.isFormEmpty) return;
        if (selectedRow != null && !this.isFormEmpty) {
            this.fillDataInForm(this.rowOriginator.revert(this.rowCareTaker.retrieve()));
        }
    }

    /**
     * Perform some action when button 'Valider' is clicked
     */
    @Override
    public void valider() {
        if (this.insertionFlag) {
            /**
             * perform operation for insertion
             */
            Row inserRow = this.convertDatatoRow(this.data);
            this.initializeMomentoManage();
            try {
                SystemAdministrator.insertIntoRubric(this.rubric, this.spreadSheet, inserRow);
            } catch (PermissionRequiredException e) {
                AlertBox.createAlert("Warning", "Permission requise", e.getMessage(), Alert.AlertType.WARNING);
            } catch (SQLException e) {
                AlertBox.createAlert(e);
            } catch (CodeOutOfBoundsException e) {
                AlertBox.createAlert(e);
            } catch (ImpossibleToInsertException e) {
                AlertBox.createAlert(e);
            } catch (CloneNotSupportedException e) {
                AlertBox.createAlert(e);
            }
            /**
             * after operation, handling of flag
             */
            this.cleanForm();
            this.insertionFlag = false;
            this.bSuppression.setDisable(true);
            return;
        }
        if (!this.isFormEmpty) {
            Row updateRow = this.convertDatatoRow(this.data);
            /**
             * perform operation to update the current row
             */
            try {
                SystemAdministrator.updateOnRubric(this.rubric, spreadSheet, updateRow);
            } catch (PermissionRequiredException e) {
                AlertBox.createAlert("Warning", "Permission requise", e.getMessage(), Alert.AlertType.WARNING);
            } catch (SQLException e) {
                AlertBox.createAlert(e);
            }

            /**
             * after database operation
             */
            this.copyRow(this.selectedRow, updateRow);
            return;
        }
    }

    /**
     * Perform some action when button 'Insertion' is clicked
     */
    @Override
    public void insertion() {
        /**
         * raise the flag of insertion, clean form and disable deletion button, raise the flag of emptyform
         */
        this.insertionFlag = true;
        // clearnForm() raise the flag "isEmptyForm"
        this.cleanForm();
        this.bSuppression.setDisable(true);
        /**
         * fill relational field if the form is relational form
         */
        if (this.relation != null && this.valueFieldSource != null) {
           this.data.get(this.relation.getFieldDestination()).set(this.valueFieldSource);
        }
    }

    /**
     * Perform some action when button 'Supprimer' is clicked
     */
    @Override
    public void suppression() {
        /**
         * operation on database
         */
        try {
            SystemAdministrator.deleteFromRubrique(this.rubric, this.spreadSheet, this.selectedRow);
        } catch (PermissionRequiredException e) {
            AlertBox.createAlert("Warning", "Permission requise", e.getMessage(), Alert.AlertType.WARNING);
        } catch (SQLException e) {
            AlertBox.createAlert(e);
        }

        this.insertionFlag = false;
        this.cleanForm();
        this.isFormEmpty = true;
    }


    /**
     * Perform some action when button of treatment is clicked
     * @param operationName
     * @param parameter
     */
    @Override
    public void applyOperation(String operationName, LinkedHashMap<String, String> parameter) {

    }

    private Row convertDatatoRow(LinkedHashMap<String, StringProperty> data) {
        if (data == null) throw new IllegalArgumentException();
        Row result = new Row(data.size());
        int index;
        for (String key : data.keySet()) {
            index = this.spreadSheet.getIndexColumnName().getValueByKey(key);
            result.setValue(index, data.get(key).getValue());
        }
        return result;
    }

    /**
     * function to initialize attribute which handle revert operation
     */
    private void initializeMomentoManage() {
        this.rowCareTaker = new CareTaker<>();
        this.rowOriginator = new Originator<>();
    }

    private void copyRow(Row des, Row source) {
        for (int index = 0; index < source.size(); index++) {
            des.setValue(index, source.getStringValue(index));
        }
    }

    private void loadRelationalForm(Relation relation) throws SQLException, NotUniqueTableInQueryException, IOException {
        if (this.selectedRow != null) {
            /**
             * perform some operation when user select row
             */
            Tab tab;
            if (!this.activeTab.containsKey("R"+valueFieldSource+" : " + rubric)) {
                String valueFieldSource = this.selectedRow.getStringValue(this.spreadSheet.getIndexColumnName().getValueByKey(relation.getFieldSource()));
                SpreadSheet spreadSheet = SystemAdministrator.loadRelationalRubric(relation, valueFieldSource);
                ControllerForm controllerForm= new ControllerForm(relation.getTableSource(), spreadSheet, this.tabPane, this.activeTab);
                controllerForm.setValueFieldSource(valueFieldSource);
                controllerForm.setRelation(relation);
                this.fxmlLoader = new FXMLLoader();
                this.fxmlLoader.setLocation(getClass().getResource("/views/Form.fxml"));
                this.fxmlLoader.setControllerFactory(c -> controllerForm);
                tab = new Tab("R"+ valueFieldSource+" : " + rubric);
                tab.setId("R"+valueFieldSource+" : " + rubric);
                tab.setContent(this.fxmlLoader.load());
                this.activeTab.put("R"+valueFieldSource+" : " + rubric, tab);
                this.tabPane.getTabs().add(tab);
                this.tabPane.getSelectionModel().select(tab);
                /**
                 * remove the tab in LinkedHashMap of active tab when a tab is closed
                 */
                tab.setOnClosed(c -> {
                    this.activeTab.remove("R"+valueFieldSource+" : " + rubric);
                });
            } else {
                tab = this.activeTab.get("R"+valueFieldSource+" : " + rubric);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabPane.getSelectionModel().select(tab);
                    }
                });
            }
        }
    }

    private void addListenerOnImplicantField() {

        for (String columnSource : this.spreadSheet.getForeignKeys().keySet()) {
            /**
             * set FieldImplicant
             */
            if (SystemAdministrator.containsFieldImplicant(this.spreadSheet.getTableName(), columnSource)) {
                this.data.get(columnSource).addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        fillFormViaImplicant(spreadSheet.getTableName(), columnSource, newValue);
                    }
                });
            }
            /**
             * set ListImplicant
             */
            if (SystemAdministrator.containsListImplicant(spreadSheet.getTableName(), columnSource)) {
                this.data.get(columnSource).addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        ListImplicant listImplicant = SystemAdministrator.getListImplicant(spreadSheet.getTableName(), columnSource);
                        if (mapColumnListEditor.containsKey(listImplicant.getFieldTarget())) {
                            listImplicant.setValueFieldSource(newValue);
                            ForeignKey fk = spreadSheet.getForeignKeys().get(listImplicant.getFieldTarget());
                            changeListImpliquant(columnSource, fk.getPkTableName(), listImplicant);
                        }
                    }
                });

            }

        }
    }

    private void fillFormViaImplicant(String tableSource, String columnSource, String valueFieldSource) {
        LinkedHashMap<String, Object> objectLinkedHashMap;
        Row row;
        BidirectionalLinkedHashMap<String, Integer> indexColumnName;
        FieldImplicant fieldImplicant = SystemAdministrator.getFielImpliquant(tableSource, columnSource);
        fieldImplicant.setValueColumnSource(valueFieldSource);
        try {
            objectLinkedHashMap = FieldImplicant.processFieldImplicant(this.spreadSheet.getForeignKeys().get(columnSource), fieldImplicant);
            row = (Row)objectLinkedHashMap.get(FieldImplicant.ROW);
            indexColumnName = (BidirectionalLinkedHashMap<String, Integer>)objectLinkedHashMap.get(FieldImplicant.BIDIRECTIONALMAP);
            /**
             * fill field in form
             */
            for (String key : fieldImplicant.getMappingColumn().keySet()) {
                this.data.get(key).set(row.getStringValue(indexColumnName.getValueByKey(fieldImplicant.getMappingColumn().get(key))));
            }
        } catch (SQLException e) {
            AlertBox.createAlert(e);
        } catch (NotUniqueTableInQueryException e) {
            AlertBox.createAlert(e);
        } catch (ImplicantException e) {
            AlertBox.createAlert(e);
        }
    }

    private void changeListImpliquant(String fieldSource, String tableDestination, ListImplicant listImplicant) {
        String fieldTarget = listImplicant.getFieldTarget();
        ListEditor listEditor = this.mapColumnListEditor.get(fieldTarget);
        try {
            listEditor.getComboBox().setItems(ListImplicant.processListImplicant(fieldSource, tableDestination, listImplicant));
        } catch (SQLException e) {
            AlertBox.createAlert(e);
        } catch (NotUniqueTableInQueryException e) {
            AlertBox.createAlert(e);
        }
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getValueFieldSource() {
        return valueFieldSource;
    }

    public void setValueFieldSource(String valueFieldSource) {
        this.valueFieldSource = valueFieldSource;
    }
}
