import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

class Game extends JPanel {
    private Timer gameTimer;
    private Snake gameSnake;
    private Point gameCherry;
    private int gamePoints = 0;
    private int gameBest = 0;
    private BufferedImage gameImage;
    private GameState gameStatus;
    private boolean didLoadCherryImage = true;

    private static Font FONT_MEDIUM = new Font("Roboto", Font.PLAIN, 24);
    private static Font FONT_MEDIUM_ITALIC = new Font("Roboto", Font.ITALIC, 24);
    private static Font FONT_LARGE = new Font("MV Boli", Font.PLAIN, 84);
    private static Font FONT_XLARGE = new Font("Roboto", Font.PLAIN, 140);
    private static int GAME_WIDTH = 760;
    private static int GAME_HEIGHT = 520;
    private static int GAME_DELAY = 50;

    // Constructor
    public Game() {
        try {
            gameImage = ImageIO.read(new File("cherry.png"));
        } catch (IOException e) {
            didLoadCherryImage = false;
        }

        addKeyListener(new GameKeyListener());
        setFocusable(true);
        setBackground(new Color(0,206,209));
        setDoubleBuffered(true);

        gameSnake = new Snake(GAME_WIDTH / 2, GAME_HEIGHT / 2);
        gameStatus = GameState.NOT_STARTED;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        render(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {
        gameSnake.move();

        if (gameCherry != null && gameSnake.getHead().intersects(gameCherry, 20)) {
            gameSnake.addTail();
            gameCherry = null;
            gamePoints++;
        }

        if (gameCherry == null) {
            spawnCherry();
        }

        checkForGameOver();
    }
    
    private void reset() {
        gamePoints = 0;
        gameCherry = null;
        gameSnake = new Snake(GAME_WIDTH / 2, GAME_HEIGHT / 2);
        setGameState(GameState.RUNNING);
    }
    
    private void setGameState(GameState newGameState) {
        switch(newGameState) {
            case RUNNING:
                gameTimer = new Timer();
                gameTimer.schedule(new GameLoop(), 0, GAME_DELAY);
                break;
            case PAUSED:
                gameTimer.cancel();
            case GAME_OVER:
                gameTimer.cancel();
                gameBest = gamePoints > gameBest ? gamePoints : gameBest;
                break;
        }

        gameStatus = newGameState;
    }

    private void togglePause() { 
        setGameState(gameStatus == GameState.PAUSED ? GameState.RUNNING : GameState.PAUSED);
    }

    private void checkForGameOver() { 
        Point head = gameSnake.getHead();
        boolean hitBoundary = head.getX() <= 20
            || head.getX() >= GAME_WIDTH + 10
            || head.getY() <= 40
            || head.getY() >= GAME_HEIGHT + 30;

        boolean ateItself = false;

        for(Point t : gameSnake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }

        if (hitBoundary || ateItself) {
            setGameState(GameState.GAME_OVER);
        }
    }

    public void drawCenteredString(Graphics g, String text, Font font, int y) { 
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (GAME_WIDTH - metrics.stringWidth(text)) / 2;

        g.setFont(font);
        g.drawString(text, x, y);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.setFont(FONT_MEDIUM);

        if (gameStatus == GameState.NOT_STARTED) {
          drawCenteredString(g2d, "SNAKE", FONT_XLARGE, 200);
          drawCenteredString(g2d, "GAME", FONT_XLARGE, 300);
          drawCenteredString(g2d, "Press  any  key  to  begin", FONT_MEDIUM_ITALIC, 330);

          return;
        }

        Point p = gameSnake.getHead();

        g2d.drawString("SCORE: " + String.format ("%02d", gamePoints), 20, 30);
        g2d.drawString("BEST: " + String.format ("%02d", gameBest), 630, 30);

        if (gameCherry != null) {
          if (didLoadCherryImage) {
            g2d.drawImage(gameImage, gameCherry.getX(), gameCherry.getY(), 60, 60, null);
          } else {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(gameCherry.getX(), gameCherry.getY(), 10, 10);
            g2d.setColor(Color.BLACK);
          }
        }

        if (gameStatus == GameState.GAME_OVER) {
            drawCenteredString(g2d, "Press  enter  to  start  again", FONT_MEDIUM_ITALIC, 330);
            drawCenteredString(g2d, "GAME OVER", FONT_LARGE, 300);
        }

        if (gameStatus == GameState.PAUSED) {
            g2d.drawString("Paused", 600, 14);
        }

        g2d.setColor(new Color(33, 0, 199));
        g2d.fillRect(p.getX(), p.getY(), 10, 10);

        for(int i = 0, size = gameSnake.getTail().size(); i < size; i++) {
            Point t = gameSnake.getTail().get(i);

            g2d.fillRect(t.getX(), t.getY(), 10, 10);
        }

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(20, 40, GAME_WIDTH, GAME_HEIGHT);
    }

    public void spawnCherry() {
        gameCherry = new Point((new Random()).nextInt(GAME_WIDTH - 60) + 20,
            (new Random()).nextInt(GAME_HEIGHT - 60) + 40);
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (gameStatus == GameState.RUNNING) {
                switch(key) {
                    case KeyEvent.VK_LEFT: gameSnake.turn(GameDirection.LEFT); break;
                    case KeyEvent.VK_RIGHT: gameSnake.turn(GameDirection.RIGHT); break;
                    case KeyEvent.VK_UP: gameSnake.turn(GameDirection.UP); break;
                    case KeyEvent.VK_DOWN: gameSnake.turn(GameDirection.DOWN); break;
                }
            }

            if (gameStatus == GameState.NOT_STARTED) {
                setGameState(GameState.RUNNING);
            }

            if (gameStatus == GameState.GAME_OVER && key == KeyEvent.VK_ENTER) {
                reset();
            }

            if (key == KeyEvent.VK_P) {
                togglePause();
            }
        }
    }

    private class GameLoop extends java.util.TimerTask {
        public void run() {
            update();
            repaint();
        }
    }
}

enum GameState 
{ 
    NOT_STARTED, RUNNING, PAUSED, GAME_OVER
}

enum GameDirection { 
    UP, DOWN, LEFT, RIGHT;
    
