package bomberman.Object.NonMovingObject;


import bomberman.GlobalVariable.SoundVariable;
import javafx.scene.image.Image;

import java.util.ArrayList;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;

import bomberman.Map.PlayGround;

import bomberman.Object.GameObject;
import bomberman.Object.MovingObject.Bomber.Bomber;
import bomberman.Object.MovingObject.MovingObject;
import bomberman.Object.MovingObject.Threats.Enemy;

import javax.sound.sampled.FloatControl;

public class Bomb extends GameObject {
    /**
     * BombPlacer of the bomb.
     */
    private Bomber bombPlacer;

    /**
     * Explosion start time.
     */
    private long plantTime;

    private ArrayList<MovingObject> explodedObject = new ArrayList<>();

    /**
     * Explosion duration.
     */
    private final long EXIST_DURATION = 2000000000; // 2 giây

    public Bomber getBombPlacer() {
        return bombPlacer;
    }

    /**
     * Constructor cho Bomb.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Bomb(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        this.plantTime = System.nanoTime();

        for (Bomber X : this.getCorrespondingPlayGround().getPlayerList()) {
            if (X.isIntersect(this)) {
                explodedObject.add(X);
            }
        }

        for (Enemy X : this.getCorrespondingPlayGround().getEnemyList()) {
            if (X.isIntersect(this)) {
                explodedObject.add(X);
            }
        }

        int tempX = GameVariables.calculateCellIndex(x);
        int tempY = GameVariables.calculateCellIndex(y);

        this.getCorrespondingPlayGround().setBombState(tempY, tempX, true);
    }

    /**
     * Constructor cho Bomb.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param bombPlacer    chủ thể của quả bom
     */
    public Bomb(PlayGround correspondingPlayGround, double x, double y, double width, double length, Bomber bombPlacer) {
        super(correspondingPlayGround, x, y, width, length);

        this.bombPlacer = bombPlacer;
        this.plantTime = System.nanoTime();

        for (Bomber X : this.getCorrespondingPlayGround().getPlayerList()) {
            if (X.isIntersect(this)) {
                explodedObject.add(X);
            }
        }

        for (Enemy X : this.getCorrespondingPlayGround().getEnemyList()) {
            if (X.isIntersect(this)) {
                explodedObject.add(X);
            }
        }

        int tempX = GameVariables.calculateCellIndex(x);
        int tempY = GameVariables.calculateCellIndex(y);

        this.getCorrespondingPlayGround().setBombState(tempY, tempX, true);
    }

    /**
     * Update lại exploded list.
     */
    public void updateExplodedList() {
        for (int i = 0; i < this.getCorrespondingPlayGround().getPlayerList().size(); i++) {
            MovingObject X = this.getCorrespondingPlayGround().getPlayerList().get(i);
            if (explodedObject.contains(X) && !X.isIntersect(this)) {
                explodedObject.remove(X);

                i--;
            }
        }

        for (int i = 0; i < this.getCorrespondingPlayGround().getEnemyList().size(); i++) {
            MovingObject X = this.getCorrespondingPlayGround().getEnemyList().get(i);
            if (explodedObject.contains(X) && !X.isIntersect(this)) {
                explodedObject.remove(X);

                i--;
            }
        }
    }

    /**
     * Kiểm tra đã đến thời gian nổ chưa.
     */
    public boolean checkExplosion() {
        return (System.nanoTime() - plantTime >= EXIST_DURATION);
    }

    /**
     * Check xem bomb có block object này không.
     * (chỉ không block khi bombPlacer mới đặt bomb, khi bombPlacer đi ra khỏi bomb sẽ bị block)
     *
     * @param tempObject object cần check
     * @return có block hoặc không
     */
    public boolean checkBlockStatusWithObject(MovingObject tempObject) {
        return !explodedObject.contains(tempObject);
    }

