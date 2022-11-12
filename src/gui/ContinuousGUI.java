package gui;

import representation.HeatMap;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.*;

public final class ContinuousGUI extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private final JLabel status;
    private static final long serialVersionUID = 1L;
    private boolean toDrag = false;
    private boolean SMOOTH = true;
    private Image image; // offscreen image for double buffering
    private Graphics graphics; // offscreen graphics for the offscreen image
    private int width, height; // current screen width and height
    private int pal = 0; // color id
    private long time;
    private int mouseX, mouseY;
    private int dragX, dragY;
    double w0, w1, h0, h1;
    int[][] matrix, original;

    public int[] rowLabels, colLabels;
    private boolean labels = true;
    Color[][] pixels;

    public ColorFunction[] palette = {
            (double r) -> new Color((int) (255 - 255 * r), (int) (255 - 255 * r), (int) (255 - 255 * r)), // white-black
            (double r) -> new Color((int) (255 - 255 * r), (int) (255 - 255 * r), 255), // white-blue
            (double r) -> new Color((int) (255 * r), 0, 0), // black-red
    };

    public Color[] labelColor = {
            new Color(220, 46, 59), new Color(73, 162, 89), new Color(43, 52, 141),
            new Color(246, 230, 85), new Color(72, 173, 233), new Color(168, 51, 135),
            new Color(153, 89, 70), new Color(231, 135, 60), new Color(214, 224, 146),
            new Color(110, 51, 135), new Color(237, 174, 175), new Color(169, 164, 122),
            new Color(255, 255, 255)
    };

    public ContinuousGUI(JLabel status, int[][] m) {
        super(true);
        this.status = status;
        this.matrix = m;
        this.original = m;
        this.h0 = 0.0;
        this.w0 = 0.0;
        this.h1 = m.length - 1;
        this.w1 = m[0].length - 1;
        addMouseListener(this);
        addMouseMotionListener(this);
        rowLabels = new int[m.length];
        colLabels = new int[m[0].length];
    }

    private void draw() {
        int LSIZE = 15;
        Dimension size = getSize();
        // Create offscreen buffer for double buffering
        if (image == null || size.width != width || size.height != height) {
            width = size.width;
            height = size.height;
            image = createImage(width, height);
            graphics = image.getGraphics();
        }
        int heightMinus = height;
        int widthMinus = width;

        pixels = new Color[size.height][size.width];
        if (labels) {
            heightMinus = height - LSIZE;
            widthMinus = width - LSIZE;
            for (int r = 0; r < heightMinus; r++) {
                for (int rb = 0; rb < LSIZE; rb++) {
                    int indice = (int) ((1.0 * r / heightMinus) * rowLabels.length);
                    pixels[r][widthMinus + rb] = labelColor[rowLabels[indice]];
                }
            }
            for (int c = 0; c < widthMinus; c++) {
                for (int cb = 0; cb < LSIZE; cb++) {
                    int indice = (int) ((1.0 * c / widthMinus) * colLabels.length);
                    pixels[heightMinus + cb][c] = labelColor[colLabels[indice]];
                }
            }
            for (int rb = 0; rb < LSIZE; rb++) {
                for (int cb = 0; cb < LSIZE; cb++) {
                    pixels[heightMinus + rb][widthMinus + cb] = new Color(255, 255, 255);
                }
            }
        }
        time = System.currentTimeMillis();

        matrix = crop(matrix, (int) h0, (int) w0, (int) h1, (int) w1);
        h0 = 0.0;
        w0 = 0.0;
        h1 = matrix.length - 1;
        w1 = matrix[0].length - 1;
        double[][] surface = new double[heightMinus][widthMinus];
        int a = (int) (1.0 * matrix.length / surface.length);
        int b = (int) (1.0 * matrix[0].length / surface[0].length);
        int a_odd = (a % 2 == 0) ? a + 1 : a;
        int b_odd = (b % 2 == 0) ? b + 1 : b;

        for (int r = 0; r < heightMinus; r++) {
            for (int c = 0; c < widthMinus; c++) {
                double q = (1.0 * r / surface.length) * matrix.length;
                double w = (1.0 * c / surface[0].length) * matrix[0].length;
                if (SMOOTH) {
                    int count = 0;
                    int square = (a_odd * b_odd);
                    for (int x = 0; x < a_odd; x++) {
                        for (int y = 0; y < b_odd; y++) {
                            try {
                                count += matrix[(int) (q) + x][(int) (w) + y];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                square--;
                            }
                        }
                    }
                    surface[r][c] = 1.0 * count / square;
                } else {
                    int new_q = (int) q;
                    int new_w = (int) w;
                    surface[r][c] = matrix[new_q][new_w];
                }
            }
        }
        // Color attribution
        for (int i = 0; i < heightMinus; i++) { // i < hm.rep.size
            for (int j = 0; j < widthMinus; j++) {
                try {
                    pixels[i][j] = palette[pal].color(surface[i][j]);
                } catch (Exception e) {
                    pixels[i][j] = new Color(0, 0, 0);
                }
            }
        }
        time = System.currentTimeMillis() - time;
        // Painting
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                graphics.setColor(pixels[y][x]);
                graphics.drawLine(x, y, x, y); // draws point
            }
        }
        updateStatus();
        repaint();
    }

    private void redraw() {
        draw();
    }

    public void updateStatus() {
        status.setText("Size=" + getWidth() + "x" + getHeight() +
                " - Time=" + time + " ms");
    }

    // To prevent background clearing for each paint()
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        Dimension size = getSize();
        if (image == null || size.width != width || size.height != height)
            redraw();
        g.drawImage(image, 0, 0, null);
        // Select-rectangle or offset-line drawing
        if (toDrag) {
            g.setColor(Color.black);
            g.setXORMode(Color.white);
            int x = Math.min(mouseX, dragX);
            int y = Math.min(mouseY, dragY);
            double w = mouseX + dragX - 2.0 * x;
            double h = mouseY + dragY - 2.0 * y;
            g.drawRect(x, y, (int) (width * (w / width)), (int) (height * (h / height)));
        }
    }

    // Methods from MouseListener interface
    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = dragX = e.getX();
        mouseY = dragY = e.getY();
        toDrag = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        toDrag = false;
        int x = e.getX();
        int y = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (x != mouseX && y != mouseY) { // zoomed
                if (x >= 0 && y >= 0 && x < width && y < height) {
                    w0 = Math.min((1.0 * x / width) * matrix[0].length, (1.0 * mouseX / width) * matrix[0].length);
                    w1 = Math.max((1.0 * x / width) * matrix[0].length, (1.0 * mouseX / width) * matrix[0].length);
                    h1 = Math.max((1.0 * y / height) * matrix.length, (1.0 * mouseY / height) * matrix.length);
                    h0 = Math.min((1.0 * y / height) * matrix.length, (1.0 * mouseY / height) * matrix.length);
                }
                redraw(); // recompute and repaint
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // Methods from MouseMotionListener interface
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            dragX = e.getX();
            dragY = e.getY();
            repaint(); // only repaint - no recomputation
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // Methods from KeyListener interface
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: // reset
                // matrix = (int[][]) hm.toArray();
                matrix = original.clone();
                h0 = 0.0;
                w0 = 0.0;
                h1 = original.length - 1;
                w1 = original[0].length - 1;
                redraw(); // recompute and repaint
                break;
            case KeyEvent.VK_P:
                if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
                    pal = (pal == 0 ? palette.length - 1 : pal - 1);
                } else {
                    pal = (pal + 1) % palette.length;
                }
                redraw(); // recompute and repaint
                break;
            case KeyEvent.VK_L:
                labels = !labels;
                redraw();
                break;
            case KeyEvent.VK_S:
                screenshot();
                break;
            case KeyEvent.VK_Q:
                // quit
                System.exit(0);
            default:
                break;
        }
    }

    public void screenshot() {
        BufferedImage bufferedImage = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                bufferedImage.setRGB(y, x, pixels[x][y].getRGB());
            }
        }
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        String folder = "screenshots/";
        String timed = String.format("%d-%02d-%02d %02d.%02d.%02d", year, month, day, hour, minute, second);
        String fname = folder + timed + ".png";
        try {
            ImageIO.write(bufferedImage, "png", new File(fname));
        } catch (IOException ex) {
            Logger.getLogger(BinaryGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("wrote to " + fname);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int[][] crop(int[][] a, int x0, int y0, int x1, int y1) {
        int[][] cr = new int[x1 - x0 + 1][y1 - y0 + 1];
        for (int i = 0; i < cr.length; i++) {
            for (int j = 0; j < cr[0].length; j++) {
                cr[i][j] = a[x0 + i][y0 + j];
            }
        }
        return cr;
    }

    public double[][] crop(double[][] a, int x0, int y0, int x1, int y1) {
        double[][] cr = new double[x1 - x0 + 1][y1 - y0 + 1];
        for (int i = 0; i < cr.length; i++) {
            for (int j = 0; j < cr[0].length; j++) {
                cr[i][j] = a[x0 + i][y0 + j];
            }
        }
        return cr;
    }

    private interface ColorFunction {
        Color color(double val);
    }

}
