package orderings;

import representation.HeatMap;

public abstract class MatrixOrdering {
    protected HeatMap hm;
    public boolean symmetry = false;
    
    /*
    * Create a matrix ordering instance.
    * A new associated HeatMap is built.
    */
    public MatrixOrdering(HeatMap hm) {
        this.hm = new HeatMap(hm.rep);
        this.hm.col_order = hm.col_order.clone();
        this.hm.row_order = hm.row_order.clone();
    }
    
    /*
    * Method used to permute the rows and columns of the HeatMap
    */
    public abstract void solve();
    
    public HeatMap getHeatMap() {
        return this.hm;
    }
    
}
