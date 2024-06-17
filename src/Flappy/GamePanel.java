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
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


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
    private Clip clickSound;
    private Clip scoreSound;

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

        backgroundImage = new ImageIcon(getClass().getResource("/Flappy/resources/background.png")).getImage();

        try {
            AudioInputStream audioInClick = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/Flappy/resources/sfx_wing.wav"));
            clickSound = AudioSystem.getClip();
            clickSound.open(audioInClick);

            AudioInputStream audioInScore = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/Flappy/resources/sfx_point.wav"));
            scoreSound = AudioSystem.getClip();
            scoreSound.open(audioInScore);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

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
                playClickSound();
            }
        });
    }

    private void playClickSound() {
        if (clickSound != null) {
            if (clickSound.isRunning()) {
                clickSound.stop();
            }
            clickSound.setFramePosition(0);
            clickSound.start();
        }
    }

    private void playScoreSound() {
        if (scoreSound != null) {
            if (scoreSound.isRunning()) {
                scoreSound.stop();
            }
            scoreSound.setFramePosition(0);
            scoreSound.start();
        }
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
        
        if (bird.getY() > getHeight() || bird.getY() < 0) {
            gameOver();
            return;
        }

        List<Pipe> pipesToRemove = new ArrayList<>();
        for (int i = 0; i < pipes.size(); i += 2) {
            Pipe topPipe = pipes.get(i);
            Pipe bottomPipe = pipes.get(i + 1);

            topPipe.update();
            bottomPipe.update();

            if (bird.intersects(topPipe) || bird.intersects(bottomPipe)) {
                gameOver();
                return;
            }

            if (topPipe.getX() + PIPE_WIDTH < bird.getX() && !topPipe.isPassed()) {
                topPipe.setPassed(true);
                bottomPipe.setPassed(true);
                score++;
                playScoreSound();
            }

            if (topPipe.getX() + PIPE_WIDTH < 0) {
                pipesToRemove.add(topPipe);
                pipesToRemove.add(bottomPipe);
            }
        }

        pipes.removeAll(pipesToRemove);

        if (pipes.isEmpty() || pipes.get(pipes.size() - 2).getX() < getWidth() - 300) {
            generatePipes();
        }
    }

    private void generatePipes() {
        if (pipeCounter % 2 == 0) {
            int randomGap = (int) (Math.random() * (pipeGap - 50)) + 50;
            int randomHeight = (int) (Math.random() * (getHeight() - pipeGap - 100)) + 100;
            int displayWidth = PIPE_WIDTH * 7;

            pipes.add(new Pipe(getWidth(), 0, PIPE_WIDTH, randomHeight, pipeSpeed, true, displayWidth));
            pipes.add(new Pipe(getWidth(), randomHeight + pipeGap, PIPE_WIDTH, getHeight() - randomHeight - pipeGap,
                    pipeSpeed, false, displayWidth));
        }
        pipeCounter++;
    }


    private void gameOver() {
        gameOver = true;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score, "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
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