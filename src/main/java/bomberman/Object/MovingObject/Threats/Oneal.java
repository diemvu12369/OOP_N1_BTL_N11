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
    private boolean headingRight = false;
    private boolean headingLeft = false;
    private boolean headingUp = false;
    private boolean headingDown = false;

    /**
     * Thời gian di chuyển theo hướng hiện tại
     */
    private final long duration = 250000000; // 0.25 giây
    private final long speedChangeMoment = duration * 8;
    private long plantTime = System.nanoTime();
    private long plantTimeSp = System.nanoTime();
    /**
     * Constructor cho Oneal.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Oneal(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
    }

    /**
     * Constructor cho Oneal.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Oneal(PlayGround correspondingPlayGround, double x, double y) {
        super(correspondingPlayGround, x, y);
    }

    @Override
    public Image getImage() {
        return FilesPath.Oneal;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(4);
    }

    /**
     * Tìm đường đi tốt nhất cho enemy chạy tới player.
     *
     * @param playerX chỉ số ô đang đứng x của player
     * @param playerY chỉ số ô đang đứng y của player
     */
    public void bestWay(int playerX, int playerY) {
        int enemyX = GameVariables.calculateCellIndex(this.getXCenter());
        int enemyY = GameVariables.calculateCellIndex(this.getYCenter());

        boolean[][] used = new boolean[110][110];

        Pair<Integer, Integer>[][] tracePath = new Pair[110][110];

        int[] deltaX = {0, 0, -1, 1};
        int[] deltaY = {1, -1, 0, 0};

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        Pair<Integer, Integer> p;

        p = new Pair<>(enemyY, enemyX);

        used[enemyY][enemyX] = true;

        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == playerX && y == playerY) {
                for (int i = 1; i < this.getCorrespondingPlayGround().numberOfRow(); i++) {
                    for (int j = 1; j < this.getCorrespondingPlayGround().numberOfColumn(); j++) {
                        state[i][j] = 0;
                    }
                }

                int tempX = playerX;
                int tempY = playerY;

                while (tempX != enemyX || tempY != enemyY) {
                    state[tempY][tempX] = 1;

                    p = tracePath[tempY][tempX];

                    tempY = p.getKey();
                    tempX = p.getValue();
                }

                state[tempY][tempX] = 1;

                ok = true;

                return;
            }

            p = new Pair<>(y, x);

            for (int i = 0; i <= 3; i++) {
                int newX = x + deltaX[i];
                int newY = y + deltaY[i];

                if (newX < 0 || newX >= this.getCorrespondingPlayGround().numberOfColumn() ||
                        newY < 0 || newY >= this.getCorrespondingPlayGround().numberOfRow()) {
                    continue;
                }

                if (!this.getCorrespondingPlayGround().isCellBlocked(newY, newX) &&
                        !this.getCorrespondingPlayGround().getBombState(newY, newX) && !used[newY][newX]) {
                    used[newY][newX] = true;
                    tracePath[newY][newX] = p;

                    Pair<Integer, Integer> newP = new Pair<>(newY, newX);

                    q.add(newP);
                }
            }
        }

        ok = false;
    }

    @Override
    public void move() {
        if (System.nanoTime() - plantTimeSp >= speedChangeMoment) {
            double randomlyDouble = Math.random();
            randomlyDouble = randomlyDouble * 1000 + 1;
            int randomlyInt = (int) randomlyDouble;
            randomlyInt = (randomlyInt % 4) + 1;
            this.setSpeed((double) randomlyInt);
            plantTimeSp = System.nanoTime();
        }
        if (System.nanoTime() - plantTime >= duration) {
            int playerX = GameVariables.calculateCellIndex(this.getCorrespondingPlayGround().getPlayerList().get(0).getXCenter());
            int playerY = GameVariables.calculateCellIndex(this.getCorrespondingPlayGround().getPlayerList().get(0).getYCenter());
            int enemyX = GameVariables.calculateCellIndex(this.getXCenter());
            int enemyY = GameVariables.calculateCellIndex(this.getYCenter());

            this.bestWay(playerX, playerY);

            if (this.ok) {
                headingDown = false;
                headingUp = false;
                headingLeft = false;
                headingRight = false;

                int[] deltaX = {0, 0, -1, 1};
                int[] deltaY = {1, -1, 0, 0};

                for (int i = 0; i <= 3; i++) {
                    int newX = enemyX + deltaX[i];
                    int newY = enemyY + deltaY[i];

                    if (newX < 1 || newX >= this.getCorrespondingPlayGround().numberOfColumn() ||
                            newY < 1 || newY > this.getCorrespondingPlayGround().numberOfRow()) {
                        continue;
                    }

                    if (state[newY][newX] == 1) {
                        if (i == 0) {
                            headingDown = true;
                        } else {
                            if (i == 1) {
                                headingUp = true;
                            } else if (i == 2) {
                                headingLeft = true;
                            } else {
                                headingRight = true;

                            }
                        }
                    }
                }

                state[enemyY][enemyX] = 0;

                setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT, headingRight);
                setDirectionOfObject(MovingObject.DirectionOfObject.LEFT, headingLeft);
                setDirectionOfObject(MovingObject.DirectionOfObject.UP, headingUp);
                setDirectionOfObject(MovingObject.DirectionOfObject.DOWN, headingDown);
                this.randomMove = false;
            } else {
                this.randomMove = true;
            }
            plantTime = System.nanoTime();
        }

        super.move();
    }

    public void dead() {
         SoundVariable.playSound(FilesPath.OnealDieAudio);
    }
}
