package representation;

import utils.Utils;

public class HeatMap {
    
    public Representation rep;
    public int[] col_order;
    public int[] row_order;
    public double score; // temporary variable to store last score calculated
    
    public HeatMap(Representation m) {
        this.rep = m;
        this.col_order = new int[m.width];
        this.row_order = new int[m.height];
        for(int i = 0; i < m.height; i++)
            row_order[i] = i;
        for(int i = 0; i < m.width; i++)
            col_order[i] = i;     
    }
    
    public int get(int row, int col) {
        return rep.get(row_order[row], col_order[col]);
    }
    
    public double getDouble(int row, int col) {
        return rep.getDouble(row_order[row], col_order[col]);
    }
    
    public int[] getRow(int row) {
        int[] r = new int[col_order.length];
        for(int i = 0; i < r.length; i++) {
            r[i] = rep.get(row, i);
        }
        return r;
    }
    
    public double[] getRowDouble(int row) {
        double[] r = new double[col_order.length];
        for(int i = 0; i < r.length; i++) {
            r[i] = rep.getDouble(row, i);
        }
        return r;
    }
   
    public int[] getCol(int col) {
        int[] c = new int[row_order.length];
        for(int i = 0; i < c.length; i++) {
            c[i] = rep.get(i, col);
        }
        return c;
    }
    
    public double[] getColDouble(int col) {
        double[] c = new double[row_order.length];
        for(int i = 0; i < c.length; i++) {
            c[i] = rep.getDouble(i, col);
        }
        return c;
    }
    
    public void shuffle() {
        Utils.shuffle(col_order);
        Utils.shuffle(row_order);
    }
    
    public int[][] toArrayInt() {
        int[][] a = new int[rep.height][rep.width];
        for(int i = 0; i < rep.height; i++) {
            for(int j = 0; j < rep.width; j++) {
                a[i][j] = get(i,j);
            }
        }
        return a;
    }
    
    public double[][] toArrayDouble() {
        double[][] a = new double[rep.height][rep.width];
        for(int i = 0; i < rep.height; i++) {
            for(int j = 0; j < rep.width; j++) {
                a[i][j] = getDouble(i,j);
            }
        }
        return a;
    }
    
    public int[][] toArray() {
        return toArrayInt();
    }
    
    public HeatMap duplicate() {
        HeatMap clone = new HeatMap(rep);
        clone.col_order = col_order.clone();
        clone.row_order = row_order.clone();
        return clone;
    }
    
    public int[] getRowLabels() {
        int[] res = new int[row_order.length];
        for(int r = 0; r < row_order.length; r++) {
            res[r] = rep.rowLabels[row_order[r]];
        }
        return res;
    }
    
    public int[] getColLabels() {
        int[] res = new int[col_order.length];
        for(int r = 0; r < col_order.length; r++) {
            res[r] = rep.colLabels[col_order[r]];
        }
        return res;
    }
}
