package convolution;

import utils.Utils;

public class Convolution {
    
    private static Convolution instance; // singleton
    
    private Convolution() {}
    
    public static Convolution getInstance() {
        if(instance == null) {
            instance = new Convolution();
        }   
        return instance;
    }
    
    /**
     * 
     * @param matrix: source matrix on which the convolution will be applied
     * @param kernel: kernel used for convolution
     * @param normalized: normalize results [0,1], dividing by the sum of the kernel
     * @return convolution of matrix
     */
    public double[][] apply(double [][] matrix, int [][] kernel, boolean normalized) {
        double [][] conv = new double [matrix.length][matrix[0].length];
        for(int i = 0; i < conv.length; i ++ ) {
            for(int j = 0; j < conv[0].length; j ++ ) {
                conv[i][j] = pointConvolution(matrix, kernel, i, j, normalized);
            }
        }
        return conv;
    }
    
    public double[][] apply(int[][] matrix, int [][] kernel, boolean normalized) {
        return apply(toDoubleArray(matrix), kernel, normalized);
    }

    private double pointConvolution(double[][] matrix, int[][] kernel, int r, int c, boolean normalized) {
        double sum = 0.0;
        double global = 0.0;
        int h = kernel.length / 2;
        int w = kernel[0].length / 2;
        for(int i = -h; i <= h; i++) {
            for(int j = -w; j <= w; j++ ) {
                if(r + i >= 0 && c + j >= 0 && r + i < matrix.length && c + j < matrix[0].length) {
                    sum += matrix[r + i][c + j] * kernel[i + h][j + w];
                    global += kernel[i + h][j + w];
                }
            }
        }
        return normalized ? (sum / global) : sum;
    }
    
    /**
     * @return the error or difference between the source matrix and its
     * convoluted version after applying the kernel
     */
    public double differenceConvolution(int[][] source, int[][] kernel) {
        double score = 0.0;
        double[][] difConvolution = differenceConvolutionMatrix(source, kernel);
        for(int i = 0; i < source.length; i++) {
            for(int j = 0; j < source[0].length; j++) {
                score += difConvolution[i][j];
            }
        }
        return score;
    }
    
    public double differenceConvolution(double[][] source, int[][] kernel) {
        double score = 0.0;
        double[][] difConvolution = differenceConvolutionMatrix(source, kernel);
        for(int i = 0; i < source.length; i++) {
            for(int j = 0; j < source[0].length; j++) {
                score += difConvolution[i][j];
            }
        }
        return score;
    }
    
    private double[][] differenceConvolutionMatrix(double[][] source, int[][] kernel) {
        double[][] difference = new double[source.length][source[0].length];
        double[][] convolution = apply(source, kernel, true);
        for(int i = 0; i < source.length; i++) {
            for(int j = 0; j < source[0].length; j++) {
                difference[i][j] = Math.abs(source[i][j] - convolution[i][j]);
            }
        }
        return difference;
    }

    private double[][] differenceConvolutionMatrix(int[][] source, int[][] kernel) {
        double[][] difference = new double[source.length][source[0].length];
        double[][] convolution = apply(source, kernel, true);
        for(int i = 0; i < source.length; i++) {
            for(int j = 0; j < source[0].length; j++) {
                difference[i][j] = Math.abs(source[i][j] - convolution[i][j]);
            }
        }
        return difference;
    }
    
    /**
     * 
     * @param array: matrix before the threhsolding
     * @param threshold: value [0,1]
     * @return a new thresholded matrix
     */
    public int[][] threshold(double[][] array, double threshold) {
        int[][] matrix = new int[array.length][array[0].length];
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                matrix[i][j] = array[i][j] <= threshold ? 0 : 1;
            }
        }
        return matrix;
    }

    public int[][] OTSUThreshold(double[][] matrix) {
        double threshold = Utils.otsu(matrix);
        return threshold(matrix, threshold);
    }
    
    private int[][] toIntArray(double[][] matrix) {
        int[][] convert = new int[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                convert[i][j] = (int) (matrix[i][j]);
            }
        }
        return convert;
    }
    
    private double[][] toDoubleArray(int[][] matrix) {
        double[][] convert = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                convert[i][j] = 1.0 * matrix[i][j];
            }
        }
        return convert;
    }
}
