package finances;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.reduce3.Dot;
import org.nd4j.linalg.factory.Nd4j;

/**
 * goal : this class hold a set of common function use in financial computation
 */
public class Finance {
    /**
     * formula : THT = (quantity.T * cost)
     * @param quantity
     * @param cost
     * @return
     */
    public static double THT(INDArray quantity, INDArray cost) {
        if (quantity == null || cost == null) throw new IllegalArgumentException();
        /**
         * return dot product of quantity and cost
         */
        return quantity.mul(cost).sum().getDouble();
    }

    /**
     *  formula : TWR =  ((1 - reduction/100) * quantity).T*cost
     * @param quantity
     * @param cost
     * @param reduction
     * @return total cost with reduction
     */
    public static double TWR(INDArray quantity, INDArray cost, INDArray reduction) {
        if (quantity == null || cost == null || reduction == null) throw new IllegalArgumentException();
        reduction = reduction.neg().add(1);
        INDArray result = reduction.mul(quantity);
        result = result.mul(cost).sum();
        return result.getDouble();
    }

    /**
     * formula : THT = (quantity.T * cost) + (1 + TVA/100)
     * @param quantity
     * @param cost
     * @param TVA
     * @return
     */
    public static double TTC(INDArray quantity, INDArray cost, double TVA) {
        if (quantity == null || cost == null) throw new IllegalArgumentException();
        double tht = Finance.THT(quantity, cost);
        return tht * (1 + TVA/100);
    }

    public static void main(String[] args) {
        INDArray quantity = Nd4j.create((new int[]{5, 1}), new double[]{1, 1, 1, 1, 1});
        INDArray cost = Nd4j.create((new int[]{5, 1}), new double[]{4564.64, 14654.5, 4984.5, 646465.15, 498.5});
        double THT = Finance.TTC(quantity, cost, 19.5);
        System.out.println(Finance.THT(quantity, cost));
        System.out.println(THT);
    }
}
