package representation;

public class ContinuousRepresentation extends Representation {
    public double[][] matrix;

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
}
