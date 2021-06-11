package orderings;

import representation.HeatMap;

public class ContinuousSmooth extends MatrixOrdering {
    private final int STOP_THRESHOLD = 2;
    public double[][] blurred;
    
    public ContinuousSmooth(HeatMap hm, double[][] blurred) {
        super(hm);
        this.blurred = blurred;
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
                s0 += Math.abs(blurred[ip][i] - hm.getDouble(ip, i)) + Math.abs(blurred[jp][i] - hm.getDouble(jp, i));
                s1 += Math.abs(blurred[ip][i] - hm.getDouble(jp, i)) + Math.abs(blurred[jp][i] - hm.getDouble(ip, i));
            }
        }
        else {
            l = hm.row_order.length; 
            order = hm.col_order;
            for(int i = 0; i < l; i++) {
                s0 += Math.abs(blurred[i][ip] - hm.getDouble(i, ip)) + Math.abs(blurred[i][jp] - hm.getDouble(i, jp));
                s1 += Math.abs(blurred[i][ip] - hm.getDouble(i, jp)) + Math.abs(blurred[i][jp] - hm.getDouble(i, ip));
            }
        }
        return s1 - s0;
    }
}
