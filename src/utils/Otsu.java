package utils;

public class Otsu {
    
    // number of bins / intervals for the histogram
    public final static int K_PARTITION = 255; 
    
    public static double otsu(double[][] matrix) {
        int[] histData = new int[K_PARTITION+1];
        int total = matrix.length * matrix[0].length;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                int h = (int) (matrix[i][j] * K_PARTITION);
                histData[h]++;
            }
        }

        float sum = 0;
        for (int t=0 ; t <= K_PARTITION ; t++) sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t=0 ; t <= K_PARTITION ; t++) {
           wB += histData[t];               // Weight Background
           if (wB == 0) continue;

           wF = total - wB;                 // Weight Foreground
           if (wF == 0) break;

           sumB += (float) (t * histData[t]);

           float mB = sumB / wB;            // Mean Background
           float mF = (sum - sumB) / wF;    // Mean Foreground

           // Calculate Between Class Variance
           float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

           // Check if new maximum found
           if (varBetween > varMax) {
              varMax = varBetween;
              threshold = t;
           }
        }
        //System.out.println(1.0 * threshold / K_PARTITION);
        return (1.0 * threshold / K_PARTITION);
    }
}
