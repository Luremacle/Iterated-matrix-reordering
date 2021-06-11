package orderings;
import representation.HeatMap;

public class ROrderingTable extends MatrixOrdering {
    public String method;

    public ROrderingTable(HeatMap hm, String method) {
        super(hm);
        this.method = method;
    }

    @Override
    public void solve() {
        int[] new_r_order, new_c_order;
        if(!hm.rep.discrete) {
            double[][] row_distances = utils.Utils.getDistancesContinuous(hm, 'r');
            double[][] col_distances = utils.Utils.getDistancesContinuous(hm, 'c');
            new_r_order = SeriationHelper.callRScript(row_distances, method, true);
            new_c_order = SeriationHelper.callRScript(col_distances, method, true);
        }       
        else {
            int[][] row_distances = utils.Utils.getDistances(hm, 'r');
            int[][] col_distances = utils.Utils.getDistances(hm, 'c');
            // Seriation R library needs numeric value for some methods (OLO, ...)
            new_r_order = SeriationHelper.callRScript(convert(row_distances), method, true);
            new_c_order = SeriationHelper.callRScript(convert(col_distances), method, true);
        }
        int[] old_r_order = hm.row_order.clone();
        int[] old_c_order = hm.col_order.clone();
        for(int i = 0; i < new_c_order.length; i++) {
            hm.col_order[i] = old_c_order[new_c_order[i]];
        }
        for(int i = 0; i < new_r_order.length; i++) {
            hm.row_order[i] = old_r_order[new_r_order[i]];
        }
    }
    
    private double[][] convert(int[][] a) {
        double[][] convert = new double[a.length][a[0].length];
        for(int i = 0; i < a.length;i++) {
            for(int j = 0; j < a[0].length;j++) {
                convert[i][j] = a[i][j] * 1.0;
            }
        }
        return convert;
    }
}
