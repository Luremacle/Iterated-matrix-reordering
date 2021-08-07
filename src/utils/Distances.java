package utils;

import representation.HeatMap;

public class Distances {
    
    public static int[][] getDistances(HeatMap hm, char option) {
        int size = option == 'r' ? hm.rep.height : hm.rep.width;
        int[][] distances = new int[size][size];
        for(int i = 0; i < distances.length; i++) {
            for(int j = 0; j < i; j++) {
                if(option == 'c') {
                    distances[i][j] = hamming(hm.getCol(i), hm.getCol(j));
                }
                if(option == 'r') {
                     distances[i][j] = hamming(hm.getRow(i), hm.getRow(j));
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
                    distances[i][j] = euclidean(hm.getColDouble(i), hm.getColDouble(j));
                }
                if(option == 'r') {
                    distances[i][j] = euclidean(hm.getRowDouble(i), hm.getRowDouble(j));
                }   
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }
    
    public static int hamming(int[] v0, int[] v1) {
        int count = 0;
        for(int i = 0; i < v0.length; i++)
            count = v0[i] == v1[i] ? count : count+1;
        return count;
    }
    
    public static double euclidean(double[] v0, double[] v1) {
        double count = 0.0;
        for(int i = 0; i < v0.length; i++)
            count += (v0[i]  - v1[i]) * (v0[i]  - v1[i]);
        return Math.sqrt(count);
    }
    
    public static double manhattan(double[] v0, double[] v1) {
        double count = 0.0;
        for(int i = 0; i < v0.length; i++) 
            count += Math.abs(v0[i]  - v1[i]);
        return count;
    }
    
}
