import com.example.server.GameResult.GameResult;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Game extends JPanel implements ActionListener {

    private Dimension dimension;
    private final Font font = new Font("Arial", Font.BOLD, 14);
    private final Font bigFont = new Font("Arial", Font.BOLD, 17);
    private boolean inGame = false;
    private boolean dead = false;
    private final int blockSize = 24;
    private final int numBlocks = 20;
    private final int windowSize = numBlocks * blockSize;
    boolean protection = false;


    private int score = 0, dotsAmount;
    private int pointsInARow = 0;
    private int theBestScore;
    private int takenDots = 0;
    private int lives = 3;
    private int[] dx, dy;
    private int[] enemyX, enemyY, enemyDx, enemyDy, enemySpeed;
    private int superEnemyX, superEnemyY, superEnemyDx, superEnemyDy;
    private Stack<Integer> superEnemyPath;
    public ScheduledExecutorService service;

    private Image heart, brokenHeart, enemy, superEnemy;
    private Image pacman, protectedPacman;

    private int pacmanX, pacmanY, pacmanDx, pacmanDy;
    private int changeX, changeY;
    private Node[][] nodeArray;


    private int currentSpeed = 4;
    private int[] screenData;
    private Timer timer;
    private GameClient gameClient = new GameClient(WebClient.create("http://localhost:8080/api/v1/result"));

    private final int[] levelData = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1,
            1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1,
            1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };


    public Game() {
        loadImages();
        try {
            initVariables();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addKeyListener(new ControlAdapter());
        setFocusable(true);
        createMap();
    }


    public void loadImages() {
        pacman = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/normalPacman.png").getImage();
        protectedPacman = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/icons8-pacman-50.png").getImage();
        enemy = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/easyGhost.png").getImage();
        superEnemy = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/superGhost.png").getImage();
        heart = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/heart.png").getImage();
        brokenHeart = new ImageIcon("/Users/macbook/Downloads/Pac-Man_App/src/main/resources/broken-heart.png").getImage();
    }

    public void initVariables() throws InterruptedException {
        screenData = new int[numBlocks * numBlocks];
        dimension = new Dimension(480, 540);
        inGame = true;
        superEnemyPath = new Stack<>();
        enemyX = new int[4];
        enemyY = new int[4];
        enemyDx = new int[4];
        enemyDy = new int[4];
        enemySpeed = new int[]{2,3,4,4};
        dx = new int[]{0,0,1,-1};
        dy = new int[]{1,-1,0,0};
        nodeArray = createNodeArray();
        findPathForSuperEnemy();
        setHeroes();
        dotsAmount = getNumberOfDots();

        timer = new Timer(20,this);
        timer.start();

        service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(this::stopProtection,3, TimeUnit.SECONDS);
        theBestScore = gameClient.getTheBestResult();
        gameClient.addNewResult(createGameResult());
    }

    public void createMap() {
        for (int i = 0; i < levelData.length; i++) {
            screenData[i] = levelData[i];
        }
    }

    public void setHeroes() {
        pacmanX = 3 * blockSize;
        pacmanY = 3 * blockSize;
        superEnemyX = 8 * blockSize;
        superEnemyY = 8 * blockSize;
        setEnemies();
    }

    public void setHearts(Graphics2D g) {
        int rowDepth = blockSize * 20 + 3;
        int HeartCol = 15;
        if (lives == 3) {
            g.drawImage(heart, HeartCol, rowDepth, this);
            g.drawImage(heart, HeartCol + blockSize, rowDepth, this);
            g.drawImage(heart, HeartCol + blockSize * 2, rowDepth, this);
        } else if (lives == 2) {
            g.drawImage(heart, HeartCol, rowDepth, this);
            g.drawImage(heart, HeartCol + blockSize, rowDepth, this);
            g.drawImage(brokenHeart, HeartCol + blockSize * 2, rowDepth, this);
        } else if (lives == 1) {
            g.drawImage(heart, HeartCol, rowDepth, this);
            g.drawImage(brokenHeart, HeartCol + blockSize, rowDepth, this);
            g.drawImage(brokenHeart, HeartCol + blockSize * 2, rowDepth, this);
        } else {
            g.drawImage(brokenHeart, HeartCol, rowDepth, this);
            g.drawImage(brokenHeart, HeartCol + blockSize, rowDepth, this);
            g.drawImage(brokenHeart, HeartCol + blockSize * 2, rowDepth, this);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(243, 161, 127));
        g2d.fillRect(0, 0, dimension.width, dimension.height);

        drawMaze(g2d);
        setHearts(g2d);
        checkIfKilled();

        if (takenDots == dotsAmount) {
            dead = true;
            addExtraScore();
            takenDots--;
        }
        if (dead) {
            inGame = false;
            showTheEndScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void showTheEndScreen(Graphics2D g) {
        String score = "Your score is:  " + this.score;
        if (takenDots + 1 == dotsAmount) {
            String win = "Congratulations you win";
            g.setFont(bigFont);
            g.drawString(win, (windowSize) / 2 - 80, 210);
        }
        String again = "Press SPACE to start again";
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(score, (windowSize) / 2 - 65, 240);
        g.drawString(again, (windowSize) / 2 - 90, 260);
    }

    public void drawMaze(Graphics2D g2d) {
        int i = 0;
        g2d.setColor(new Color(243, 184, 158));
        g2d.setStroke(new BasicStroke(5));

        for (int y = 0; y < windowSize; y += blockSize) {

            for (int x = 0; x < windowSize; x += blockSize) {
                g2d.setColor(new Color(243, 184, 158));
                g2d.setStroke(new BasicStroke(5));
                if (screenData[i] == 1) {
                    g2d.fillRect(x, y, blockSize, blockSize);
                } else if (screenData[i] != 1) {
                    g2d.setColor(new Color(255, 215, 196));
                    g2d.fillRect(x, y, blockSize, blockSize);
                }

                if (screenData[i] == 0) {
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);

                }

                i++;
            }
        }
        movePacMan();
        drawPacMan(g2d);
        moveEnemies();
        moveSuperEnemy();
        drawEnemies(g2d);
        drawScore(g2d);
    }

    public void drawPacMan(Graphics g) {
        if (!protection) {
            g.drawImage(pacman, pacmanX, pacmanY, this);
        } else {
            g.drawImage(protectedPacman, pacmanX, pacmanY, this);
        }
    }

    public void movePacMan() {
        int pos;
        if (pacmanX % blockSize == 0 && pacmanY % blockSize == 0) {
            pos = pacmanX / blockSize + numBlocks * (pacmanY / blockSize);
            pacmanDx = changeX;
            pacmanDy = changeY;

            if ((pacmanDx == 1 && pos < 399 && screenData[pos + 1] == 1 && (pos + 1) % 20 != 0) ||
                    (pacmanDx == -1 && pos > 0 && screenData[pos - 1] == 1 && pos % 20 != 0) ||
                    (pacmanDy == 1 && pos < 380 && screenData[pos + 20] == 1) ||
                    (pacmanDy == -1 && pos > 20 && screenData[pos - 20] == 1)) {

                pacmanDy = 0;
                pacmanDx = 0;
            }
        }
        pacmanX = pacmanX + pacmanDx * currentSpeed;
        pacmanY = pacmanY + pacmanDy * currentSpeed;

        if (pacmanX % blockSize == 0 && pacmanY % blockSize == 0) {
            pos = pacmanX / blockSize + numBlocks * (pacmanY / blockSize);
            if (screenData[pos] == 0) {
                screenData[pos] = 2;
                pointsInARow++;
                takenDots++;
                calculateScore();
            }
        }

        if (pacmanX < 0) {
            pacmanX = windowSize;
        } else if (pacmanX > windowSize) {
            pacmanX = 0;
        } else if (pacmanY < 0) {
            pacmanY = windowSize - blockSize;
        } else if (pacmanY > windowSize - blockSize) {
            pacmanY = 0;
        }
    }

    private void calculateScore() {
        int percentage = Math.round(100 * pointsInARow / dotsAmount);

        if (percentage < 10) {
            score++;
        } else if (percentage < 20) {
            score += 2;
        } else if (percentage < 40) {
            score += 4;
        } else if (percentage < 60) {
            score += 6;
        } else if (percentage < 80) {
            score += 10;
        } else {
            score += 15;
        }
    }

    private void addExtraScore() {
        for (int i = 0; i < lives; i++) {
            score += 100;
        }
    }

    private void drawScore(Graphics g) {
        g.setFont(font);
        g.setColor(new Color(112, 112, 112));
        String s = "Score: " + score;
        g.drawString(s, windowSize / 2 + 150, windowSize + 20);

        g.setColor(new Color(82, 81, 81));
        g.setFont(new Font("Arial", Font.BOLD, 15));
        String best = "The best score: " + Math.max(theBestScore, score);
        g.drawString(best, windowSize / 3 + 8, windowSize + 21);
    }

    public void setEnemies() {
        enemyX[0] = 3 * blockSize ;
        enemyY[0] = 16 * blockSize;
        enemyX[1] = 16 * blockSize;
        enemyY[1] = 16 * blockSize;
        enemyX[2] = 16 * blockSize;
        enemyY[2] = 3 * blockSize;
        enemyX[3] = 12 * blockSize;
        enemyY[3] = 11 * blockSize;
    }

    public void drawEnemies(Graphics g) {
        for (int i = 0; i < enemyX.length; i++) {
            g.drawImage(enemy, enemyX[i], enemyY[i], this);
        }
        g.drawImage(superEnemy, superEnemyX, superEnemyY, this);
    }

    public void moveEnemies() {
        Random random = new Random();
        int pos;
        for (int i = 0; i < enemyX.length; i++) {

            if (enemyX[i] % blockSize == 0 && enemyY[i] % blockSize == 0) {
                pos = enemyX[i] / blockSize + numBlocks * (enemyY[i] / blockSize);

                if ((enemyDx[i] == 1 && pos < 399 && screenData[pos + 1] == 1) ||
                        (enemyDx[i] == -1 && pos > 0 && screenData[pos - 1] == 1 || (pos - 1) % 20 == 0) ||
                        (enemyDy[i] == 1 && pos < 380 && screenData[pos + 20] == 1) ||
                        (enemyDy[i] == -1 && pos > 20 && screenData[pos - 20] == 1) || (enemyDx[i] == 0 && enemyDy[i] == 0)) {
                    setGhostDirection(i, pos);

                } else {
                    int randomDirection = random.nextInt(7);
                    if (randomDirection <= 2) {
                        setGhostDirection(i, pos);
                    }
                }
            }

            enemyX[i] = enemyX[i] + enemyDx[i] * enemySpeed[i];
            enemyY[i] = enemyY[i] + enemyDy[i] * enemySpeed[i];

            if (enemyX[i] < 0) {
                enemyX[i] = windowSize;
            } else if (enemyX[i] > windowSize) {
                enemyX[i] = 0;
            } else if (enemyY[i] < 0) {
                enemyY[i] = windowSize - blockSize;
            } else if (enemyY[i] > windowSize - blockSize) {
                enemyY[i] = 0;
            }
        }
    }


    public void setGhostDirection(int i, int pos) {
        Random random = new Random();
        java.util.List<Integer> list = new ArrayList<>();
        if (pos >= 1 && screenData[pos - 1] != 1) {
            list.add(3);
        }
        if (pos < 399 && screenData[pos + 1] != 1) {
            list.add(2);
        }
        if (pos < 380 && screenData[pos + 20] != 1) {
            list.add(0);
        }
        if (pos >= 20 && screenData[pos - 20] != 1) {
            list.add(1);
        }
        if (list.size() == 0) {
            enemyDx[i] = 0;
            enemyDy[i] = 0;
            return;
        }
        int num = random.nextInt(list.size());
        enemyDx[i] = dx[list.get(num)];
        enemyDy[i] = dy[list.get(num)];
    }

    public void findPathForSuperEnemy() {
        Node start = new Node(superEnemyX / blockSize, superEnemyY / blockSize);
        Node goal = new Node(pacmanX / blockSize, pacmanY / blockSize);
        if (start.col != goal.col || start.row != goal.row) {
            nodeArray = createNodeArray();
            this.superEnemyPath = ShortestPath.aStar(nodeArray, start, goal);
        }
    }

    public void moveSuperEnemy() {
        findPathForSuperEnemy();
        if (superEnemyPath.isEmpty() && (superEnemyX % blockSize == 0 && superEnemyY % blockSize == 0)) {
            superEnemyDx = 0;
            superEnemyDy = 0;
        }
        if (!superEnemyPath.isEmpty() && (superEnemyX % blockSize == 0 && superEnemyY % blockSize == 0)) {
            int index = superEnemyPath.pop();
            superEnemyDx = dx[index];
            superEnemyDy = dy[index];
        }

        int pos = superEnemyX / blockSize + numBlocks * (int) (superEnemyY / blockSize);

        if (superEnemyX % blockSize == 0 && superEnemyY % blockSize == 0) {
            if ((superEnemyDx == 1 && pos < 399 && (screenData[pos + 1] == 1)) ||
                    (superEnemyDx == -1 && pos > 0 && screenData[pos - 1] == 1) ||
                    (superEnemyDy == 1 && pos < 380 && screenData[pos + 20] == 1) ||
                    (superEnemyDy == -1 && pos > 20 && screenData[pos - 20] == 1)) {
                superEnemyDx = 0;
                superEnemyDy = 0;
            }
        }
        superEnemyX = superEnemyX + superEnemyDx * 2;
        superEnemyY  = superEnemyY + superEnemyDy * 2;

    }

    public GameResult createGameResult() {
        return new GameResult(LocalDate.now(),score,lives);
    }



    class ControlAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    changeX = -1;
                    changeY = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    changeX = 1;
                    changeY = 0;
                } else if (key == KeyEvent.VK_UP) {
                    changeX = 0;
                    changeY = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    changeX = 0;
                    changeY = 1;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    startGame();
                }
            }
        }

        private void startGame() {
            if (theBestScore < score) {
                GameResult gameResult = createGameResult();
                gameClient.addNewResult(gameResult);
                theBestScore = score;
            }
            lives = 3;
            score = 0;
            createMap();
            setHeroes();
            dead = false;
            pointsInARow = 0;
            takenDots = 0;
        }
    }

    public Node[][] createNodeArray() {
        Node[][] arr = new Node[20][20];
        int index = 0;
        for (int i = 0; i < numBlocks; i++) {
            for (int j = 0; j < numBlocks; j++) {
                Node node = new Node(j, i);
                node.obstacle = levelData[index];
                if ((index + 1) % 20 == 0 || index % 20 == 0
                        || index > numBlocks * numBlocks - numBlocks ) {
                    node.obstacle = 1;
                }
                arr[j][i] = node;
                index++;
            }
        }
        return arr;
    }

    public int getNumberOfDots() {
        int num = 0;
        for (int i = 0; i < numBlocks * numBlocks; i++) {
            if (levelData[i] == 0) {
                num++;
            }
        }
        return num;
    }

    public void checkIfKilled() {
        int pacmanPosition = pacmanX / blockSize + numBlocks * (pacmanY / blockSize);
        if (!protection) {
            for (int i = 0; i < enemyX.length; i++) {
                int pos = enemyX[i] / blockSize + numBlocks * (enemyY[i] / blockSize);
                if (pos == pacmanPosition) {
                    lives--;
                    pointsInARow = 0;
                    if (lives == 0) {
                        dead = true;
                    }
                    protection = true;
                    service.schedule(this::stopProtection, 3, TimeUnit.SECONDS);
                    return;
                }
            }
            int pos = superEnemyX / blockSize + numBlocks * (superEnemyY / blockSize);
            if (pos == pacmanPosition) {
                lives--;
                pointsInARow = 0;
                if (lives == 0) {
                    dead = true;
                }
                protection = true;
                service.schedule(this::stopProtection, 3, TimeUnit.SECONDS);
            }
        }
    }

    public  void stopProtection() {
        protection = false;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
