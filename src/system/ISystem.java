package system;

import dataDefinition.Row;
import dataDefinition.SpreadSheet;

public interface ISystem {

    /**
     * goal : load all necessary data for operation on system
     * @throws Throwable
     */
    public void loadSystem() throws Throwable;

    /**
     * goal : load data from a given rubric
     * @param rubricName
     * @return
     * @throws Throwable
     */
    public SpreadSheet loadRubric(String rubricName) throws Throwable;

    /**
     * goal : insert record of the active rubric on database
     * @param row
     * @throws Throwable
     */
    public void insertIntoRubric(Row row) throws Throwable;

    /**
     * goal : update record of the active rubric on database
     * @param row
     * @throws Throwable
     */
    public void updateOnRubric(Row row) throws Throwable;

    /**
     * goal : delete record of the active rubric on database
     * @param row
     * @throws Throwable
     */
    public void deleteFromRubrique(Row row) throws Throwable;

    /**
     * goal : apply some operation on system
     * @param operationName
     * @throws Throwable
     */
    public void applyOperation(String operationName) throws Throwable;
}
