package systemGui;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public abstract class Editor {

    public abstract StringProperty valueProperty();

    public abstract Node getEditor();
}
