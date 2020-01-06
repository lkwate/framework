package systemGui;

import dataDefinition.ColumnMetaData;
import dataDefinition.Row;
import exceptions.NotUniqueTableInQueryException;
import exceptions.NotYetEditorException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import system.SystemAdministrator;

import java.sql.SQLException;

public class ListEditorFactory {

    public static ListEditor getListEditor(ColumnMetaData columnMetaData, String tableSource, String columnSource, String tableDestination, String label) throws SQLException, NotUniqueTableInQueryException, NotYetEditorException {
        ObservableList<Row> result = SystemAdministrator.getDataListEditor(tableSource, columnSource, tableDestination);
        ListEditor listEditor = new ListEditor(result);
        if (label == null) {
            listEditor.setLabel(TextEditorFactory.removeUnderScrore(columnMetaData.getName()));
        } else {
            listEditor.setLabel(label);
        }
        if (!columnMetaData.isNullable()) {
            TextEditorFactory.setInnerBackgroundOfField((Node)listEditor.getTextField(), "paleturquoise");
        }
        return listEditor;
    }
}
