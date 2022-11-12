package gui;

import representation.HeatMap;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuiHelper {
    
    public static void showHeatMap(HeatMap hm, String name) {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	final JLabel status = new JLabel("Starting up...");
	statusBar.add(status);
	JFrame frame = new JFrame(name);
        if(hm.rep.discrete) {
            BinaryGUI mandel = new BinaryGUI(status, hm.toArray());
            mandel.rowLabels = hm.getRowLabels();
            mandel.colLabels = hm.getColLabels();
            frame.add(mandel);
            frame.addKeyListener(mandel);
        }
        else{
            ContinuousGUI mandel = new ContinuousGUI(status, hm.toArray());
            mandel.rowLabels = hm.getRowLabels();
            mandel.colLabels = hm.getColLabels();
            frame.add(mandel);
            frame.addKeyListener(mandel);
        }
        frame.setSize(301, (int) (300 + 22));
        frame.setVisible(true);
    }
}
