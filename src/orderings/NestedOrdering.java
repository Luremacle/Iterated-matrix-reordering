package orderings;

import representation.HeatMap;
import java.util.Arrays;

public class NestedOrdering extends MatrixOrdering{
    
    private class Pair implements Comparable<Pair>{
        public int id; public int count;
        public Pair(int id, int count) {
            this.id = id;
            this.count = count;
        }
        @Override
        public int compareTo(Pair o) {
            return o.count - this.count;
        }
    }
    
    public NestedOrdering(HeatMap hm) {
        super(hm);
    }
    
    @Override
    public void solve() {
        Pair[] row = new Pair[hm.row_order.length];
        Pair[] col = new Pair[hm.col_order.length];
        for(int i = 0; i < row.length; i++) {
            int index = hm.row_order[i];
            row[i] = new Pair(index, count(hm.getRow(index)));
        }
        for(int i = 0; i < col.length; i++) {
            int index = hm.col_order[i];
            col[i] = new Pair(index, count(hm.getCol(index)));
        }
        Arrays.sort(row);
        Arrays.sort(col);
        hm.row_order = extract(row);
        hm.col_order = extract(col);
    }
    
    private int count(int[] vector) {
        int count = 0;
        for(int i = 0; i < vector.length; i++) {
            count += vector[i];
        }
        return count;
    }
    
    private int[] extract(Pair[] vector) {
        int[] v = new int[vector.length];
        for(int i = 0; i < vector.length; i++) {
            v[i] = vector[i].id;
        }
        return v;
    }
}
