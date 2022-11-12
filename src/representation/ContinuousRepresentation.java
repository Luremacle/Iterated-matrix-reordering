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
        for (int i = x0; i < x1; i++) {
            for (int j = y0; j < y1; j++) {
                matrix[i][j] = value;
            }
        }
    }

    public void addRep(ContinuousRepresentation cr) { // ajouter des représentations sur l'actuelle
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                matrix[i][j] = Math.min(1.0, get(i, j) + cr.get(i, j));
            }
        }
    }
}
