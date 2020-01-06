package ScienceComputation;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tools {

    /**
     * goal : convert ResultSet to INDArray for preparing statistical computation
     * @param resultSet
     * @param listColumnName
     * @return
     */
    public static INDArray ResultSetToINDArray(ResultSet resultSet, String[] listColumnName) throws SQLException {
        if (resultSet == null || listColumnName == null) throw new IllegalArgumentException();
        /**
         * retrieve number of Row and column
         */
        int numCol = listColumnName.length;
        resultSet.last();
        int numRow = resultSet.getRow();
        resultSet.beforeFirst();
        /**
         * create
         */
        INDArray result = Nd4j.create(numRow, numCol);
        int counterRow = 0;
        double currentValue = 0.0;
        while (resultSet.next()) {
            for (int counterCol = 0; counterCol < listColumnName.length; counterCol ++) {
                currentValue = resultSet.getDouble(counterCol);
                result.putScalar(new int[]{counterRow, counterCol} , currentValue);
            }
            counterRow++;
        }
        return result;
    }
}