    /**
     * Kích nổ quả bom.
     */
    public void explodeBomb() {
        int tempX = GameVariables.calculateCellIndex(this.getX());
        int tempY = GameVariables.calculateCellIndex(this.getY());

        // Xóa bomb state
        this.getCorrespondingPlayGround().setBombState(tempY, tempX, false);

        int length = bombPlacer.getFlameLength();
        int side = (int) GameVariables.unitLength;

        // sinh flame ra bên trái bom
        for (int i = tempX - 1; i >= 0 && i >= tempX - length; i--) {
            boolean stopFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getCorrespondingPlayGround().isCellBlocked(tempY, i)) {
                Flame.handleIntersectCell(this.getCorrespondingPlayGround().getCell(tempY, i));

                stopFlame = true;

                if (!(this.getCorrespondingPlayGround().getCell(tempY, i) instanceof Block))  {
                    break;
                }
            }

            if (i == tempX - length) {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), i * side, tempY * side, side, side, Flame.FlameType.LEFT));
            } else {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), i * side, tempY * side, side, side, Flame.FlameType.HORIZONTAL));
            }

            if (stopFlame) {
                break;
            }
        }

        // sinh flame ra bên phải bom
        for (int i = tempX + 1; i <= this.getCorrespondingPlayGround().numberOfColumn() && i <= tempX + length; i++) {
            boolean stopFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getCorrespondingPlayGround().isCellBlocked(tempY, i)) {
                Flame.handleIntersectCell(this.getCorrespondingPlayGround().getCell(tempY, i));

                stopFlame = true;

                if (!(this.getCorrespondingPlayGround().getCell(tempY, i) instanceof Block))  {
                    break;
                }
            }

            if (i == tempX + length) {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), i * side, tempY * side, side, side, Flame.FlameType.RIGHT));
            } else {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), i * side, tempY * side, side, side, Flame.FlameType.HORIZONTAL));
            }

            if (stopFlame) {
                break;
            }
        }

        // sinh flame ra bên trên bom
        for (int i = tempY - 1; i >= 0 && i >= tempY - length; i--) {
            boolean stopFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getCorrespondingPlayGround().isCellBlocked(i, tempX)) {
                Flame.handleIntersectCell(this.getCorrespondingPlayGround().getCell(i, tempX));

                stopFlame = true;

                if (!(this.getCorrespondingPlayGround().getCell(i, tempX) instanceof Block))  {
                    break;
                }
            }

            if (i == tempY - length) {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), tempX * side, i * side, side, side, Flame.FlameType.UP));
            } else {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), tempX * side, i * side, side, side, Flame.FlameType.VERTICAL));
            }

            if (stopFlame) {
                break;
            }
        }

        // sinh flame ra bên dưới bom
        for (int i = tempY + 1; i <= this.getCorrespondingPlayGround().numberOfRow() && i <= tempY + length; i++) {
            boolean stopFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getCorrespondingPlayGround().isCellBlocked(i, tempX)) {
                Flame.handleIntersectCell(this.getCorrespondingPlayGround().getCell(i, tempX));

                stopFlame = true;

                if (!(this.getCorrespondingPlayGround().getCell(i, tempX) instanceof Block))  {
                    break;
                }
            }

            if (i == tempY + length) {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), tempX * side, i * side, side, side, Flame.FlameType.DOWN));
            } else {
                this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), tempX * side, i * side, side, side, Flame.FlameType.VERTICAL));
            }

            if (stopFlame) {
                break;
            }
        }

        //sinh flame ở chính giữa
        this.getCorrespondingPlayGround().addFlame(new Flame(this.getCorrespondingPlayGround(), tempX * side, tempY * side, side, side, Flame.FlameType.CENTER));

        SoundVariable.playSound(FilesPath.ExplosionAudio);
        //FilesPath.ExplosionAudio.setMicrosecondPosition(0);
    }

    @Override
    public Image getImage() {
        return FilesPath.Bomb;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(5);
    }

    @Override
    protected void render(Image displayImage, double renderX, double renderY, double renderWidth, double renderLength) {
        setPosRender(5, 5, -10, -10);

        super.render(displayImage, renderX, renderY, renderWidth, renderLength);
    }
}
