package Flappy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int PIPE_WIDTH = 45;
    private static final int INITIAL_PIPE_SPEED = 2;
    private static final int PIPE_GAP_INITIAL = 150;

    private Bird bird;
    private List<Pipe> pipes;
    private Timer timer;
    private int score;
    private int pipeCounter;
    private boolean gameOver;
    private Font scoreFont;
    private int pipeSpeed;
    private int pipeGap;
    private Image backgroundImage;

    public GamePanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth, screenHeight));

        bird = new Bird(100, 300);
        pipes = new ArrayList<>();
        score = 0;
        pipeCounter = 0;
        gameOver = false;
        scoreFont = new Font("Arial", Font.BOLD, screenHeight / 30); 
        pipeSpeed = INITIAL_PIPE_SPEED;
        pipeGap = PIPE_GAP_INITIAL;

        backgroundImage = new ImageIcon(getClass().getResource("/Flappy/background.png")).getImage();

        timer = new Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    update();
                    repaint();
                }
            }
        });
        timer.start();

        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bird.flap();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        render(g);
    }

    public void render(Graphics g) {
        bird.draw(g);
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(scoreFont);
        g.drawString("Score: " + score, 10, 30);
    }

    public void update() {
        bird.update();

        List<Pipe> pipesToRemove = new ArrayList<>();
        for (Pipe pipe : pipes) {
            pipe.update();
            if (bird.intersects(pipe)) {
                gameOver();
                return;
            }
            if (pipe.getX() + PIPE_WIDTH < bird.getX() && !pipe.isPassed()) {
                pipe.setPassed(true);
                score++;
            }
            if (pipe.getX() + PIPE_WIDTH < 0) {
                pipesToRemove.add(pipe);
            }
        }

        pipes.removeAll(pipesToRemove);

        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < getWidth() - 300) {
            generatePipes();
        }
    }

    private void generatePipes() {
        if (pipeCounter % 2 == 0) {
            int randomGap = (int) (Math.random() * (pipeGap - 50)) + 50;
            int randomHeight = (int) (Math.random() * (getHeight() - pipeGap - 100)) + 100;
            int displayWidth = PIPE_WIDTH *7;


            pipes.add(new Pipe(getWidth(), 0, PIPE_WIDTH, randomHeight, pipeSpeed, true, displayWidth));  
            pipes.add(new Pipe(getWidth(), randomHeight + pipeGap, PIPE_WIDTH, getHeight() - randomHeight - pipeGap, pipeSpeed, false, displayWidth));  
        }
        pipeCounter++;
    }


    private void gameOver() {
        gameOver = true;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        bird = new Bird(100, 300);
        pipes.clear();
        score = 0;
        pipeCounter = 0;
        gameOver = false;
        pipeSpeed = INITIAL_PIPE_SPEED;
        pipeGap = PIPE_GAP_INITIAL;
        timer.start();
    }
}
