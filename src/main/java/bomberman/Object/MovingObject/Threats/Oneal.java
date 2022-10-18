package bomberman.Object.MovingObject.Threats;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;
import bomberman.GlobalVariable.SoundVariable;
import bomberman.Map.PlayGround;
import bomberman.Object.MovingObject.MovingObject;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class Oneal extends Enemy {
    /**
     * Trạng thái đường đi của object.
     */
    private int[][] state = new int[50][50];

    /**
     * Trạng thái di chuyển của enemy.
     */
    private boolean ok = false;
    private boolean toRight = false;
    private boolean toLeft = false;
    private boolean toUp = false;
    private boolean toDown = false;

    /**
     * Thời gian di chuyển theo hướng hiện tại
     */
    private final long duration1 = 250000000; // 0.25 giây
    private final long timeChangeSpeed1 = duration1 * 8;
    private long startTime1 = System.nanoTime();
    private long startTimeSp = System.nanoTime();
    /**
     * Constructor cho Oneal.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Oneal(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
    }

    /**
     * Constructor cho Oneal.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Oneal(PlayGround belongTo, double x, double y) {
        super(belongTo, x, y);
    }

    @Override
    public Image getImage() {
        return FilesPath.Oneal;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(4);
    }

    /**
     * Tìm đường đi tốt nhất cho enemy chạy tới player.
     *
     * @param xPlayer chỉ số ô đang đứng x của player
     * @param yPlayer chỉ số ô đang đứng y của player
     */
    public void findBestWay(int xPlayer, int yPlayer) {
        int xEnemy = GameVariables.calculateCellIndex(this.getCenterX());
        int yEnemy = GameVariables.calculateCellIndex(this.getCenterY());

        boolean[][] dx = new boolean[110][110];

        Pair<Integer, Integer>[][] tr = new Pair[110][110];

        int[] c = {0, 0, -1, 1};
        int[] d = {1, -1, 0, 0};

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        Pair<Integer, Integer> p;

        p = new Pair<>(yEnemy, xEnemy);

        dx[yEnemy][xEnemy] = true;

        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == xPlayer && y == yPlayer) {
                for (int i = 1; i < this.getBelongTo().getNumberOfRow(); i++) {
                    for (int j = 1; j < this.getBelongTo().getNumberOfColumn(); j++) {
                        state[i][j] = 0;
                    }
                }

                int tmpX = xPlayer;
                int tmpY = yPlayer;

                while (tmpX != xEnemy || tmpY != yEnemy) {
                    state[tmpY][tmpX] = 1;

                    p = tr[tmpY][tmpX];

                    tmpY = p.getKey();
                    tmpX = p.getValue();
                }

                state[tmpY][tmpX] = 1;

                ok = true;

                return;
            }

            p = new Pair<>(y, x);

            for (int i = 0; i <= 3; i++) {
                int newX = x + c[i];
                int newY = y + d[i];

                if (newX < 0 || newX >= this.getBelongTo().getNumberOfColumn() ||
                        newY < 0 || newY >= this.getBelongTo().getNumberOfRow()) {
                    continue;
                }

                if (!this.getBelongTo().isBlockCell(newY, newX) &&
                        !this.getBelongTo().getStateBomb(newY, newX) && !dx[newY][newX]) {
                    dx[newY][newX] = true;
                    tr[newY][newX] = p;

                    Pair<Integer, Integer> pp = new Pair<>(newY, newX);

                    q.add(pp);
                }
            }
        }

        ok = false;
    }

    @Override
    public void move() {
        if (System.nanoTime() - startTimeSp >= timeChangeSpeed1) {
            double randomDouble = Math.random();
            randomDouble = randomDouble * 1000 + 1;
            int randomInt = (int) randomDouble;
            randomInt = (randomInt % 4) + 1;
            this.setSpeed((double) randomInt);
            startTimeSp = System.nanoTime();
        }
        if (System.nanoTime() - startTime1 >= duration1) {
            int xPlayer = GameVariables.calculateCellIndex(this.getBelongTo().getPlayers().get(0).getCenterX());
            int yPlayer = GameVariables.calculateCellIndex(this.getBelongTo().getPlayers().get(0).getCenterY());
            int xEnemy = GameVariables.calculateCellIndex(this.getCenterX());
            int yEnemy = GameVariables.calculateCellIndex(this.getCenterY());

            this.findBestWay(xPlayer, yPlayer);

            if (this.ok) {
                toDown = false;
                toUp = false;
                toLeft = false;
                toRight = false;

                int[] c = {0, 0, -1, 1};
                int[] d = {1, -1, 0, 0};

                for (int i = 0; i <= 3; i++) {
                    int newX = xEnemy + c[i];
                    int newY = yEnemy + d[i];

                    if (newX < 1 || newX >= this.getBelongTo().getNumberOfColumn() ||
                            newY < 1 || newY > this.getBelongTo().getNumberOfRow()) {
                        continue;
                    }

                    if (state[newY][newX] == 1) {
                        if (i == 0) {
                            toDown = true;
                        } else {
                            if (i == 1) {
                                toUp = true;
                            } else if (i == 2) {
                                toLeft = true;
                            } else {
                                toRight = true;

                            }
                        }
                    }
                }

                state[yEnemy][xEnemy] = 0;

                setObjectDirection(MovingObject.ObjectDirection.RIGHT_, toRight);
                setObjectDirection(MovingObject.ObjectDirection.LEFT_, toLeft);
                setObjectDirection(MovingObject.ObjectDirection.UP_, toUp);
                setObjectDirection(MovingObject.ObjectDirection.DOWN_, toDown);
                this.moveRandom = false;
            } else {
                this.moveRandom = true;
            }
            startTime1 = System.nanoTime();
        }

        super.move();
    }

    public void die() {
         SoundVariable.playSound(FilesPath.OnealDieAudio);
    }
}