    public boolean isX() {
        return this == LEFT || this == RIGHT;
    }
    
    public boolean isY() {
        return this == UP || this == DOWN;
    }
}

class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public void move(GameDirection d, int value) {
        switch(d) {
            case UP: this.y -= value; break;
            case DOWN: this.y += value; break;
            case RIGHT: this.x += value; break;
            case LEFT: this.x -= value; break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point setX(int x) {
        this.x = x;

        return this;
    }

    public Point setY(int y) {
        this.y = y;

        return this;
    }

    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean intersects(Point p) {
        return intersects(p, 10);
    }

    public boolean intersects(Point p, int tolerance) {
        int diffX = Math.abs(x - p.getX());
        int diffY = Math.abs(y - p.getY());

        return this.equals(p) || (diffX <= tolerance && diffY <= tolerance);
    }
}

class Snake {
    private GameDirection direction;
    private Point head;
    private ArrayList<Point> tail;
    
    public Snake(int x, int y) {
        this.head = new Point(x, y);
        this.direction = GameDirection.RIGHT;
        this.tail = new ArrayList<Point>();
        
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
    }

    public void move() {
        ArrayList<Point> newTail = new ArrayList<Point>();
        
        for (int i = 0, size = tail.size(); i < size; i++) {
            Point previous = i == 0 ? head : tail.get(i - 1);

            newTail.add(new Point(previous.getX(), previous.getY()));
        }
        
        this.tail = newTail;
        
        this.head.move(this.direction, 10);
    }
    
    public void addTail() {
        this.tail.add(new Point(-10, -10));
    }
    
    public void turn(GameDirection d) {       
        if (d.isX() && direction.isY() || d.isY() && direction.isX()) {
           direction = d; 
        }       
    }
    
    public ArrayList<Point> getTail() {
        return this.tail;
    }
    
    public Point getHead() {
        return this.head;
    }
}

public class Main extends JFrame {
    public Main() {
        initUI();
    }

    private void initUI() {
        add(new Game());

        setTitle("Snake");
        setSize(800, 610);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}
