package systemGui;

import dataDefinition.ColumnMetaData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;

import java.time.format.DateTimeFormatter;

public class TextEditorFactory {

    private static final String patternDate = "yyyy-MM-dd";
    private static final LocalDateStringConverter dateStringConverter = new LocalDateStringConverter(DateTimeFormatter.ofPattern(TextEditorFactory.patternDate), DateTimeFormatter.ofPattern(TextEditorFactory.patternDate));

    /**
     *
     * @param fieldDescription
     * @return
     */

    public static TextFieldEditor<TextField> getTextField(FieldDescription fieldDescription) {
        TextFieldEditor<TextField> result = new TextFieldEditor<>();
        TextField editor = new TextField();
        StringProperty bindValue = new SimpleStringProperty();

        result.setEditor(editor);
        result.setLabel(fieldDescription.getLabel());
        result.setProperty(bindValue);
        /**
         * populate the properties of editor
         */
        editor.textProperty().bindBidirectional(bindValue);
        if (fieldDescription.getFieldDesingInformation().get(FieldDescription.REQUIRED).equals(FieldDescription.TRUE)) {
            TextEditorFactory.setRequiredEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");

        }
        return result;
    }

    public static TextFieldEditor<TextArea> getTextArea(FieldDescription fieldDescription) {
        TextFieldEditor<TextArea> result = new TextFieldEditor<>();
        TextArea editor = new TextArea();
        StringProperty bindValue = new SimpleStringProperty();

        result.setEditor(editor);
        result.setLabel(fieldDescription.getLabel());
        result.setProperty(bindValue);
        /**
         * populate the properties of editor
         */
        editor.textProperty().bindBidirectional(bindValue);
        if (fieldDescription.getFieldDesingInformation().get(FieldDescription.REQUIRED).equals(FieldDescription.TRUE)) {
            TextEditorFactory.setRequiredEffect((Node) editor);
        }
        return result;
    }

    public static TextFieldEditor<PasswordField> getPassworldField(FieldDescription fieldDescription) {
        TextFieldEditor<PasswordField> result = new TextFieldEditor<>();
        PasswordField editor = new PasswordField();
        Tooltip tooltip = new Tooltip("le mot de passe doit contenir 8 à 40 caractères, au moins un chiffre, au moins une majuscule et minuscule, au moins un caractère spécial");
        editor.setTooltip(tooltip);
        StringProperty bindValue = new SimpleStringProperty();

        result.setEditor(editor);
        result.setLabel(fieldDescription.getLabel());
        result.setProperty(bindValue);

        /**
         * populate the properties of editor
         */
        editor.textProperty().bindBidirectional(bindValue);
        TextEditorFactory.setRequiredEffect((Node) editor);
        TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
        return result;
    }

    public static TextFieldEditor<DatePicker> getDatePicker(FieldDescription fieldDescription) {
        TextFieldEditor<DatePicker> result = new TextFieldEditor<>();
        DatePicker editor = new DatePicker();
        StringProperty bindValue = new SimpleStringProperty();

        result.setEditor(editor);
        result.setLabel(fieldDescription.getLabel());
        result.setProperty(bindValue);
        /**
         * populate the properties of editor
         */
        Bindings.bindBidirectional(bindValue, editor.valueProperty(), TextEditorFactory.dateStringConverter);
        if (fieldDescription.getFieldDesingInformation().get(FieldDescription.REQUIRED).equals(FieldDescription.TRUE)) {
            TextEditorFactory.setRequiredEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
        }
        return result;
    }

    public static TextFieldEditor<TextField> getTextFieldByColumnMetaData(ColumnMetaData columnMetaData, String label) {
        TextFieldEditor<TextField> result = new TextFieldEditor<>();
        TextField editor = new TextField();
        StringProperty bindValue = new SimpleStringProperty();

        /**
         * populate the properties of editor
         */
        result.setEditor(editor);
        editor.textProperty().bindBidirectional(bindValue);
        if (label == null) {
            result.setLabel(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        } else {
            result.setLabel(label);
        }

        result.setLabel(label);
        result.setProperty(bindValue);

        if (ColumnMetaData.systemColumn.contains(columnMetaData.getName())) {
            editor.setEditable(false);
            TextEditorFactory.setNotEditableEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "LightSlateGrey");
        } else {
            if (columnMetaData.isReadonly()) {
                editor.setEditable(false);
                TextEditorFactory.setNotEditableEffect((Node) editor);
            }
            if (columnMetaData.isWritable()) {
                editor.setEditable(true);
            }
            if (!columnMetaData.isNullable()) {
                TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
            }
        }
        return result;
    }

