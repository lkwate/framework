package security;

import dataBaseManager.DataBaseAdministrator;
import dataDefinition.Row;
import dataDefinition.Rule;
import dataDefinition.SpreadSheet;
import exceptions.PermissionRequiredException;
import system.SystemAdministrator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SecureOperation {
    /**
     * key word and
     * system field
     */
    private static final String date_activation = "date_activation";
    private static final String password = "password";
    private static final String user_createur = "user_createur";
    private static final String user_modificateur = "user_modificateur";
    private static final String date_creation = "date_creation";
    private static final String date_modification = "date_modification";
    private static final String heure_creation = "heure_creation";
    private static final String heure_modification = "heure_modification";

    /**
     * tools
     */
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * @param rubricName
     * @param spreadSheet
     * @param row
     */
    public static void checkSecureInsert(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException {
        if (rubricName == null || spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * check rights
         */
        if (!SystemAdministrator.haveRight(rubricName, rubricName+"-"+Droit.CREATE))
            throw new PermissionRequiredException("user doesn't have permission to create item on this rubric");
        /**
         * encrypt necessary field
         */
        if (spreadSheet.getColumnMetaData().containsKey("password")) {
            int index = spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.password);
            row.setValue(index, Encryptor.passwordEncryption(row.getStringValue(index)));
        }

        /**
         * populate system field
         */
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.user_createur), SystemAdministrator.getUserCode());
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.date_creation), LocalDate.now().format(SecureOperation.dateFormatter));
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.user_modificateur), SystemAdministrator.getUserCode());
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.date_modification), LocalDate.now().format(SecureOperation.dateFormatter));
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.heure_creation), LocalDateTime.now().format(SecureOperation.timeFormatter));
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.heure_modification), LocalDateTime.now().format(SecureOperation.timeFormatter));
    }

    /**
     *
     * @param rubricName
     * @param spreadSheet
     * @param row
     */
    public static void checkSecureUpdate(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException {
        if (spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * check rights
         */
        if (!SystemAdministrator.haveRight(rubricName, rubricName+"-"+Droit.UPDATE))
            throw new PermissionRequiredException("user doesn't have permission to update item on this rubric");

        /**
         * secure check
         */
        if (spreadSheet.getColumnMetaData().containsKey("password")) {
            int index = spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.password);
            row.setValue(index, Encryptor.passwordEncryption(row.getStringValue(index)));
        }
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.user_modificateur), SystemAdministrator.getUserCode());
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.date_modification), LocalDate.now().format(SecureOperation.dateFormatter));
        row.setValue(spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.heure_modification), LocalDateTime.now().format(SecureOperation.timeFormatter));
    }

    /**
     * @param rubricName
     * @param spreadSheet
     * @param row
     */
    public static void checkSecureDelete(String rubricName, SpreadSheet spreadSheet, Row row) throws PermissionRequiredException {
        if (rubricName == null || spreadSheet == null || row == null) throw new IllegalArgumentException();
        /**
         * check rights
         */
        if (!SystemAdministrator.haveRight(rubricName, rubricName+"-"+Droit.DELETE))
            throw new PermissionRequiredException("user doesn't have permission to delete item on this rubric");
    }

    /**
     *
     * @param rubricName
     * @param spreadSheet
     */
    public static void checkSecureRead(String rubricName, SpreadSheet spreadSheet) {
        if (spreadSheet == null) throw new IllegalArgumentException();
        if (spreadSheet.getColumnMetaData().containsKey(SecureOperation.password)) {
            /**
             * decryption of password
             */
            int index = spreadSheet.getIndexColumnName().getValueByKey(SecureOperation.password);
            for (Row row : spreadSheet.getRows()) {
                row.setValue(index, Encryptor.passwordDecryption(row.getStringValue(index)));
            }
        }
    }

    /**
     *  goal : verify whether the user have the right to read data on rubric
     * @param rubricName
     */
    public static void checkSecureRightRead(String rubricName) throws PermissionRequiredException {
        if (rubricName == null) throw new IllegalArgumentException();
        /**
         * check right
         */
        if (!SystemAdministrator.haveRight(rubricName,rubricName+"-"+Droit.READ))
            throw new PermissionRequiredException("user doesn't have permission to read this rubric");
    }

    /**
     * goal : load data about right of user
     */
    public static Set<String> loadRights(String userCode) throws SQLException {
        Set<String> groupRight = new HashSet<>();
        Set<String> groups = new HashSet<>();
        ResultSet resultSet;
        List<LinkedHashMap<String, Rule>> rules = new LinkedList<>();
        List<String> columns;

        if (userCode == null) {
            throw new IllegalArgumentException("user is not load in system");
        } else {
            /**
             * building of Rules
             */
            LinkedHashMap<String, Rule> r = new LinkedHashMap<>(1);
            Rule rule = new Rule();
            rule.setOperator(Rule.EQUAL);
            rule.setValue(userCode);
            r.put("code_utilisateur", rule);
            rules.add(r);
            /**
             * columns
             */
            columns = new LinkedList<>();
            columns.add("code_groupe");
            /**
             * retrieve code's group
             */
            resultSet = DataBaseAdministrator.getQueryManager().readOnTable(DataBaseAdministrator.getConnection(), DataBaseAdministrator.catalog, DataBaseAdministrator.schema,"usergroupe", 0, null, null, columns, -1, -1, rules);
            while (resultSet.next()) {
                groups.add(resultSet.getString("code_groupe"));
            }
            /**
             * retrieve right of each group
             */
            /**
             * necessary field:
             */
            String query = "select r.nom_rubrique, r.tableName, op.libelle from droit as d, rubrique as r, operation as op where d.code_groupe = ? and d.code_operation = op.code and op.code_rubrique = r.code";
            PreparedStatement preparedStatement = DataBaseAdministrator.getConnection().prepareStatement(query);
            for (String group : groups) {
                preparedStatement.setString(1, group);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    groupRight.add(resultSet.getString("tableName")+"-"+resultSet.getString("libelle"));
                }
            }
        }
        return groupRight;
    }
}
