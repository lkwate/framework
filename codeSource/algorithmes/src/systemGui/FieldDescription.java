package systemGui;

import javafx.beans.property.StringProperty;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;

public class FieldDescription {

    public static final String TYPEEDITOR = "TYPEEDITOR";
    public static final String VALUE = "VALUE";
    public static final String COLUMNNAME = "COLUMNNAME";
    public static final String REQUIRED = "REQUIRED";
    public static final String PROMPTTEXT = "PROMPTTEXT";
    public static final String TOOLTIPS = "TOOLTIPS";

    public static final String FIELDTEXT = "TEXTFIELD";
    public static final String DATEPICKER = "DATEPICKER";
    public static final String TEXTAREA = "TEXTAREA";
    public static final String PASSWORD = "PASSWORDFIELD";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    private static Set<String> keyWords = new HashSet<>();
    static  {
        FieldDescription.keyWords.add(FieldDescription.TYPEEDITOR);
        FieldDescription.keyWords.add(FieldDescription.COLUMNNAME);
        FieldDescription.keyWords.add(FieldDescription.REQUIRED);
        FieldDescription.keyWords.add(FieldDescription.FIELDTEXT);
        FieldDescription.keyWords.add(FieldDescription.DATEPICKER);
        FieldDescription.keyWords.add(FieldDescription.TEXTAREA);
        FieldDescription.keyWords.add(FieldDescription.PASSWORD);
        FieldDescription.keyWords.add(FieldDescription.PROMPTTEXT);
        FieldDescription.keyWords.add(FieldDescription.TOOLTIPS);
    }
    private String label;
    private LinkedHashMap<String, String> fieldInformation;
    private LinkedHashMap<String, String> fieldDesingInformation;
    private LinkedHashMap<String, StringProperty> data = new LinkedHashMap<>();

    public FieldDescription(String description) {
        if (description == null) throw new IllegalArgumentException();
        description = description.trim();
        String fieldDescriptor1;
        String fieldDescriptor2;
        String[] fieldDescriptors = description.split("\\$");
        fieldDescriptor1 = fieldDescriptors[0].trim();
        fieldDescriptor2 = fieldDescriptors[1].trim();
        this.fieldInformation = this.getLinkedHashMapFromString(fieldDescriptor1);
        this.fieldDesingInformation = this.getLinkedHashMapFromString(fieldDescriptor2);
    }

    private LinkedHashMap<String, String> getLinkedHashMapFromString(String description) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        String keyValue[];
        for (String s : description.split(";")) {
            keyValue = (s.trim()).split("=");
            if (FieldDescription.keyWords.contains(keyValue[0].trim().toUpperCase()))
                result.put(keyValue[0].trim().toUpperCase(), keyValue[1].trim().toUpperCase());
        }
        return result;
    }

    public LinkedHashMap<String, String> getFieldInformation() {
        return fieldInformation;
    }

    public LinkedHashMap<String, String> getFieldDesingInformation() {
        return fieldDesingInformation;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