    public static TextFieldEditor<TextArea> getTextAreaByColumnMetaData(ColumnMetaData columnMetaData, String label) {
        TextFieldEditor<TextArea> result = new TextFieldEditor<>();
        TextArea editor = new TextArea();
        StringProperty bindValue = new SimpleStringProperty();

        /**
         * populate the properties of editor
         */
        result.setEditor(editor);
        result.setProperty(bindValue);
        editor.textProperty().bindBidirectional(bindValue);
        if (label == null) {
            result.setLabel(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        } else {
            result.setLabel(label);
        }
        result.setLabel(label);

        if (ColumnMetaData.systemColumn.contains(columnMetaData.getName())) {
            editor.setEditable(false);
            TextEditorFactory.setNotEditableEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "LightSlateGrey");
        } else {
            if (columnMetaData.isReadonly()) {
                editor.setEditable(false);
                TextEditorFactory.setNotEditableEffect((Node) editor);
            }
            if (columnMetaData.isWritable()) {
                editor.setEditable(true);
            }
            if (!columnMetaData.isNullable()) {
                TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
            }
        }
        return result;
    }

    public static TextFieldEditor<PasswordField> getPasswordFieldByColumnMetaData(ColumnMetaData columnMetaData, String label) {
        TextFieldEditor<PasswordField> result = new TextFieldEditor<>();
        PasswordField editor = new PasswordField();
        StringProperty bindValue = new SimpleStringProperty();

        /**
         * populate the properties of editor
         */
        result.setEditor(editor);
        editor.textProperty().bindBidirectional(bindValue);
        if (label == null) {
            result.setLabel(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        } else {
            result.setLabel(label);
        }
        result.setProperty(bindValue);
        result.setLabel(label);

        if (ColumnMetaData.systemColumn.contains(columnMetaData.getName())) {
            editor.setEditable(false);
            TextEditorFactory.setNotEditableEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "LightSlateGrey");
        } else {
            if (columnMetaData.isReadonly()) {
                editor.setEditable(false);
                TextEditorFactory.setNotEditableEffect((Node) editor);
            }
            if (columnMetaData.isWritable()) {
                editor.setEditable(true);
            }
            if (!columnMetaData.isNullable()) {
                TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
            }
        }
        return result;
    }


    public static TextFieldEditor<DatePicker> getDatePickerByColumnMetaData(ColumnMetaData columnMetaData, String label) {
        TextFieldEditor<DatePicker> result = new TextFieldEditor<>();
        DatePicker editor = new DatePicker();
        StringProperty bindValue = new SimpleStringProperty();

        /**
         * populate the properties of editor
         */
        result.setEditor(editor);
        Bindings.bindBidirectional(bindValue, editor.valueProperty(), TextEditorFactory.dateStringConverter);
        if (label == null) {
            result.setLabel(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        } else {
            result.setLabel(label);
        }
        result.setProperty(bindValue);
        result.setLabel(label);

        if (ColumnMetaData.systemColumn.contains(columnMetaData.getName())) {
            editor.setEditable(false);
            editor.setDisable(true);
            TextEditorFactory.setNotEditableEffect((Node) editor);
            TextEditorFactory.setInnerBackgroundOfField((Node)editor, "LightSlateGrey");
        } else {
            if (columnMetaData.isReadonly()) {
                editor.setEditable(false);
                TextEditorFactory.setNotEditableEffect((Node) editor);
            }
            if (columnMetaData.isWritable()) {
                editor.setEditable(true);
            }
            if (!columnMetaData.isNullable()) {
                TextEditorFactory.setInnerBackgroundOfField((Node)editor, "paleturquoise");
            }
        }
        return result;
    }


    public static String removeUnderScrore(String chaine) {
        return chaine.replace("_", " ");
    }

    public static void setRequiredEffect(Node node) {
        if (node == null) throw new IllegalArgumentException();
        node.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
    }

    public static void setNotEditableEffect(Node node) {
        if (node == null) throw new IllegalArgumentException();
        node.setStyle("-fx-border-color: grey ; -fx-border-width: 2px ;");
    }

    public static void setInnerBackgroundOfField(Node node, String color) {
        node.setStyle("-fx-control-inner-background : "+color);
    }
}
