package systemGui;

import dataDefinition.Row;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ListEditor extends Editor {

    private TextField textField = new TextField();
    private String label;
    private ComboBox<Row> comboBox = new ComboBox();
    private VBox vbox = new VBox();
    private LinkedHashMap<String, Row> rowLinkedHashMap = new LinkedHashMap<>();

    public ListEditor(ObservableList<Row> listRow) {
        if (listRow == null) throw new IllegalArgumentException();
        this.vbox.getChildren().add(comboBox);
        this.vbox.getChildren().add(this.textField);
        this.comboBox.setItems(listRow);
        /**
         * define render of comboBox
         */
        this.comboBox.setCellFactory(new Callback<ListView<Row>, ListCell<Row>>() {
            @Override
            public ListCell<Row> call(ListView<Row> param) {
                final ListCell<Row> cell = new ListCell<Row>() {
                    @Override
                    public void updateItem(Row item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getStringValue(1));
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        /**
         * add listener to comboBox
         */
        this.comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Row>() {
            @Override
            public void changed(ObservableValue<? extends Row> observable, Row oldValue, Row newValue) {
                if (newValue != null)
                    textField.setText(newValue.getStringValue(0));
            }
        });
        /**
         * bind size of comboBox and textField
         */
        this.comboBox.prefHeightProperty().bind(this.textField.heightProperty());
        this.comboBox.prefWidthProperty().bind(this.textField.widthProperty());

        /**
         * add listener on textProperty
         */
        this.textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && rowLinkedHashMap.containsKey(newValue)) {
                    comboBox.setValue(rowLinkedHashMap.get(newValue));
                }
                else {
                    comboBox.setValue(null);
                }
            }
        });
        /**
         * run thread to populate rowHashMap
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                populatedHasMap(listRow);
            }
        });
    }

    public StringProperty valueProperty() {
        return this.textField.textProperty();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public VBox getEditor() {
        return vbox;
    }

    public ComboBox<Row> getComboBox() {
        return comboBox;
    }

    private void populatedHasMap(List<Row> rows) {
        for (Row row : rows)
            this.rowLinkedHashMap.put(row.getStringValue(0), row);
    }
}
