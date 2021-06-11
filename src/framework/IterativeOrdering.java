
package framework;

import convolution.Kernels;
import utils.LabelsCheck;
import gui.GuiHelper;
import orderings.MatrixOrdering;
import representation.ContinuousRepresentation;
import representation.DiscreteRepresentation;
import representation.HeatMap;


public class IterativeOrdering {
    public Configuration config;
    
    public IterativeOrdering(Configuration config) {
        this.config = config;
    }
    
    /*
    * Iterated ordering of the HeatMap
    */
    public HeatMap Order(HeatMap hm) {
        HeatMap best, candidate;
        double s0, s1;
        int count = 0;
        boolean improved;
        
        MatrixOrdering preliminary = config.getOrdering(hm, true);
        preliminary.solve();
        best = preliminary.getHeatMap();
        s0 = config.getScore(best);
        System.out.println("Preliminary ordering:");
        System.out.println("Evaluation criterion: " + s0);
        System.out.println("Labels accuracy: " + LabelsCheck.accuracy(best.getRowLabels(), 10));
        GuiHelper.showHeatMap(best, "preliminary");
        do {
            improved = false;
            for(String s : config.KERNELS) {
                int[][] k = Kernels.getKernel(s);
                candidate = generate(best, k);
                s1 = candidate.score;
                if(s1 < s0) {
                    best = candidate;
                    s0 = s1;
                    improved = true;
                    GuiHelper.showHeatMap(best, "candidate: " + count);
                    System.out.println("Candidate " + count + " :");
                    System.out.println("Evaluation criterion: " + s0);
                    System.out.println("Labels accuracy: " + LabelsCheck.accuracy(best.getRowLabels(), 10));
                    break;
                }
            }
            count++;
        } while(count < config.MAX_ITERATIONS && improved);
        return best;
    }
    
    /*
    * Helper method to generate a candidate ordered matrix
    * by applying a specific kernel and ordering method
    */
    private HeatMap generate(HeatMap best, int[][] k) {
        HeatMap temp;
        if(config.THRESHOLD) { // convolution + thresholding
            int[][] threshold = config.getThresholdMatrix(best, k);
            temp = new HeatMap(new DiscreteRepresentation(threshold));
        }
        else { // convolution only
            double[][] blurred = config.getConvolutionMatrix(best, k);
            temp = new HeatMap(new ContinuousRepresentation(blurred));
        }
        MatrixOrdering embedded = config.getOrdering(temp, false);
        embedded.solve();
        // Renoising
        temp = reorder(best, embedded.getHeatMap());
        // Smoothing
        if(config.SMOOTHING) {
            double s0 = config.getScore(temp);
            temp.score = s0;
            MatrixOrdering smoothing = config.getSmoothOrdering(temp, embedded.getHeatMap());
            smoothing.solve();
            double s1 = config.getScore(smoothing.getHeatMap());
            if(s0 > s1) {
                smoothing.getHeatMap().score = s1;
                return smoothing.getHeatMap();
            }
        }
        return temp; 
    }
    
    /*
    * Method used for the renoising process.
    * Reorder original with the order of ordered.
    * Needed because indices of ordered != original
    */
    public static HeatMap reorder(HeatMap original, HeatMap ordered) {
        int[] new_col_order = new int[original.col_order.length];
        int[] new_row_order = new int[original.row_order.length];
        // replacement
        for(int i = 0; i < new_col_order.length; i++)
            new_col_order[i] = original.col_order[ordered.col_order[i]];
        for(int i = 0; i < new_row_order.length; i++)
            new_row_order[i] = original.row_order[ordered.row_order[i]];
        // new HeatMap with correct order
        HeatMap temp = new HeatMap(original.rep);
        temp.col_order = new_col_order;
        temp.row_order = new_row_order;
        return temp;
    }
}
