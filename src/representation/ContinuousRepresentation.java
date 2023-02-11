package representation;

import java.util.Random;
public class ContinuousRepresentation extends Representation {
    public double[][] matrix;
    // SEED to generate the same randomized matrix
    Random rand = new Random(42);

    public ContinuousRepresentation(int height, int width) {
        this.discrete = false;
        this.height = height;
        this.width = width;
        matrix = new double[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                matrix[i][j] = 0.0;
            }
        }
        this.rowLabels = new int[height];
        this.colLabels = new int[width];
    }
    
    public ContinuousRepresentation(double[][] matrix) {
        this.discrete = false;
        this.height = matrix.length;
        this.width = matrix[0].length;
        this.matrix = matrix;
        this.rowLabels = new int[height];
        this.colLabels = new int[width];
    }
    
    @Override
    public int get(int row, int col) {
        return (int) getDouble(row, col);
    }
    
    @Override
    public double getDouble(int row, int col) {
        return matrix[row][col];
    }

    public void addNoiseDefault(double percentages) {  
        int n = 6;
        double[] possVal = { 0.0, 0.2, 0.4, 0.6, 0.8, 1.0 };                                                       
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                double r = rand.nextDouble();

                if (r < (percentages * n / (n - 1)))
                    matrix[i][j] = possVal[rand.nextInt(n)];
            }
        }
    }

    public void addNoise(double[] possibleVal, double[] percentages) {    // possibleVal are possible values in our graph, percentages are their associated probabilities
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                double r = rand.nextDouble();

                int valIndex = -1;
                int n = possibleVal.length;
                for (int k = 0; k < n; k++) {
                    if (matrix[i][j] == possibleVal[k]){
                        valIndex = k;
                        break;
                    }
                }

                if (r < (percentages[valIndex] * n/(n-1)))
                    matrix[i][j] = possibleVal[rand.nextInt(n)];
            }
        }
    }

    public void addNoise(double[] possibleVal, double percentage) {
        double[] percentages = new double[possibleVal.length];
        for (int i = 0; i < percentages.length; i++) {
            percentages[i] = percentage;
        }
        this.addNoise(possibleVal, percentages);
    }

    public void addNoiseRandom(double percentage){    
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                double r = rand.nextDouble();
                if(r < percentage)
                    matrix[i][j] = r;
            }
        } 
    }

    public void rectangle(int x0, int y0, int x1, int y1, double value){
        for (int i = y0; i < y1; i++) {
            for (int j = x0; j < x1; j++) {
                matrix[i][j] = value;
            }
        }
    }

    public void triangle(int x0, int y0, int x1, int y1, int x2, int y2, double value){   
        double a01; double a12; double a02;
        double b01; double b12; double b02;
        int c01; int c12; int c02;

        if (x0 == x1) {
            a01 = 1.0;
            b01 = -x0;
            c01 = 0;
        } else{
            a01 = (double) (y1 - y0) / (x1 - x0);
            b01 = y0 - a01*x0;
            c01 = 1;
        }
        if (x1 == x2) {
            a12 = 1.0;
            b12 = -x1;
            c12 = 0;
        } else {
            a12 = (double) (y1 - y2) / (x1 - x2);
            b12 = y1 - a12*x1;
            c12 = 1;
        }
        if (x0 == x2) {
            a02 = 1.0;
            b02 = -x0;
            c02 = 0;
        } else {
            a02 = (double) (y2 - y0) / (x2 - x0);
            b02 = y0 - a02*x0;
            c02 = 1;
        }

        for (int i = 0; i < height; i++) { // coordonate in y
            for (int j = 0; j < width; j++) { // coordonate in x
                if (((c01 * y2 - b01 - a01 * x2 > 0) == (c01 * i - b01 - a01 * j > 0))
                        && ((c12 * y0 - b12 - a12 * x0 > 0) == (c12 * i - b12 - a12 * j > 0))
                        && ((c02 * y1 - b02 - a02 * x1 > 0) == (c02 * i - b02 - a02 * j > 0))) {
                    matrix[i][j] = value;
                }
            }
        }
    }

    public void addRep(ContinuousRepresentation cr) { // ajouter des représentations sur l'actuelle
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                matrix[i][j] = Math.min(1.0, getDouble(i, j) + cr.getDouble(i, j));
            }
        }
    }

    public void band(int width_band, int value) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                matrix[i][j] = Math.abs(i - (1.0 * height / width) * j) < width_band ? value : matrix[i][j];
            }
        }
    }
}
