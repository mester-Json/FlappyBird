package Flappy;

import java.awt.Color;
import java.awt.Graphics;

public class Bird {
    private int x, y, width, height;
    private int yVelocity;
    private static final int GRAVITY = 1;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.yVelocity = 0;
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
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

