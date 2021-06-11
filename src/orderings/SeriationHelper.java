/*******************************************************************
 * CLASS: SeriationHelper
 *
 * This class calls and executes a secondary R script to solve a seriation 
 * instance problem. R must be installed (2.14.0+) as well as the "seriation"
 * package [1] via command line: install.packages("seriation"). This class
 * should probably be adapted according to the OS used. Precise in PATH the
 * full path of the R script executable.

 * [1] seriation: Infrastructure for Ordering Objects Using Seriation
 * Michael Hahsler, Christian Buchta, Kurt Hornik 
 * https://cran.r-project.org/web/packages/seriation/index.html 
 * 
 ******************************************************************/

package orderings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SeriationHelper {
    
    public final static String SCRIPT0 = "R/seriation_asdist.r";
    public final static String SCRIPT1 = "R/seriation_dist.r";
    public final static String PATH = "/Library/Frameworks/R.framework/Versions/4.0/Resources/Rscript";

    public static int[] callRScript(double[][] entry, String method, boolean asDist) {
        File o = new File("R/row.order"); o.delete(); // delete last order
        File f = new File("R/temp.txt"); // tempory file to store the matrix
        write(entry, f);
        String scriptname = asDist ? SCRIPT0 : SCRIPT1;
        try { 
            // execution call need to be adapted depending on the OS !
            Process p = Runtime.getRuntime().exec(PATH + " " + scriptname + " " + method);
            System.out.println("executing: " + method);
            System.out.println("Waiting ...");
            p.waitFor();
            System.out.println("Done.");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error: R script failed ! Check path in SeriationHelper.java !");
            System.exit(0);
        }
        f.delete();
        return readOrder("R/row.order", entry.length);
    }
    
    private static void write(double[][] matrix, File file) {
        FileWriter fr = null;
        BufferedWriter br = null;
        String sep = System.getProperty("line.separator");
        try{
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            for(int r = 0; r < matrix.length; r++) {
                for(int c = 0; c < matrix[0].length; c++) {
                    br.write(matrix[r][c] + " ");
                }
                br.write(sep);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static int[] readOrder(String filename, int size) {
        BufferedReader br;
        File file = new File(filename); 
        int[] order = new int[size];
        if(!file.exists()) {
            System.out.println("Error: R output not found !");
            System.out.println("Seriation R package is needed !");
            System.out.println("install.packages(\"seriation\")");
            System.exit(0);
        }
        try {
            br = new BufferedReader(new FileReader(file));
            String st = br.readLine(); // skip first line
            int i = 0;
            while ((st = br.readLine()) != null) {
                order[i] = Integer.parseInt(st) - 1;
                i++;
            }
        } catch(IOException e) {
            System.out.println("Error: wrong output !");
            System.exit(0);
        }
        return order;
    }
    
}
