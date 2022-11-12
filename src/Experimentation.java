import datasets.DataLoader;
import framework.Configuration;
import framework.IterativeOrdering;
import gui.GuiHelper;
import java.io.IOException;

import representation.ContinuousRepresentation;
import representation.DiscreteRepresentation;
import representation.HeatMap;

public class Experimentation {
    
    /* CONFIGURATION (parameters of the framework) :
     * - max nb limit of iterations (integer)
     * - type of matrix: 'b' : binary; 'n' : numeric (char)
     * - preliminary ordering method (String)
     * - embedded / base ordering method (String)
     * - network, enabled if network (matrix is symmetric) (boolean)
     * - smoothing, enabled to perform smoothing refinement (boolean)
     * - thresholding, enabled to apply a (OTSU) threshold after convolution (boolean)
     * - kernel used as evaluation criterion (String)
     * - set of kernels used for convolution / thresholding (list of Strings)
     *
     * List of methods: (use "complete" as defaut linkage)
     * - "bary": barycenter heuristic
     * - "nested": nested
     * - "TSP": traveling salesman problem solver (2-opt random / non determistic)
     * - "OLO_complete": optimal leaf ordering
     * - "GW_complete": Gruvaeus and Wainer
     * - "HC_complete": hierarchical clustering
     * - "MDS": muldi-dimensionnal scaling
     * - "Spectral": spectral seriation (2-SUM)
     * - ...
     */
    
    public static Configuration getConfig() {
        Configuration c = new Configuration();
        c.MAX_ITERATIONS = 50;
        c.TYPE = 'b';
        c.NETWORK = false;
        c.SMOOTHING = true;
        c.THRESHOLD  = true; // mandatory for bary, nested
        c.PRELIMINARY_METHOD = "OLO";
        c.EMBEDDED_METHOD = "OLO";
        c.CRITERION = "GBS_49"; // GBS_25 for mammals, GBS_49 originally
        /*c.KERNELS = new String[]{
            "GBE_3","GBS_3","GBS_5",
            "GBE_25","GBS_25","GBE_5",
            "GBS_15","GBE_9","GBE_15",
            "GBS_9"
        };*/ // for networks (slower convergence, more refined)
        c.KERNELS = new String[]{
            "GBE_25","GBS_25","GBE_5",
            "GBS_15","GBE_9","GBE_15",
            "GBS_9"
        }; // for tables (quicker, less refined for networks)
        return c;
    }
    
    /*
    * Experiments for reproducibility (de-comment the desired dataset)
    *
    * We use the same random seed to generate the noise. However some methods 
    * are not deterministic and the results may vary (slightly) from one run 
    * to another. The pre-ordering shuffle can slightly impact the final 
    * score as well due to tie linkages.
    */
    public static void main (String [] args) throws IOException {
        Configuration c = getConfig();
        DiscreteRepresentation m;
        // ContinuousRepresentation m;
        
        // Tables:
        // m = DataLoader.Pareto();
        // m = DataLoader.Banded();
        m = DataLoader.Triangles();
        // m = DataLoader.Blocks();
        // m = DataLoader.loadMammals();

        // m = DataLoader.zones();
        
        // Networks: (set NETWORK flag true !)
        // m = DataLoader.loadNet0(); // assortative
        // m = DataLoader.loadNet1(); // core-periphery
        //m = DataLoader.loadNet2(); // dissassortative
        //m = DataLoader.loadNet3(); // ordered
        // m = DataLoader.loadNetwork("src/datasets/blogs_net", "src/datasets/blogs_labels");
        // m = DataLoader.loadNetwork("src/datasets/india_net", "src/datasets/india_labels");
        // m = DataLoader.loadNetwork("src/datasets/mails_net", "src/datasets/mails_labels");
        // m = DataLoader.loadNetwork("src/datasets/news_net", "src/datasets/news_labels");
        
        IterativeOrdering or = new IterativeOrdering(c);
        HeatMap hm = new HeatMap(m);
        System.out.println("Original score criterion: " + c.getScore(hm));
        GuiHelper.showHeatMap(hm, "original");
        // hm.shuffle(); //optional - may impact scores for some methods
        // GuiHelper.showHeatMap(hm, "shuffled");
        HeatMap result = or.Order(hm);
        GuiHelper.showHeatMap(result, "Final order - score: " + c.getScore(result));
    }
    
}
