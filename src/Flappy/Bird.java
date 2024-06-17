package Flappy;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Bird {
    private int x, y, width, height;
    private int yVelocity;
    private static final int GRAVITY = 1;
    private Image birdImage;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 70;
        this.height = 70;
        this.yVelocity = 0;

        birdImage = new ImageIcon(getClass().getResource("/Flappy/resources/Bird.png")).getImage();
    }

    public void update() {
        yVelocity += GRAVITY;
        y += yVelocity;
        
        if (y > 570) { 
            y = 570;
            yVelocity = 0;
        }
    }

    public void flap() {
        yVelocity = -12;
    }

    public void draw(Graphics g) {
        g.drawImage(birdImage, x, y, width, height, null);
    }

    public boolean intersects(Pipe pipe) {
        return x < pipe.getX() + pipe.getWidth() && 
               x + width > pipe.getX() &&
               y < pipe.getY() + pipe.getHeight() && 
               y + height > pipe.getY();
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
}
