package dataDefinition;

import dataBaseManager.DataBaseAdministrator;
import exceptions.NotUniqueTableInQueryException;
import javafx.collections.ObservableList;
import system.SystemAdministrator;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ListImplicant {

    private String valueFieldSource = new String();
    private String fieldTarget = new String();
    private String line = new String();
    private String libelle = new String();
    private List<LinkedHashMap<String, Rule>> rules = new LinkedList<>();

    public String getValueFieldSource() {
        return valueFieldSource;
    }

    public void setValueFieldSource(String valueFieldSource) {
        this.valueFieldSource = valueFieldSource;
    }

    public List<LinkedHashMap<String, Rule>> getRules() {
        return rules;
    }

    public void setRules(List<LinkedHashMap<String, Rule>> rules) {
        this.rules = rules;
    }

    public String getFieldTarget() {
        return fieldTarget;
    }

    public void setFieldTarget(String fieldTarget) {
        this.fieldTarget = fieldTarget;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public static ObservableList<Row> processListImplicant(String fieldSource, String tableDestination, ListImplicant listImplicant) throws SQLException, NotUniqueTableInQueryException {
        String tempLine = listImplicant.getLine();
        tempLine = tempLine.replace(":"+fieldSource, listImplicant.getValueFieldSource());
        List<LinkedHashMap<String, Rule>> rules = SystemAdministrator.getRuleOfListImplicant(tempLine.trim().split("##")[1].trim().split("\\$"));
        SpreadSheet spreadSheet = new SpreadSheet(DataBaseAdministrator.getConnection(), DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, tableDestination, 0, null, null, null, -1, -1, rules));
        return spreadSheet.getDataForEditor(listImplicant.getLibelle());
    }
}
