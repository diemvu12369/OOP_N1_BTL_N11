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
     * Owner of the bomb.
     */
    private Bomber owner;

    /**
     * Explosion start time.
     */
    private long startTime;

    private ArrayList<MovingObject> unblockObject = new ArrayList<>();

    /**
     * Explosion duration.
     */
    private final long EXISTENCE_DURATION = 2000000000; // 2 giây

    public Bomber getOwner() {
        return owner;
    }

    /**
     * Constructor cho Bomb.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Bomb(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);

        this.startTime = System.nanoTime();

        for (Bomber X : this.getBelongTo().getPlayers()) {
            if (X.checkIntersect(this)) {
                unblockObject.add(X);
            }
        }

        for (Enemy X : this.getBelongTo().getEnemies()) {
            if (X.checkIntersect(this)) {
                unblockObject.add(X);
            }
        }

        int tempX = GameVariables.calculateCellIndex(x);
        int tempY = GameVariables.calculateCellIndex(y);

        this.getBelongTo().setStateBomb(tempY, tempX, true);
    }

    /**
     * Constructor cho Bomb.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param owner    chủ thể của quả bom
     */
    public Bomb(PlayGround belongTo, double x, double y, double width, double length, Bomber owner) {
        super(belongTo, x, y, width, length);

        this.owner = owner;
        this.startTime = System.nanoTime();

        for (Bomber X : this.getBelongTo().getPlayers()) {
            if (X.checkIntersect(this)) {
                unblockObject.add(X);
            }
        }

        for (Enemy X : this.getBelongTo().getEnemies()) {
            if (X.checkIntersect(this)) {
                unblockObject.add(X);
            }
        }

        int tempX = GameVariables.calculateCellIndex(x);
        int tempY = GameVariables.calculateCellIndex(y);

        this.getBelongTo().setStateBomb(tempY, tempX, true);
    }

    /**
     * Update lại unblock list.
     */
    public void updateUnblockList() {
        for (int i = 0; i < this.getBelongTo().getPlayers().size(); i++) {
            MovingObject X = this.getBelongTo().getPlayers().get(i);
            if (unblockObject.contains(X) && !X.checkIntersect(this)) {
                unblockObject.remove(X);

                i--;
            }
        }

        for (int i = 0; i < this.getBelongTo().getEnemies().size(); i++) {
            MovingObject X = this.getBelongTo().getEnemies().get(i);
            if (unblockObject.contains(X) && !X.checkIntersect(this)) {
                unblockObject.remove(X);

                i--;
            }
        }
    }

    /**
     * Kiểm tra đã đến thời gian nổ chưa.
     */
    public boolean checkExplode() {
        return (System.nanoTime() - startTime >= EXISTENCE_DURATION);
    }

    /**
     * Check xem bomb có block object này không.
     * (chỉ không block khi owner mới đặt bomb, khi owner đi ra khỏi bomb sẽ bị block)
     *
     * @param tempObject object cần check
     * @return có block hoặc không
     */
    public boolean checkBlockStatusWithObject(MovingObject tempObject) {
        return !unblockObject.contains(tempObject);
    }

    /**
     * Kích nổ quả bom.
     */
    public void detonateBomb() {
        int tempX = GameVariables.calculateCellIndex(this.getX());
        int tempY = GameVariables.calculateCellIndex(this.getY());

        // Xóa bomb state
        this.getBelongTo().setStateBomb(tempY, tempX, false);

        int len = owner.getFlameLength();
        int side = (int) GameVariables.cellLength;

        // sinh flame ra bên trái bom
        for (int i = tempX - 1; i >= 0 && i >= tempX - len; i--) {
            boolean breakFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getBelongTo().isBlockCell(tempY, i)) {
                Flame.handleIntersectCell(this.getBelongTo().getCells(tempY, i));

                breakFlame = true;

                if (!(this.getBelongTo().getCells(tempY, i) instanceof Block))  {
                    break;
                }
            }

            if (i == tempX - len) {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), i * side, tempY * side, side, side, Flame.FlameType.LEFT_));
            } else {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), i * side, tempY * side, side, side, Flame.FlameType.HORIZONTAL_));
            }

            if (breakFlame) {
                break;
            }
        }

        // sinh flame ra bên phải bom
        for (int i = tempX + 1; i <= this.getBelongTo().getNumberOfColumn() && i <= tempX + len; i++) {
            boolean breakFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getBelongTo().isBlockCell(tempY, i)) {
                Flame.handleIntersectCell(this.getBelongTo().getCells(tempY, i));

                breakFlame = true;

                if (!(this.getBelongTo().getCells(tempY, i) instanceof Block))  {
                    break;
                }
            }

            if (i == tempX + len) {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), i * side, tempY * side, side, side, Flame.FlameType.RIGHT_));
            } else {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), i * side, tempY * side, side, side, Flame.FlameType.HORIZONTAL_));
            }

            if (breakFlame) {
                break;
            }
        }

        // sinh flame ra bên trên bom
        for (int i = tempY - 1; i >= 0 && i >= tempY - len; i--) {
            boolean breakFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getBelongTo().isBlockCell(i, tempX)) {
                Flame.handleIntersectCell(this.getBelongTo().getCells(i, tempX));

                breakFlame = true;

                if (!(this.getBelongTo().getCells(i, tempX) instanceof Block))  {
                    break;
                }
            }

            if (i == tempY - len) {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), tempX * side, i * side, side, side, Flame.FlameType.UP_));
            } else {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), tempX * side, i * side, side, side, Flame.FlameType.VERTICAL_));
            }

            if (breakFlame) {
                break;
            }
        }

        // sinh flame ra bên dưới bom
        for (int i = tempY + 1; i <= this.getBelongTo().getNumberOfRow() && i <= tempY + len; i++) {
            boolean breakFlame = false;

            // gặp cô cản, ngừng sinh flame
            if (this.getBelongTo().isBlockCell(i, tempX)) {
                Flame.handleIntersectCell(this.getBelongTo().getCells(i, tempX));

                breakFlame = true;

                if (!(this.getBelongTo().getCells(i, tempX) instanceof Block))  {
                    break;
                }
            }

            if (i == tempY + len) {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), tempX * side, i * side, side, side, Flame.FlameType.DOWN_));
            } else {
                this.getBelongTo().addFlame(new Flame(this.getBelongTo(), tempX * side, i * side, side, side, Flame.FlameType.VERTICAL_));
            }

            if (breakFlame) {
                break;
            }
        }

        //sinh flame ở chính giữa
        this.getBelongTo().addFlame(new Flame(this.getBelongTo(), tempX * side, tempY * side, side, side, Flame.FlameType.CENTER_));

        SoundVariable.playSound(FilesPath.ExplosionAudio);
        //FilesPath.ExplosionAudio.setMicrosecondPosition(0);
    }

    @Override
    public Image getImage() {
        return FilesPath.Bomb;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(5);
    }

    @Override
    protected void render(Image currentImage, double renderX, double renderY, double renderWidth, double renderLength) {
        setPosRender(5, 5, -10, -10);

        super.render(currentImage, renderX, renderY, renderWidth, renderLength);
    }
}
