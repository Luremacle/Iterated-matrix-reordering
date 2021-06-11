package utils;

import representation.HeatMap;
import java.util.Random;

public class Utils {
    
    public static int[][] getDistances(HeatMap hm, char option) {
        int size = option == 'r' ? hm.rep.height : hm.rep.width;
        int[][] distances = new int[size][size];
        for(int i = 0; i < distances.length; i++) {
            for(int j = 0; j < i; j++) {
                if(option == 'c') {
                    int ip = hm.col_order[i]; int jp = hm.col_order[j];
                    distances[i][j] = hamming(hm.getCol(ip), hm.getCol(jp));
                }
                if(option == 'r') {
                     int ip = hm.row_order[i]; int jp = hm.row_order[j];
                     distances[i][j] = hamming(hm.getRow(ip), hm.getRow(jp));
                }   
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }
    
    public static double[][] getDistancesContinuous(HeatMap hm, char option) {
        int size = option == 'r' ? hm.rep.height : hm.rep.width;
        double[][] distances = new double[size][size];
        for(int i = 0; i < distances.length; i++) {
            for(int j = 0; j < i; j++) {
                if(option == 'c') {
                    int ip = hm.col_order[i]; int jp = hm.col_order[j];
                    distances[i][j] = euclidean(hm.getColDouble(ip), hm.getColDouble(jp));
                }
                if(option == 'r') {
                    int ip = hm.row_order[i]; int jp = hm.row_order[j];
                    distances[i][j] = euclidean(hm.getRowDouble(ip), hm.getRowDouble(jp));
                }   
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }
    
    public static int hamming(int[] city0, int[] city1) {
        int count = 0;
        for(int i = 0; i < city0.length; i++) {
            count = city0[i] == city1[i] ? count : count+1;
        }
        return count;
    }
    
    public static double euclidean(double[] city0, double[] city1) {
        boolean euclidean = true; // Manhattan distance otherwise
        double count = 0.0;
        for(int i = 0; i < city0.length; i++) {
            if(euclidean)
                count += (city0[i]  - city1[i]) * (city0[i]  - city1[i]);
            else 
                count += Math.abs(city0[i]  - city1[i]);
        }
        return euclidean ? Math.sqrt(count) : count;
    }
    
    
    public static void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
    
    public static double otsu(double[][] matrix) {
        int K_PARTITION = 255;
        int[] histData = new int[K_PARTITION+1];
        int total = matrix.length * matrix[0].length;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                int h = (int) (matrix[i][j] * K_PARTITION);
                histData[h]++;
            }
        }

        float sum = 0;
        for (int t=0 ; t <= K_PARTITION ; t++) sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t=0 ; t <= K_PARTITION ; t++) {
           wB += histData[t];               // Weight Background
           if (wB == 0) continue;

           wF = total - wB;                 // Weight Foreground
           if (wF == 0) break;

           sumB += (float) (t * histData[t]);

           float mB = sumB / wB;            // Mean Background
           float mF = (sum - sumB) / wF;    // Mean Foreground

           // Calculate Between Class Variance
           float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

           // Check if new maximum found
           if (varBetween > varMax) {
              varMax = varBetween;
              threshold = t;
           }
        }
        //System.out.println(1.0 * threshold / K_PARTITION);
        return (1.0 * threshold / K_PARTITION);
    }
    
}
