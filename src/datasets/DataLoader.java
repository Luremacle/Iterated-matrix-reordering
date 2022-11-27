package datasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import representation.ContinuousRepresentation;
import representation.DiscreteRepresentation;


public class DataLoader {
    public static DiscreteRepresentation Pareto() {
        DiscreteRepresentation m = new DiscreteRepresentation(300,300);
        m.circle(1, 0, 0, 1000, 1000);
        m.circle(0, 400, 400, 400, 400);
        m.addNoise(0.2,0.2);
        return m;
    }
    
    public static DiscreteRepresentation Triangles() {
        DiscreteRepresentation m = new DiscreteRepresentation(300,300);
        m.triangles(0, 0, 100, 100);
        m.triangles(100, 100, 200, 200);
        m.triangles(200, 200, 300-1, 300-1);
        m.addNoise(0.2,0.2);
        return m;
    }
    
    public static DiscreteRepresentation Blocks() {
        DiscreteRepresentation m = new DiscreteRepresentation(300,300);
        int epsilon = 4;
        m.cluster(0, 0, 50, 50+epsilon, 0);
        m.cluster(50, 50-epsilon, 100, 100+epsilon, 1);
        m.cluster(100, 100-epsilon, 150, 150+epsilon, 2);
        m.cluster(150, 150-epsilon, 200, 200+epsilon, 3);
        m.cluster(200, 200-epsilon, 250, 250+epsilon, 4);
        m.cluster(250, 250-epsilon, 299, 299, 5);
        m.addNoise(0.2,0.2);
        return m;
    }
    
    public static DiscreteRepresentation Banded() {
        DiscreteRepresentation m = new DiscreteRepresentation(300,300);
        m.band(60);
        m.addNoise(0.2,0.2);
        return m;
    }

    public static ContinuousRepresentation testBanded() {
        ContinuousRepresentation m = new ContinuousRepresentation(300, 300);
        m.band(60, 1);
        double[] possVal = {0, 1};
        m.addNoise(possVal, 0.2);
        return m;
    }
    
    public static DiscreteRepresentation loadNet0() {
        DiscreteRepresentation m = new DiscreteRepresentation(500,500);
        m.bandCluster(5);
        m.addNoise(0.01,0.9);
        m.symmetry();
        return m;
    }
    
    public static DiscreteRepresentation loadNet1() {
        DiscreteRepresentation m = new DiscreteRepresentation(400,400);
        DiscreteRepresentation x = new DiscreteRepresentation(400,400);
        DiscreteRepresentation y = new DiscreteRepresentation(400,400);
        DiscreteRepresentation z = new DiscreteRepresentation(400,400);
        m.cluster(0, 0, 99, 99, 0); m.addNoise(0,0.45);
        x.cluster(0, 0, 199, 199, 1); x.addNoise(0,0.7);
        y.cluster(0, 0, 299, 299, 2); y.addNoise(0,0.85);
        z.cluster(0, 0, 399, 399, 3); z.addNoise(0,0.92);
        m.addRep(x);m.addRep(y);m.addRep(z);
        m.setLabels(0, 99, 0);m.setLabels(100, 199, 1);m.setLabels(200, 299, 2);m.setLabels(300, 399, 3);
        m.symmetry();
        return m;
    }
    
    public static DiscreteRepresentation loadNet2() {
        DiscreteRepresentation m = new DiscreteRepresentation(400,400);
        m.clusterProba(0,0,99,99,0.05);m.clusterProba(100,100,199,199,0.05);m.clusterProba(200,200,299,299,0.05);m.clusterProba(300,300,399,399,0.05);
        m.clusterProba(0,100,99,199,0.33);m.clusterProba(100,200,199,299,0.33);m.clusterProba(200,300,299,399,0.33);
        m.clusterProba(0,200,99,299,0.25);m.clusterProba(100,300,199,399,0.25);
        m.clusterProba(0,300,99,399,0.15);
        m.symmetry();
        m.setLabels(0, 99, 0);m.setLabels(100, 199, 1);m.setLabels(200, 299, 2);m.setLabels(300, 399, 3);
        return m;
    }
    
