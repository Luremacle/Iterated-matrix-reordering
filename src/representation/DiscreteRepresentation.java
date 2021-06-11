package representation;

import java.util.Random;

public class DiscreteRepresentation extends Representation{
    public int[][] matrix;
    // SEED to generate the same randomized matrix
    Random rand = new Random(42);
    
    public DiscreteRepresentation(int height, int width) {
        this.discrete = true;
        this.height = height;
        this.width = width;
        matrix = new int[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }
        this.rowLabels = new int[height];
        this.colLabels = new int[width];
    }
    
    public DiscreteRepresentation(int[][] matrix) {
        this.discrete = true;
        this.height = matrix.length;
        this.width = matrix[0].length;
        this.matrix = matrix;
        this.rowLabels = new int[height];
        this.colLabels = new int[width];
    }
    
    @Override
    public int get(int row, int col) {
        return matrix[row][col];
    }
    
    @Override
    public double getDouble(int row, int col) {
        return get(row, col);
    }
    
    public void addNoise(double percentage_0, double percentage_1) {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                double r = rand.nextDouble();
                if((matrix[i][j] == 1 && r < percentage_1) || (matrix[i][j] == 0 && r < percentage_0))
                    matrix[i][j] = (matrix[i][j] + 1) % 2;
            }
        } 
    }
    
    public void addNoise(double percentage) {
        this.addNoise(percentage, percentage);
    }
    
    
    public void cluster(int x0, int y0, int x1, int y1, int lab) {
        for(int i = x0; i <= x1; i++) {
            for(int j = y0-0; j <= y1+0; j++) {
                try {
                    matrix[i][j] = 1;
                    rowLabels[i] = lab;
                    colLabels[j] = lab;
                } catch (Exception e) {  
                }
            }
        }
    }
    
    public void clusterProba(int x0, int y0, int x1, int y1, double proba) {
        for(int i = x0; i <= x1; i++) {
            for(int j = y0; j <= y1; j++) {
                double r = rand.nextDouble();
                if(r < proba) {
                    matrix[i][j] = 1;
                }
            }
        }
    }
    
    public void triangles(int x0, int y0, int x1, int y1) {
        int a = 0;
        for(int i = x0; i <= x1; i++) {
            int b = 0;
            for(int j = y0; j <= y1; j++) {
                if(a >= b)
                    matrix[i][j] = 1;
                b++;
            }
            a++;
        }
    }
    
    public void bandCluster(int number) {
        int side_width = width / number;
        int side_height = height / number;
        for(int i = 0; i < number; i++) {
            this.cluster(i * side_height, i * side_width, ((i+1)*side_height)-1, ((i+1)*side_width)-1, i);
        }
    }
    
    public void bandTriangles(int number) {
        int side_width = width / number;
        int side_height = height / number;
        for(int i = 0; i < number; i++) {
            this.triangles(i * side_height, i * side_width, ((i+1)*side_height)-1, ((i+1)*side_width)-1);
        }
    }
    
    public void nested() {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                matrix[i][j] = i < (-1.0*height/width)*j+height ? 1 : 0;
            }
        } 
    }
    
    public void band(int width_band) {
        band(width_band, 1);
    }
    
    public void whiteBand(int width_band) {
        band(width_band, 0);
    }
    
    public void band(int width_band, int value) {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                //matrix[i][j] = Math.abs(i-j) < width_band ? 1 : 0;
                matrix[i][j] = Math.abs(i-(1.0*height/width)*j) < width_band ? value : matrix[i][j];
            }
        } 
    }
    
    public void addRep(DiscreteRepresentation dr) {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                matrix[i][j] = Math.min(1, get(i,j) + dr.get(i, j));
            }
        }
    }
    
    public void symmetry() {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                if(i>j)
                    matrix[i][j] = matrix[j][i];
            }
        }
    }
    
    public void setLabels(int begin, int stop, int label) {
        for(int i = begin; i <= stop; i++) {
            colLabels[i] = label;
            rowLabels[i] = label;
        }
    }
    
    public void setLabelsRow(int begin, int stop, int label) {
        for(int i = begin; i <= stop; i++) {
            rowLabels[i] = label;
        }
    }
    
    public void setLabelsCol(int begin, int stop, int label) {
        for(int i = begin; i <= stop; i++) {
            colLabels[i] = label;
        }
    }
    
    public void setRowNul(int r) {
        for(int i = 0; i < width; i++) {
            matrix[r][i] = 0;
        }
    }
    
    public void setColNul(int c) {
        for(int i = 0; i < height; i++) {
            matrix[i][c] = 0;
        }
    }

    public void circle(int nb, int h, int k, int rx, int ry) {
        for(int i = 0; i < this.height; i++) {
            for(int j = 0; j < this.width; j++) {
                if(1.0*(i-h)*(i-h)/(rx*rx) + 1.0*(j-k)*(j-k)/(ry*ry) <= 1) {
                    matrix[i][j] = nb;
                }
            }
        }
    }
    
}
