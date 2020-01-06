package dataDefinition;

public class Relation {
    private String tableSource = new String();
    private String tableDestination = new String();
    private String fieldSource = new String();
    private String fieldDestination = new String();
    private String libelle = new String();

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTableSource() {
        return tableSource;
    }

    public void setTableSource(String tableSource) {
        this.tableSource = tableSource;
    }

    public String getTableDestination() {
        return tableDestination;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }

    public String getFieldSource() {
        return fieldSource;
    }

    public void setFieldSource(String fieldSource) {
        this.fieldSource = fieldSource;
    }

    public String getFieldDestination() {
        return fieldDestination;
    }

    public void setFieldDestination(String fieldDestination) {
        this.fieldDestination = fieldDestination;
    }
}
