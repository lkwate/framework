package system;

import dataBaseManager.DataBaseAdministrator;
import dataBaseManager.KeyGenerator;
import dataDefinition.*;
import exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import security.Encryptor;
import security.SecureOperation;
import security.Validator;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;


public class SystemAdministrator {

    /**
     * attributes
     */
    private static Set<String> systemTables = new HashSet<>();
    private static Set<String> referenceTables = new HashSet<>();
    private static String userCode;
    private static BidirectionalLinkedHashMap<String, String> nameRubricNameTableMap = new BidirectionalLinkedHashMap<>();
    private static LinkedHashMap<String, SpreadSheet> systemDataset = new LinkedHashMap<>();
    private static CacheDataSet cacheDataSet = new CacheDataSet();
    private static MenuTree menuTree;
    private static LinkedHashMap<String, LinkedHashMap<String, Relation>> relations = new LinkedHashMap<>();
    private static LinkedHashMap<String, EditorMetaData> globalEditorMetaData = new LinkedHashMap<>();
    private static LinkedHashMap<String, LinkedHashMap<String, EditorMetaData>> localEditorMetaData = new LinkedHashMap<>();
    private static Set<String> rights = new HashSet<>();
    /**
     * attributes descriptor of implicants
     */
    private static LinkedHashMap<String, LinkedHashMap<String, FieldImplicant>> fieldImplicant = new LinkedHashMap<>();
    private static LinkedHashMap<String, LinkedHashMap<String, ListImplicant>> listImplicant = new LinkedHashMap<>();

    /**
     * pattern for password and alphanumeric text
     */
    public static Pattern REGEXPASSWORD = Pattern.compile("((?=.*[a-z])(?=.*\\\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");

    private static final String PATHTOFILEEDITOR = "editors/editors.txt";
    private static final String PATHTOFIELDIMPLICANT = "impliquants/fieldImpliquant.txt";
    private static final String PATHTOLISTIMPLICANT = "impliquants/listImpliquants.txt";


    static {
        /*
         * statut, groupe, rubrique, sexe, typeRubrique, typeUtilisateur, utilisateur
         */
        SystemAdministrator.systemTables.add("statut");
        SystemAdministrator.systemTables.add("groupe");
        SystemAdministrator.systemTables.add("rubrique");
        SystemAdministrator.systemTables.add("sexe");
        SystemAdministrator.systemTables.add("typeRubrique");
        SystemAdministrator.systemTables.add("typeUtilisateur");
        //SystemAdministrator.systemTables.add("utilisateur");
        SystemAdministrator.referenceTables.add("statut");
        SystemAdministrator.referenceTables.add("typeRubrique");

    }

    private SystemAdministrator() {

    }

    /**
     * goal : load all necessary data for operation on system
     *
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NotUniqueTableInQueryException
     * @throws WrongContainsOfFile
     */
    public static void loadSystem() throws SQLException, IOException, ClassNotFoundException, NotUniqueTableInQueryException, WrongContainsOfFile {
        /**
         * load dataBaseAdministrator and KeyGenerator
         */
        DataBaseAdministrator.loadConnection();
        /**
         * Load keyGenerator
         */
        KeyGenerator.loadData(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema);
        /**
         * load dataSet
         */
        SpreadSheet ss;
        for (String tableName : SystemAdministrator.systemTables) {
            ss = new SpreadSheet(DataBaseAdministrator.getConnection(), DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, tableName, 0, null, null, null, -1, -1, null));
            SystemAdministrator.systemDataset.put(tableName, ss);
        }

