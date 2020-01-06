package systemGui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class TextFieldEditor<T> extends Editor{

    private StringProperty value = new SimpleStringProperty();
    private T editor;
    private String label;

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public void setProperty(StringProperty stringProperty) {
        this.value = stringProperty;
    }

    public Node getEditor() {
        return (Node)editor;
    }

    public void setEditor(T editor) {
        this.editor = editor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
