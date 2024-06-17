package Flappy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Pipe {
    private int x, y, width, height;
    private int speed;
    private boolean passed;
    private Image pipeImage;
    private boolean isTopPipe;
    private int displayWidth;

    public Pipe(int x, int y, int width, int height, int speed, boolean isTopPipe, int displayWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.passed = false;
        this.isTopPipe = isTopPipe;
        this.displayWidth = displayWidth;

        pipeImage = new ImageIcon(getClass().getResource("/Flappy/resources/Pipe.png")).getImage();
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        if (isTopPipe) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(pipeImage, x - (displayWidth - width) / 2, y + height, displayWidth, -height, null);
        } else {
            g.drawImage(pipeImage, x - (displayWidth - width) / 2, y, displayWidth, height, null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
