package convolution;

import java.util.ArrayList;


public class Kernels {
    public final static int[][] uniform = new int[][] {
        {1, 1, 1},
        {1, 1, 1},
        {1, 1, 1}};
    
    public final static int[][] identity = new int[][] {
        {0, 0, 0}, 
        {0, 1, 0}, 
        {0, 0, 0}};
    
    public final static int[][] edgeDetection0 = new int[][] {
        {-1, 0, 1}, 
        {0, 0, 0}, 
        {1, 0, -1}};
    
    public final static int[][] edgeDetection1 = new int[][] {
        {0, -1, 0}, 
        {-1, 4, -1}, 
        {0, -1, 0}};
    
    public final static int[][] edgeDetection2 = new int[][] {
        {-1, -1, -1}, 
        {-1, 8, -1}, 
        {-1, -1, -1}};
    
    public final static int[][] gX = new int[][] {
        {-1, 0, 1}, 
        {-2, 0, 2}, 
        {-1, 0, 1}};
    
    public final static int[][] gY = new int[][] {
        {1, 2, 1}, 
        {0, 0, 0}, 
        {-1, -2, -1}};
    
    public static int[][] gaussian = new int[][] {
        {2, 4, 5, 4, 2},
        {4, 9 ,12, 9, 4},
        {5, 12, 15, 12, 5},
        {4, 9, 12, 9, 4},
        {2, 4, 5, 4, 2},
    };
    
    public static int[][] gXBis = new int[][] {
        {9, 9, 9, 9, 9},
        {9, 5 , 5, 5, 9},
        {-7, -3, 0, -3, -7},
        {-7, -3, -3, -3, -7},
        {-7, -7, -7, -7, -7},
    };
    
    public static int[][] gYBis = new int[][] {
        {9, 9, -7, -7, -7},
        {9, 5, -3, -3, -7},
        {9, 5, 0, -3, -7},
        {9, 5, -3, -3, -7},
        {9, 9, -7, -7, -7},
    };
    
    /*
     * Gaussian Blur Smooth Kernel
     */
    public static int[][] getGBS(int k, int l) {
        int[][] kernel = new int[k][l];
        int mid = Math.max(k / 2, l / 2);
        for(int r = 0; r < k; r++ ) {
            for(int c = 0; c < l; c++ ) {
                kernel[r][c] = mid - Math.max(Math.abs(k/2-r), Math.abs(l/2-c)) + 1;
            }
        }
        return kernel;
    }
    
    /*
     * Gaussian Blur Exponential Kernel
     */
    public static int[][] getGBE(int k, int l) {
        int[][] kernel = new int[k][l];
        int mid = Math.max(k / 2, l / 2);
        for(int r = 0; r < k; r++ ) {
            for(int c = 0; c < l; c++ ) {
                kernel[r][c] = (int) (Math.pow(2, 2 * mid) / Math.pow(2, Math.abs(k/2-r) + Math.abs(l/2-c)));
            }
        }
        return kernel;
    }
    
    /*
     * Gaussian approximation by Pascal Triangle
     */
    public static int[][] getBinomial(int k, int l) {
        int[][] kernel = new int[k][l];
        ArrayList<Integer> a = nthRow(k-1);
        ArrayList<Integer> b = nthRow(l-1);
        for(int r = 0; r < k; r++ ) {
            for(int c = 0; c < l; c++ ) {
                kernel[r][c] = a.get(r) * b.get(c);
            }
        }
        return kernel;
    }
    
    /*
     * Pyramid
     */
    public static int[][] getPyramid(int k, int l) {
        int[][] kernel = new int[k][l];
        int mid = Math.max(k / 2, l / 2);
        for(int r = 0; r < k; r++ ) {
            for(int c = 0; c < l; c++ ) {
                kernel[r][c] = Math.max(0 , 1 + mid - (Math.abs(k/2-r) + Math.abs(l/2-c)));
            }
        }
        return kernel;
    }
    
    /*
     * Cross Kernel
     */
    public static int[][] getCGBS(int k, int l) {
        int[][] kernel = getPyramid(k, l);
        for(int r = 0; r < k; r++ ) {
            for(int c = 0; c < l; c++ ) {
                kernel[r][c] = (r == k / 2 || c == l / 2) ? (int) Math.pow(2, kernel[r][c] - 1) : 0;
            }
        }
        return kernel;
    }
    
    private static ArrayList<Integer> nthRow(int N) {
        ArrayList<Integer> res = new ArrayList<>();
        // Adding 1 as first element of every row is 1
        res.add(1);
        for (int i = 1; i <= N; i++) {
            res.add(i, (res.get(i-1) * (N-i+1)) / i);
        }
        return res;
    }
    
    public static int[][] getKernel(String name) {
        String[] split = name.split("_");
        int r = Integer.parseInt(split[1]);
        int c = split.length == 3 ? Integer.parseInt(split[2]) : r;
        switch (split[0]) {
            case "GBS":
                return getGBS(r, c);
            case "GBE":
                return getGBE(r, c);
            case "PY":
                return getPyramid(r, c);
            case "CGBS":
                return getCGBS(r, c);
            default:
                return gaussian;
        }
    }
}


