package systemGui;

import exceptions.WrongContainsOfFile;
import system.In;

import java.util.LinkedHashMap;

public class TemplateDataForLogin {

    public static final String LABEL = "LABEL";


    private String table;
    private LinkedHashMap<String, FieldDescription> formLoginDescription = new LinkedHashMap<>();

    public TemplateDataForLogin(String fileName) throws WrongContainsOfFile {
        In in = new In(fileName);
        this.table = in.readLine();
        String[] labels;
        String[] descriptions;
        String label = null;
        FieldDescription fieldDescription;
        while(!in.isEmpty()) {
            descriptions = in.readLine().trim().split("\\|");
            labels = descriptions[0].trim().split("=");
            if (TemplateDataForLogin.LABEL.equals(labels[0].trim().toUpperCase())) {
                label = labels[1];
            }
            fieldDescription = new FieldDescription(descriptions[1]);
            if (label == null) {
                label = fieldDescription.getFieldInformation().get(FieldDescription.COLUMNNAME);
            }
            if (label == null) throw new WrongContainsOfFile("the contains of file "+fileName+" is wrong, please check it");
            fieldDescription.setLabel(label);
            this.formLoginDescription.put(label, fieldDescription);
        }
    }
    public LinkedHashMap<String, FieldDescription> getFormLoginDescription() {
        return formLoginDescription;
    }
}
