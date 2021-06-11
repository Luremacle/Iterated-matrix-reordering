package representation;

public abstract class Representation {
    public int height, width;
    public boolean discrete; // discrete := binary or categorical
    public int[] rowLabels, colLabels;
    
    public abstract int get(int row, int col);
    
    public abstract double getDouble(int row, int col);
    
    public boolean isDiscrete() { return discrete; }
}
