package orderings;
import representation.HeatMap;

public class ROrderingNetworks extends MatrixOrdering {
    public String method;

    public ROrderingNetworks(HeatMap hm, String method) {
        super(hm);
        this.method = method;
    }

    @Override
    public void solve() {
        int[] old_r_order = hm.row_order.clone();
        int[] new_r_order = SeriationHelper.callRScript(hm.toArrayDouble(), method, false);
        for(int i = 0; i < new_r_order.length; i++) {
            hm.row_order[i] = old_r_order[new_r_order[i]];
        }
        // Symmetrical Matrix / networks : take only one side order and copy it
        hm.col_order = hm.row_order.clone();
    }
}
