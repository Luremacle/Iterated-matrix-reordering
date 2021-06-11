package framework;

import convolution.Kernels;
import convolution.Convolution;
import orderings.*;
import representation.*;


public class Configuration {
    public Convolution c = Convolution.getInstance();
    
    public String PRELIMINARY_METHOD;
    public String EMBEDDED_METHOD;
    public char TYPE; // {'n' or 'b'}
    public int MAX_ITERATIONS;
    public boolean THRESHOLD;
    public boolean SMOOTHING;
    public boolean NETWORK;
    public String CRITERION;
    public String[] KERNELS;
    
    public MatrixOrdering getOrdering(HeatMap hm, boolean preliminary) {
        if (preliminary)
            return factoryOrdering(hm, PRELIMINARY_METHOD);
        else 
            return factoryOrdering(hm, EMBEDDED_METHOD);
    }
    
    public MatrixOrdering factoryOrdering(HeatMap hm, String method) {
        MatrixOrdering sr;
        if(method.equals("nested")) 
            sr = new NestedOrdering(hm);
        else if(method.equals("bary")) 
            sr =  new BarycentricOrdering(hm, 5000);
        else {
            sr =  NETWORK ? new ROrderingNetworks(hm, method) : new ROrderingTable(hm, method);
        }
        sr.symmetry = NETWORK;
        return sr;
    }
    
    public MatrixOrdering getSmoothOrdering(HeatMap hm, HeatMap template) {
        MatrixOrdering sr;
        if (TYPE == 'b' && template.rep.discrete) {
            sr = new BinarySmooth(hm, template.toArrayInt());
        }
        else {
            sr = new ContinuousSmooth(hm, template.toArrayDouble());
        }
        sr.symmetry = NETWORK;
        return sr;
    }
    
    /*
    * Evaluation / Stop criterion used to assess 
    * the quality of the ordered matrix
    */
    public double getScore(HeatMap hm) {
        if (TYPE == 'b') {
            return c.differenceConvolution(hm.toArrayInt(), Kernels.getKernel(CRITERION));
        } else {
            return c.differenceConvolution(hm.toArrayDouble(), Kernels.getKernel(CRITERION));
        }
    }
    
    public double[][] getConvolutionMatrix(HeatMap hm, int[][] kernel) {
        if (TYPE == 'b') {
            return c.apply(hm.toArrayInt(), kernel, true);
        } else {
            return c.apply(hm.toArrayDouble(), kernel, true);
        }
    }
    
    public int[][] getThresholdMatrix(HeatMap hm, int[][] kernel) {
         return c.OTSUThreshold(getConvolutionMatrix(hm, kernel));
    }
    
}
