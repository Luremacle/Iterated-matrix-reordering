
package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class LabelsCheck {
    
    public static double accuracy(int[] labels, int kNearest) {
        int count = 0;
        int l = labels.length;
        for(int i = 0; i < l; i++) {
            count = match(labels, i , kNearest) ? count + 1 : count;
        }
        return 1.0 * count / l;
    }
    
    private static boolean match(int[] labels, int pos, int k) {
        List<Integer> l = new ArrayList<>();
        int pivot = 1;
        while(l.size() < k && (pos-pivot >= 0 || pos+pivot < labels.length)) {
            int left = pos - pivot >= 0 ? labels[pos - pivot] : -1;
            int right = pos + pivot < labels.length ? labels[pos + pivot] : -1;
            if(left != - 1)
                l.add(left);
            if(right != -1)
                l.add(right);
            pivot++;
        }
        return mostCommon(l) == labels[pos];
    }
    
    private static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();
        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }
        Entry<T, Integer> max = null;
        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        return max.getKey();
    }
    
}
