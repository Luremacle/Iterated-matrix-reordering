package orderings;

import representation.HeatMap;

public class BinarySmooth extends MatrixOrdering {
    private final int STOP_THRESHOLD = 2;
    public int[][] template;
    
    public BinarySmooth(HeatMap hm, int[][] template) {
        super(hm);
        this.template = template;
    }

    @Override
    public void solve() {
        int count = 0;
        int no_progress = 0;
        double obj;
        System.out.println("Smoothing:");
        while(no_progress < STOP_THRESHOLD && count < 30) {
            obj = count % 2 == 0 ? solve('r') : solve('c');
            no_progress = obj > -1.0 ? no_progress + 1 : 0;
            if(symmetry) {
                count += 2;
            } else {
                count++;
            }
            System.out.println(count + " : obj -> " + obj);
        }
    }
    
    public double solve(char param) {
        int[] order;
        if(param == 'r' || symmetry) {
            order = hm.row_order;
        } else {
            order = hm.col_order;
        }
        double objective = 0.0;
        for(int i = 0; i < order.length; i++) {
            for(int j = i + 1; j < order.length; j++) {
                double delta = anticipate(i, j, param);
                if(delta < 0.0) {
                    objective += delta;
                    permute(order, i, j);
                    if(symmetry) {
                        permute(hm.col_order, i, j);
                    }
                }
            }
        }
        return objective;
    }
    
    private void permute(int[] v, int a, int b) {
        int temp = v[a];
        v[a] = v[b];
        v[b] = temp;
    }

    private double anticipate(int ip, int jp, char param) {
        int l; int[] order;
        double s0 = 0.0;
        double s1 = 0.0;
        if(param == 'r') {
            l = hm.col_order.length; 
            order = hm.row_order;
            for(int i = 0; i < l; i++) {
                s0 += (template[ip][i] == hm.get(ip, i) ? 0 : 1) + (template[jp][i] == hm.get(jp, i) ? 0 : 1);
                s1 += (template[ip][i] == hm.get(jp, i) ? 0 : 1) + (template[jp][i] == hm.get(ip, i) ? 0 : 1);
            }
        }
        else {
            l = hm.row_order.length; 
            order = hm.col_order;
            for(int i = 0; i < l; i++) {
                s0 += (template[i][ip] == hm.get(i, ip) ? 0 : 1) + (template[i][jp] == hm.get(i, jp) ? 0 : 1);
                s1 += (template[i][ip] == hm.get(i, jp) ? 0 : 1) + (template[i][jp] == hm.get(i, ip) ? 0 : 1);
            }
        }
        return s1 - s0;
    }
}