    public static DiscreteRepresentation loadNet3() {
        double high = 0.3;
        double med = 0.15;
        double low = 0.05;
        double nul = low;
        DiscreteRepresentation m = new DiscreteRepresentation(500,500);
        m.clusterProba(0,0,99,99,high);m.clusterProba(100,100,199,199,high);m.clusterProba(200,200,299,299,high);m.clusterProba(300,300,399,399,high);m.clusterProba(400,400,499,499,high);
        m.clusterProba(0,100,99,199,med);m.clusterProba(100,200,199,299,med);m.clusterProba(200,300,299,399,med);m.clusterProba(300,400,399,499,med);
        m.clusterProba(0,200,99,299,low);m.clusterProba(100,300,199,399,low);m.clusterProba(200,300,299,499,low);
        m.clusterProba(0,300,99,399,low);m.clusterProba(100,400,199,499,low);
        m.clusterProba(0,400,99,499,nul);
        m.symmetry();
        m.setLabels(0, 99, 0);m.setLabels(100, 199, 1);m.setLabels(200, 299, 2);m.setLabels(300, 399, 3);m.setLabels(400, 499, 4);
        return m;
    }
    
    public static DiscreteRepresentation loadNetwork(String net, String lab) throws FileNotFoundException, IOException {
        File file_net = new File(net); 
        File file_lab = new File(lab); 
        BufferedReader br = new BufferedReader(new FileReader(file_lab));
        String st = br.readLine();
        String[] split = st.split(" ");
        int size = split.length;
        int[][] buffer = new int[size][size];
        int[] labels = new int[size];
        for(int i = 0; i < size; i++)
            labels[i] = Integer.parseInt(split[i]);
        
        br.close();
        br = new BufferedReader(new FileReader(file_net));
        st = br.readLine();
        while(st != null) {
            split = st.split(" ");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            buffer[x][y] = 1; buffer[y][x] = 1;
            st = br.readLine();
        }
        br.close();
        DiscreteRepresentation dr = new DiscreteRepresentation(buffer);
        dr.colLabels = labels; dr.rowLabels = labels;
        return dr;
    }
    
    public static DiscreteRepresentation loadMammals() throws FileNotFoundException, IOException {
        File file = new File("src/datasets/mammals.backup"); 
        int[][] buffer = new int[2221][124];
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = br.readLine();
        int r = 0;
        while(st != null) {
            String[] split = st.split(" ");
            for(int i = 0; i < split.length; i++) {
                buffer[r][Integer.parseInt(split[i])] = 1;
            }
            st = br.readLine();
            r++;
        }
        br.close();
        DiscreteRepresentation dr = new DiscreteRepresentation(buffer);
        return dr;
    }

    public static ContinuousRepresentation zonesRectangle() {
        ContinuousRepresentation m = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation x = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation y = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation z = new ContinuousRepresentation(400, 400);
        m.rectangle(0, 0, 200, 200, 0);
        x.rectangle(200, 0, 400, 200, 0.3);
        y.rectangle(0, 200, 200, 400, 0.6);
        z.rectangle(200, 200, 400, 400, 0.9);
        m.addRep(x); m.addRep(y); m.addRep(z);

        double[] possVal = {0, 0.3, 0.6, 0.9};
        m.addNoise(possVal, 0.2);
        return m;
    }

    public static ContinuousRepresentation zonesTriangle() {
        ContinuousRepresentation m = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation x = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation y = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation z = new ContinuousRepresentation(400, 400);
        m.triangle(0, 0, 0, 200, 200, 0, 0.6);
        x.triangle(400, 0, 400, 200, 200, 0, 0.2);
        y.triangle(0, 200, 0, 400, 200, 400, 0.4);
        z.triangle(200, 400, 400, 400, 400, 200, 0.8);
        m.addRep(x);
        m.addRep(y);
        m.addRep(z);

        double[] possVal = {0, 0.2, 0.4, 0.6, 0.8};
        m.addNoise(possVal, 0.2);
        return m;
    }

    public static ContinuousRepresentation zonesTriangle2() {
        ContinuousRepresentation m = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation x = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation y = new ContinuousRepresentation(400, 400);
        ContinuousRepresentation z = new ContinuousRepresentation(400, 400);
        m.triangle(0, 0, 135, 20, 30, 200, 0.6);
        x.rectangle(30, 20, 135, 200, 0.2);
        // x.triangle(400, 0, 400, 200, 200, 200, 0.2);
        // y.triangle(0, 200, 0, 400, 200, 400, 0.4);
        // z.triangle(200, 400, 400, 400, 400, 200, 0.8);
        m.addRep(x);
        m.addRep(y);
        m.addRep(z);

        double[] possVal = { 0, 0.2, 0.4, 0.6, 0.8 };
        // m.addNoise(possVal, 0.2);qq
        return m;
    }
}
