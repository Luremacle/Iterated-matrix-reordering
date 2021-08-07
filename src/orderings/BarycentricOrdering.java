package orderings;
import java.util.Arrays;
import representation.HeatMap;


public class BarycentricOrdering extends MatrixOrdering {
    public int LIMIT;
    
    public BarycentricOrdering(HeatMap hm, int limit) {
        super(hm);
        this.LIMIT = limit;
    }
    
    @Override
    public void solve() {
        int iter = 0;
        while(iter < LIMIT) {
            if(iter % 2 == 0 || symmetry) {
                Pair[] row = new Pair[hm.row_order.length];
                int[] order = HELPER(hm.col_order);
                for(int i = 0; i < row.length; i++) {
                    int[] r = hm.getRow(i);
                    int count = count(r);
                    int bary = bary(r, order);
                    double score = count == 0 ? 0.0 : 1.0 * bary / count;
                    row[i] = new Pair(hm.row_order[i], score);
                }  
                Arrays.sort(row);
                hm.row_order = extract(row);
            } else {
                Pair[] col = new Pair[hm.col_order.length];
                int[] order = HELPER(hm.row_order);
                for(int i = 0; i < col.length; i++) {
                    int[] c = hm.getCol(i);
                    int count = count(c);
                    int bary = bary(c, order);
                    double score = count == 0 ? 0.0 : 1.0 * bary / count;
                    col[i] = new Pair(hm.col_order[i], score);
                }  
                Arrays.sort(col);
                hm.col_order = extract(col);
            }
            if(symmetry) {
                hm.col_order = hm.row_order.clone();
                iter += 2;
            } else {
                iter++;
            }
        }
    }
    
    private int count(int[] vector) {
        int count = 0;
        for(int i = 0; i < vector.length; i++) {
            count += vector[i];
        }
        return count;
    }
    
    private int bary(int[] vector, int[] order) {
        int count = 0;
        for(int i = 0; i < vector.length; i++) {
            count += vector[i] * order[i];
        }
        return count;
    }
    
    public int[] HELPER(int[] array) {
        int[] a = new int[array.length];
        for(int i = 0; i < array.length; i++) {
            a[array[i]] = i;
        }
        return a;
    }
    
    private int[] extract(Pair[] vector) {
        int[] v = new int[vector.length];
        for(int i = 0; i < vector.length; i++) {
            v[i] = vector[i].id;
        }
        return v;
    }
    
    private class Pair implements Comparable<Pair>{
        public int id; public double barycenter;
        public Pair(int id, double barycenter) {
            this.id = id;
            this.barycenter = barycenter;
        }
        @Override
        public int compareTo(Pair o) {
            if(this.barycenter < o.barycenter)
                return -1;
            else if(o.barycenter < this.barycenter)
                return 1;
            return 0;
        }
    }
    
}