        /**
         * populate BidirectionalLinkedHashMap with information of leaf rubric
         */
        List<LinkedHashMap<String, Rule>> rules = new LinkedList<>();
        LinkedHashMap<String, Rule> r = new LinkedHashMap<>(1);
        Rule rule = new Rule();
        rule.setOperator(Rule.EQUAL);
        rule.setValue("101");
        r.put("code_typeRubrique", rule);
        rules.add(r);
        ss = new SpreadSheet(DataBaseAdministrator.getConnection(), DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, "rubrique", 0, null, null, null, -1, -1, rules));
        int indexOfRubricName = ss.getIndexColumnName().getValueByKey("nom_rubrique");
        int indexOfTableName = ss.getIndexColumnName().getValueByKey("tableName");
        for (Row row : ss.getRows()) {
            SystemAdministrator.nameRubricNameTableMap.putKeyValue(row.getStringValue(indexOfRubricName), row.getStringValue(indexOfTableName));
        }

        /**
         * load information about branche rubric // menu
         */
        SystemAdministrator.menuTree = new MenuTree(SystemAdministrator.systemDataset.get("rubrique"));

        /**
         * load relation
         */
        SystemAdministrator.loadRelation();

        /**
         *  load editors
         */
        SystemAdministrator.loadEditorMetaData(SystemAdministrator.PATHTOFILEEDITOR);

        /**
         * load FieldImplicant
         */
        SystemAdministrator.loadFieldImplicant();

        /**
         * load ListImplicant
         */
        SystemAdministrator.loadListImpliquant();
    }

    /**
     * @param relation
     * @param valueFieldSource
     * @return
     * @throws SQLException
     * @throws NotUniqueTableInQueryException
     */
    public static SpreadSheet loadRelationalRubric(Relation relation, String valueFieldSource) throws SQLException, NotUniqueTableInQueryException {
        if (relation == null || valueFieldSource == null) throw new IllegalArgumentException();
        SpreadSheet spreadSheet = null;
        Rule rule = new Rule();
        rule.setOperator(Rule.EQUAL);
        rule.setValue(valueFieldSource);
        List<LinkedHashMap<String, Rule>> rules = new LinkedList<>();
        LinkedHashMap<String, Rule> r = new LinkedHashMap<>(1);
        r.put(relation.getFieldDestination(), rule);
        rules.add(r);
        /**
         * thread
         */
        spreadSheet = new SpreadSheet(DataBaseAdministrator.getConnection(), DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, relation.getTableDestination(), 0, null, null, null, -1, -1, rules));
        /**
         * assign relation
         */
        if (SystemAdministrator.relations.containsKey(relation.getTableDestination()))
            spreadSheet.setRelations(SystemAdministrator.relations.get(relation.getTableDestination()));
        /**
         * secure checking : thread
         */
        SpreadSheet finalSpreadSheet = spreadSheet;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SecureOperation.checkSecureRead(null, finalSpreadSheet);
            }
        });
        return spreadSheet;
    }

    /**
     * goal : load data from a given rubric
     *
     * @param rubricName
     * @return
     * @throws MissingRubricException
     * @throws SQLException
     * @throws NotUniqueTableInQueryException
     */
    public static SpreadSheet loadRubric(String rubricName) throws MissingRubricException, SQLException, NotUniqueTableInQueryException, PermissionRequiredException {
        if (rubricName == null) throw new IllegalArgumentException();

        if (!SystemAdministrator.nameRubricNameTableMap.containsKey(rubricName))
            throw new MissingRubricException("rubric " + rubricName + " doesn't exists");

        /**
         * check whether the user have a right to log into rubric
         */
        SecureOperation.checkSecureRightRead(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName));

        String tableName = SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName);
        SpreadSheet result = null;
        /**
         * check if exists in system dataSet
         */
        if (SystemAdministrator.systemDataset.containsKey(tableName)) {
            result = SystemAdministrator.systemDataset.get(tableName);
        }
        /**
         * check if exists in cache dataSet
         */
        if (result == null && SystemAdministrator.cacheDataSet.getDataSet().containsKey(tableName)) {
            result = SystemAdministrator.cacheDataSet.getDataSet().get(tableName);
        }
        /**
         * thread
         */
        if (result == null) {
            result = SystemAdministrator.loadRubricNativelyByTableName(tableName);
        }
        ForeignKey fk;
        for (String key : result.getForeignKeys().keySet()) {
            fk = result.getForeignKeys().get(key);
            if (!SystemAdministrator.systemDataset.containsKey(fk.getPkTableName()) && !SystemAdministrator.cacheDataSet.getDataSet().containsKey(fk.getPkTableName()))
                SystemAdministrator.loadRubricNativelyByTableName(fk.getPkTableName());
        }

        /**
         * assign set of relations : thread
         */
        if (SystemAdministrator.relations.containsKey(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName))) {
            result.setRelations(SystemAdministrator.relations.get(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName)));
        }
        /**
         * secure checking
         */
        SpreadSheet finalResult = result;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SecureOperation.checkSecureRead(rubricName, finalResult);
            }
        });

        /**
         * return result
         */
        return result;
    }

    public static SpreadSheet loadRubricNativelyByTableName(String tableName) throws SQLException, NotUniqueTableInQueryException {
        if (tableName == null) throw new IllegalArgumentException();
        if (SystemAdministrator.systemDataset.containsKey(tableName))
            return SystemAdministrator.systemDataset.get(tableName);
        if (SystemAdministrator.cacheDataSet.dataSet.containsKey(tableName))
            return SystemAdministrator.cacheDataSet.dataSet.get(tableName);
        else {
            SpreadSheet ss = new SpreadSheet(DataBaseAdministrator.getConnection(), DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, tableName, 0, null, null, null, -1, -1, null));
            SystemAdministrator.cacheDataSet.addData(tableName, ss);
            /**
             * secure cheking : thread
             */
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    SecureOperation.checkSecureRead(null, ss);
                }
            });
            return ss;
        }

    }

    /**
     * goal : insert record of the active rubric on database
     *
     * @param rubricName
     * @param spreadSheet
     * @param row
     * @throws PermissionRequiredException
     * @throws SQLException
     * @throws CodeOutOfBoundsException
     * @throws ImpossibleToInsertException
     * @throws CloneNotSupportedException
     */
    public static void insertIntoRubric(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException, SQLException, CodeOutOfBoundsException, ImpossibleToInsertException, CloneNotSupportedException {
        if (spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * secure checking : thread
         */
        SecureOperation.checkSecureInsert(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName), spreadSheet, row);
        /**
         * after secure checking
         * populate system field
         */
        spreadSheet.insertRow(row);
    }

    /**
     * goal : update record of the active rubric on database
     *
     * @param rubricName
     * @param spreadSheet
     * @param row
     * @throws PermissionRequiredException
     * @throws SQLException
     */

    public static void updateOnRubric(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException, SQLException {
        if (spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * secure checking : thread
         */
        SecureOperation.checkSecureUpdate(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName), spreadSheet, row);
        /**
         * update operation
         */
        spreadSheet.updateRow(row);
    }

    /**
     * goal : delete record of the active rubric on database
     *
     * @param rubricName
     * @param spreadSheet
     * @param row
     * @throws PermissionRequiredException
     * @throws SQLException
     */
    public static void deleteFromRubrique(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException, SQLException {

        if (spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * secure checking
         */
        SecureOperation.checkSecureDelete(SystemAdministrator.nameRubricNameTableMap.getValueByKey(rubricName), spreadSheet, row);
        /**
         * perform operation on database
         */
        spreadSheet.deleteRow(row);
    }

    public static String getUserCode() {
        return SystemAdministrator.userCode;
    }

    /**
     * goal : apply some operation on system
     *
     * @param rubricName
     * @param operationName
     */
    public static void applyOperation(String rubricName, String operationName) {

    }

    private static class CacheDataSet {
        private LinkedHashMap<String, SpreadSheet> dataSet = new LinkedHashMap<>();

        private SpreadSheet loadRubric(String rubricName) {
            if (rubricName == null) throw new IllegalArgumentException();
            return this.dataSet.get(rubricName);
        }

        private LinkedHashMap<String, SpreadSheet> getDataSet() {
            return this.dataSet;
        }

        private void addData(String tableName, SpreadSheet ss) {
            if (!this.dataSet.containsKey(tableName))
                this.dataSet.put(tableName, ss);
        }
    }

    /**
     * Function to load user in the system
     *
     * @param data
     * @throws IncorrectDataException
     * @throws SQLException
     */
    public static void loadUser(LinkedHashMap<String, StringProperty> data) throws IncorrectDataException, SQLException, IOException, ClassNotFoundException {
        if (data == null) throw new IllegalArgumentException("data is null");
        /**
         * star connection to database
         */
        DataBaseAdministrator.loadConnection();
        /**
         * retrieve user parameters
         */
        String nom_utilisateur = data.get("nom_utilisateur").get();
        String password = data.get("password").get();

        /**
         * check integrity of field
         */
        if (!Validator.getPattern(Validator.alphaNumeric).matcher(nom_utilisateur).matches())
            throw new IncorrectDataException("parameters are incorrect");

        /**
         * apply secure decypher to check parameter
         */
        Rule rule1 = new Rule();
        rule1.setOperator(Rule.EQUAL);
        rule1.setValue(nom_utilisateur);
        /**
         * querying database
         */
        List<LinkedHashMap<String, Rule>> columnsRules = new LinkedList<>();
        LinkedHashMap<String, Rule> rules = new LinkedHashMap<>();
        rules.put("nom_utilisateur", rule1);
        columnsRules.add(rules);
        ResultSet resultSet = DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, "utilisateur", 0, null, null, null, -1, -1, columnsRules);
        if (resultSet == null) throw new IllegalArgumentException();
        resultSet.last();
        /**
         * check password
         */
        if (resultSet.getRow() == 0 || !Encryptor.passwordDecryption(resultSet.getString("password")).equals(password))
            throw new IncorrectDataException("parameters are incorrect");
        SystemAdministrator.userCode = resultSet.getString("code");
        /**
         * load rights
         */
        SystemAdministrator.rights = SecureOperation.loadRights(SystemAdministrator.userCode);

    }

    public static MenuTree getMenuTree() {
        return SystemAdministrator.menuTree;
    }

    private static void loadRelation() throws SQLException {
        ResultSet resultSet = DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema, "relations", 0, null, null, null, -1, -1, null);
        /**
         * loop over resultSet and store data in convenient data structure
         */
        Relation relation;
        LinkedHashMap<String, Relation> r;
        while (resultSet.next()) {
            relation = new Relation();
            relation.setTableDestination(resultSet.getString("table_destination"));
            relation.setTableSource(resultSet.getString("table_source"));
            relation.setFieldDestination(resultSet.getString("champ_destination"));
            relation.setFieldSource(resultSet.getString("champ_source"));
            relation.setLibelle(resultSet.getString("libelle"));
            if (!SystemAdministrator.relations.containsKey(relation.getTableSource())) {
                r = new LinkedHashMap<>();
                r.put(relation.getTableDestination(), relation);
                SystemAdministrator.relations.put(relation.getTableSource(), r);
            } else {
                SystemAdministrator.relations.get(relation.getTableSource()).put(relation.getTableDestination(), relation);
            }
        }
    }

    /**
     * goal : load information about editor of field in the form
     *
     * @param PathToFileEditor
     * @throws WrongContainsOfFile
     */
    private static void loadEditorMetaData(String PathToFileEditor) throws WrongContainsOfFile {
        In in = new In(PathToFileEditor);
        String[] descriptor;
        String[] descriptor1;
        String tempLine;
        EditorMetaData editorMetaData;
        LinkedHashMap<String, EditorMetaData> r;
        while (!in.isEmpty()) {
            tempLine = in.readLine();
            descriptor = tempLine.split(";");
            if (descriptor.length < 3 || descriptor.length > 4)
                throw new WrongContainsOfFile("the content of file " + PathToFileEditor + " is incorrect please correct it. Line : " + tempLine);
            editorMetaData = new EditorMetaData();
            //editorMetaData.setTableName(descriptor[0]);
            editorMetaData.setColumnName(descriptor[1]);
            //editorMetaData.setTargetColumn(descriptor[2]);
            if (descriptor[0].equals("global")) {
                descriptor1 = descriptor[2].split("#");
                editorMetaData.setTargetColumn(descriptor1[0]);
                editorMetaData.setTableName(descriptor1[1]);
                SystemAdministrator.globalEditorMetaData.put(editorMetaData.getColumnName(), editorMetaData);
            } else {
                editorMetaData.setTableName(descriptor[0]);
                editorMetaData.setTargetColumn(descriptor[2]);
                if (SystemAdministrator.localEditorMetaData.containsKey(editorMetaData.getTableName())) {
                    SystemAdministrator.localEditorMetaData.get(editorMetaData.getTableName()).put(editorMetaData.getColumnName(), editorMetaData);
                } else {
                    r = new LinkedHashMap<>();
                    r.put(editorMetaData.getColumnName(), editorMetaData);
                    SystemAdministrator.localEditorMetaData.put(editorMetaData.getTableName(), r);
                }
            }
        }
    }

    /**
     * goal : retrieve data for an editor of a given column and table
     *
     * @param tableSource
     * @param columnSource
     * @param tableDestination
     * @return
     * @throws SQLException
     * @throws NotUniqueTableInQueryException
     * @throws NotYetEditorException
     */
    public static ObservableList<Row> getDataListEditor(String tableSource, String columnSource, String tableDestination) throws SQLException, NotUniqueTableInQueryException, NotYetEditorException {
        if (tableSource == null || tableDestination == null || columnSource == null)
            throw new IllegalArgumentException();
        if (SystemAdministrator.localEditorMetaData.containsKey(tableSource) && SystemAdministrator.localEditorMetaData.get(tableSource).containsKey(columnSource)) {
            return SystemAdministrator.loadRubricNativelyByTableName(tableDestination).getDataForEditor(SystemAdministrator.localEditorMetaData.get(tableSource).get(columnSource).getTargetColumn());
        }
        return SystemAdministrator.getGlobalEditor(columnSource, tableDestination);
    }

    private static ObservableList<Row> getGlobalEditor(String columnSource, String tableDestination) throws NotYetEditorException, SQLException, NotUniqueTableInQueryException {
        if (SystemAdministrator.globalEditorMetaData.containsKey(columnSource)) {
            return SystemAdministrator.loadRubricNativelyByTableName(tableDestination).getDataForEditor(SystemAdministrator.globalEditorMetaData.get(columnSource).getTargetColumn());
        }
        throw new NotYetEditorException("missing editor");
    }

    /**
     * pattern of line in file : tablesource;fieldSource||f1 = f1'; f2 = f2'; ... ; fn = fn'
     */
    private static void loadFieldImplicant() {
        In in = new In(SystemAdministrator.PATHTOFIELDIMPLICANT);
        String tempString;
        String[] descriptor, descriptor1, descriptor2;
        FieldImplicant fi;
        String sourceField;
        String tableName;
        LinkedHashMap<String, FieldImplicant> r;
        /**
         * loop in file : treatment of line
         */
        while (!in.isEmpty()) {
            tempString = in.readLine().trim();
            fi = new FieldImplicant();
            /**
             * split descriptor into two tokens
             * 1 - token which contains tableSource; fieldSource
             * 2 - token which contains the mapping between field of the sourceTable and the targetTable (Source table of foreign key)
             */
            descriptor = tempString.split("#");
            sourceField = descriptor[0].trim().split(";")[1];
            tableName = descriptor[0].trim().split(";")[0];
            fi.setValueColumnSource(sourceField);
            for (String s : descriptor[1].trim().split(";")) {
                descriptor1 = s.split("=");
                fi.addMapping(descriptor1[0].trim(), descriptor1[1].trim());
            }
            if (!SystemAdministrator.fieldImplicant.containsKey(tableName)) {
                r = new LinkedHashMap<>();
                r.put(sourceField, fi);
                SystemAdministrator.fieldImplicant.put(tableName, r);
            } else {
                SystemAdministrator.fieldImplicant.get(tableName).put(sourceField, fi);
            }
        }
    }

    private static void loadListImpliquant() {
        In in = new In(SystemAdministrator.PATHTOLISTIMPLICANT);
        String tableSource;
        String fieldSource;
        String fieldTarget;
        String libelle;
        String tempLine;
        String[] descriptor, descriptor1;
        ListImplicant li;
        List<LinkedHashMap<String, Rule>> linkedHashMapList;
        while (!in.isEmpty()) {
            tempLine = in.readLine();
            li = new ListImplicant();
            li.setLine(tempLine);
            /**
             * split tempLine with "##"
             */
            descriptor = tempLine.trim().split("##");
            /**
             * process on descriptor[0] to retrieve tableSource, fieldSource and fieldTarget
             */
            descriptor1 = descriptor[0].split(";");
            tableSource = descriptor1[0].trim();
            fieldSource = descriptor1[1].trim();
            fieldTarget = descriptor1[2].trim();
            libelle = descriptor1[3].trim();
            /**
             * process on descriptor[1] to retrieve rule.
             */

            descriptor1 = descriptor[1].trim().split("\\$");
            linkedHashMapList = SystemAdministrator.getRuleOfListImplicant(descriptor1);
            /**
             * set attributes of listImpliquant
             */
            li.setLibelle(libelle);
            li.setFieldTarget(fieldTarget);
            li.setRules(linkedHashMapList);

            /**
             * fill attribute listImplicant of class {@link SystemAdministrator}
             */
            if (!SystemAdministrator.listImplicant.containsKey(tableSource)) {
                LinkedHashMap<String, ListImplicant> r = new LinkedHashMap<>(5);
                r.put(fieldSource, li);
                SystemAdministrator.listImplicant.put(tableSource, r);
            } else {
                SystemAdministrator.listImplicant.get(tableSource).put(fieldSource, li);
            }
        }
    }

    public static List<LinkedHashMap<String, Rule>> getRuleOfListImplicant(String[] descriptor1) {
        List<LinkedHashMap<String, Rule>> linkedHashMapList = new LinkedList<>();
        LinkedHashMap<String, Rule> linkedHashMap;
        String[] descriptor2;
        String[] descriptor;
        Rule rule;
        for (String token : descriptor1) {
            linkedHashMap = new LinkedHashMap<>();
            /**
             * process on each token to retrieve each rule of clause
             */
            /**
             * each token in descriptor1 is a clause in a clausal form of boolean expression
             */
            descriptor2 = token.trim().split("#");
            for (String tokenRule : descriptor2) {
                rule = new Rule();
                descriptor = tokenRule.trim().split(";");
                rule.setValue(descriptor[2].trim());
                rule.setOperator(descriptor[1].trim());
                linkedHashMap.put(descriptor[0].trim(), rule);
            }
            /**
             * add clause in set of clause : add hashmap in linkedHashMap
             */
            linkedHashMapList.add(linkedHashMap);
        }
        return linkedHashMapList;
    }

    public static FieldImplicant getFielImpliquant(String tableSource, String columnSource) {
        if (tableSource == null || columnSource == null) throw new IllegalArgumentException();
        return SystemAdministrator.fieldImplicant.get(tableSource).get(columnSource);
    }

    public static ListImplicant getListImplicant(String tableSource, String columnSource) {
        if (tableSource == null || columnSource == null) throw new IllegalArgumentException();
        return SystemAdministrator.listImplicant.get(tableSource).get(columnSource);
    }

    public static boolean containsFieldImplicant(String tableSource, String columnSource) {
        if (!SystemAdministrator.fieldImplicant.containsKey(tableSource)) return false;
        if (!SystemAdministrator.fieldImplicant.get(tableSource).containsKey(columnSource)) return false;
        return true;
    }

    public static boolean containsListImplicant(String tableSource, String columnSource) {
        if (!SystemAdministrator.listImplicant.containsKey(tableSource)) return false;
        if (!SystemAdministrator.listImplicant.get(tableSource).containsKey(columnSource)) return false;
        return true;
    }

    public static boolean haveRight(String rubric, String operation) {
        if (rubric == null || operation == null) throw new IllegalArgumentException();
        /**
         * ultimate checking of right
         * check if the usercode == 1 then the user have all right on system
         */
        if (SystemAdministrator.userCode.equals("1")) return true;
        /**
         * alternative checking of right
         */
        return SystemAdministrator.rights.contains(rubric + "-" + operation);
    }

    public static void logOutUser() {
        /**
         * clear all static fields of class
         */
        // user
        SystemAdministrator.userCode = null;
        // clear birectionalHashMap
        SystemAdministrator.nameRubricNameTableMap.clear();
        // dataSet
        SystemAdministrator.systemDataset.clear();
        SystemAdministrator.cacheDataSet.dataSet.clear();
        // menutree
        SystemAdministrator.menuTree.clear();
        // editors
        SystemAdministrator.listImplicant.clear();
        SystemAdministrator.fieldImplicant.clear();
        //rights
        SystemAdministrator.rights.clear();
        //relations
        SystemAdministrator.relations.clear();
        // implicantMetaData
        SystemAdministrator.localEditorMetaData.clear();
        SystemAdministrator.globalEditorMetaData.clear();
    }
}


