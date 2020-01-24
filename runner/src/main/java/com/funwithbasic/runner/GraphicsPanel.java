package com.funwithbasic.runner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsPanel extends JPanel {

    int sizeOfDot;
    int sizeX;
    int sizeY;

    private BufferedImage bufferedImage = null;

    public GraphicsPanel(int sizeOfDot, int sizeX, int sizeY) {
        this.sizeOfDot = sizeOfDot;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        setPreferredSize(new Dimension(sizeX * sizeOfDot, sizeY * sizeOfDot));
        setVisible(true);
    }

    private void prepareToDraw() {
        if (bufferedImage == null) {
            bufferedImage = (BufferedImage)createImage(sizeX * sizeOfDot, sizeY * sizeOfDot);
            if(bufferedImage == null) {
                Thread.yield();
            }

            Graphics2D gc = bufferedImage.createGraphics();
            gc.setColor(Color.BLACK);
            gc.fillRect(0, 0, sizeX * sizeOfDot, sizeY * sizeOfDot);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        prepareToDraw();
        ((Graphics2D)g).drawImage(bufferedImage, null, 0, 0);
    }

    public void plot(int x, int y, int r, int g, int b) {
        prepareToDraw();
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setColor(new Color(r, g, b));
        g2.fillRect(x * sizeOfDot, y * sizeOfDot, sizeOfDot, sizeOfDot);
        repaint();
    }

    public void fillbox(int x1, int y1, int x2, int y2, int r, int g, int b) {
        prepareToDraw();
        if (x1 == x2 || y1 == y2) {
            return;
        }

        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setColor(new Color(r, g, b));
        int width = Math.abs(x1 - x2) * sizeOfDot;
        int height = Math.abs(y1 - y2) * sizeOfDot;
        int baseX = (x1 < x2 ? x1 : x2) * sizeOfDot;
        int baseY = (y1 < y2 ? y1 : y2) * sizeOfDot;
        g2.fillRect(baseX, baseY, width, height);
        repaint();
    }

}
