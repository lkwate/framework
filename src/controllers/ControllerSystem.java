package controllers;

import dataDefinition.ColumnMetaData;
import dataDefinition.Row;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.*;
import javafx.util.Callback;
import systemGui.FieldDescription;
import systemGui.TextEditorFactory;
import systemGui.TextFieldEditor;

public class ControllerSystem {
    public static TextFieldEditor getEditor(FieldDescription fieldDescription) {
        switch (fieldDescription.getFieldInformation().get(FieldDescription.TYPEEDITOR)) {
            case FieldDescription.PASSWORD:
                return TextEditorFactory.getPassworldField(fieldDescription);
            case FieldDescription.TEXTAREA:
                return TextEditorFactory.getTextArea(fieldDescription);
            case FieldDescription.DATEPICKER:
                return TextEditorFactory.getDatePicker(fieldDescription);
            default:
                return TextEditorFactory.getTextField(fieldDescription);
        }
    }

    public static TextFieldEditor getEditorFromColumnMetaData(ColumnMetaData columnMetaData, String label) {
        if (columnMetaData == null) throw new IllegalArgumentException();
        if (columnMetaData.getName().equals("password"))
            return TextEditorFactory.getPasswordFieldByColumnMetaData(columnMetaData, label);
        switch (columnMetaData.getTypeName()) {
            case ColumnMetaData.sqlCHAR:
                return TextEditorFactory.getTextFieldByColumnMetaData(columnMetaData, label);
            case ColumnMetaData.sqlVARCHAR:
                return TextEditorFactory.getTextFieldByColumnMetaData(columnMetaData, label);
            case ColumnMetaData.sqlTEXT:
                return TextEditorFactory.getTextAreaByColumnMetaData(columnMetaData, label);
            case ColumnMetaData.sqlDATE:
                return TextEditorFactory.getDatePickerByColumnMetaData(columnMetaData, label);
            default:
                return TextEditorFactory.getTextFieldByColumnMetaData(columnMetaData, label);
        }
    }

    public static TableColumn<Row, String> getTableColumnNumbering(TableView<Row> tableView) {
        if (tableView == null) throw new IllegalArgumentException();
        TableColumn<Row, String> tableColumn = new TableColumn<>("N°");
        tableColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<String>(Integer.toString(tableView.getItems().indexOf(column.getValue()) + 1)));
        return tableColumn;
    }

    public static TableColumn<Row, String> getTableColumnFromColumnMetaData(ColumnMetaData columnMetaData) {
        if (columnMetaData == null) throw new IllegalArgumentException();
        TableColumn<Row, String> tableColumn = new TableColumn<Row, String>(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        int index = columnMetaData.getIndex();
        tableColumn.setCellValueFactory(param -> param.getValue().getValue(index));
        tableColumn.setEditable(false);
        /**
         * if the column if password hide content
         */
        if (columnMetaData.getName().equals("password")) {
            tableColumn.setCellFactory(new Callback<TableColumn<Row, String>, TableCell<Row, String>>() {
                @Override
                public TableCell<Row, String> call(TableColumn<Row, String> param) {
                    return new PasswordFieldCell();
                }
            });
        } else {
            tableColumn.setCellFactory(new Callback<TableColumn<Row, String>, TableCell<Row, String>>() {
                @Override
                public TableCell<Row, String> call(TableColumn<Row, String> param) {
                    return new TextFieldCell();
                }
            });
        }
        /**
         * set Style
         */
        if (ColumnMetaData.systemColumn.contains(columnMetaData.getName())) {
            tableColumn.setStyle("-fx-control-inner-background : LightSlateGrey");
            return tableColumn;
        }
        if (!columnMetaData.isNullable()) {
            tableColumn.setStyle("-fx-control-inner-background : paleturquoise");
            return tableColumn;
        }
        return tableColumn;
    }

    private static class PasswordFieldCell extends TableCell<Row, String> {
        private PasswordField passwordField;

        public PasswordFieldCell() {
            passwordField = new PasswordField();
            passwordField.setEditable(false);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                passwordField.setText(item);
                setGraphic(passwordField);
            } else {
                setGraphic(null);
            }
        }
    }

    private static class TextFieldCell extends TableCell<Row, String> {
        private TextField textField;

        public TextFieldCell() {
            textField = new TextField();
            textField.setEditable(false);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                textField.setText(item);
                setGraphic(textField);
            } else {
                setGraphic(null);
            }
        }
    }
}




