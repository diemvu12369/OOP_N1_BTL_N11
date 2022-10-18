package bomberman.Object.MovingObject;

import java.util.ArrayList;

import bomberman.GlobalVariable.GameVariables;

import bomberman.Map.PlayGround;

import bomberman.Object.NonMovingObject.Bomb;
import bomberman.Object.GameObject;
import javafx.scene.image.Image;

public abstract class MovingObject extends GameObject {

    /**
     * Constructor cho Moving Object.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public MovingObject(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);

        setObjectDirection(ObjectDirection.NONE_, true);

        currentState = ObjectMovementState.HORIZONTAL_;
    }

    // MOVEMENT STATE ---------------------------------------------------------------------------

    /**
     * Hướng di chuyển của object.
     */
    public enum ObjectDirection {
        LEFT_,
        RIGHT_,
        UP_,
        DOWN_,
        NONE_
    }

    /**
     * Hàng chờ hướng di chuyển.
     */
    private ArrayList<ObjectDirection> queueDirection = new ArrayList<>();

    /**
     * Trạng thái hiện tại.
     */
    private ObjectDirection currentDirection;

    /**
     * Set hướng di chuyển cho object.
     *
     * @param objectDirection hướng di chuyển
     * @param tempDirection   trạng thái có hoặc không
     */
    public void setObjectDirection(ObjectDirection objectDirection, boolean tempDirection) {
        queueDirection.remove(objectDirection);

        if (tempDirection) {
            queueDirection.add(objectDirection);
        }

        currentDirection = queueDirection.get(queueDirection.size() - 1);
    }

    /**
     * Trạng thái di chuyển dọc hoặc ngang của object.
     */
    public enum ObjectMovementState {
        VERTICAL_,
        HORIZONTAL_
    }

    /**
     * Trạng thái di chuyển hiện tại của object.
     */
    private ObjectMovementState currentState;

    //---------------------------------------------------------------------------------------------

    /**
     * Tốc độ của object.
     */
    private double speed = 3; // DEFAULT SPEED

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Độ lệch x giữa điểm cần di chuyển tới và điểm hiện tại.
     */
    private double deltaX;

    /**
     * Độ lệch y giữa điểm cần di chuyển tới và điểm hiện tại.
     */
    private double deltaY;

    /**
     * Tính khoảng cách tối đa có thể di chuyển.
     */
    public void calculateDistance() {
        deltaX = 0;
        deltaY = 0;

        switch (currentDirection) {
            case LEFT_:
                deltaX -= speed;
                break;
            case RIGHT_:
                deltaX += speed;
                break;
            case UP_:
                deltaY -= speed;
                break;
            case DOWN_:
                deltaY += speed;
                break;
        }
    }

    /**
     * Kiểm tra xem có thể đứng ở vị trí hiện tại không.
     *
     * @param temp_x tạo độ trái trên x
     * @param temp_y tọa độ trái trên y
     * @return có hoặc không đứng được
     */
    boolean checkCanMove(double temp_x, double temp_y) {
        int x1 = GameVariables.calculateCellIndex(temp_x);
        int x2 = GameVariables.calculateCellIndex(temp_x + this.getWidth() - 1);
        int y1 = GameVariables.calculateCellIndex(temp_y);
        int y2 = GameVariables.calculateCellIndex(temp_y + this.getLength() - 1);

        //gặp bomb
        for (Bomb bomb : this.getBelongTo().getBombs()) {
            if (bomb.checkIntersect(temp_x, temp_x + this.getWidth() - 1,
                    temp_y, temp_y + this.getLength() - 1) &&
                    bomb.checkBlockStatusWithObject(this)) {
                return false;
            }
        }

        //đứng ở ô không cho phép
        return !this.getBelongTo().isBlockCell(y1, x1) &&
                !this.getBelongTo().isBlockCell(y1, x2) &&
                !this.getBelongTo().isBlockCell(y2, x1) &&
                !this.getBelongTo().isBlockCell(y2, x2);
    }

    /**
     * Xử lí object di chuyển.
     */
    public void move() {
        calculateDistance();

        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        double cellLength = GameVariables.cellLength;

        double nowX = getX();
        double nowY = getY();

        //Biến này cộng vào tọa độ để object luôn nằm giữa cell.
        double adjustPosition = (cellLength - this.getLength()) / 2;

        if (currentState == ObjectMovementState.HORIZONTAL_) {
            if (currentDirection == ObjectDirection.LEFT_ || currentDirection == ObjectDirection.RIGHT_) {
                if (deltaX != 0 && checkCanMove(nowX + deltaX, nowY)) {
                    setX(nowX + deltaX);
                }
            } else if (currentDirection == ObjectDirection.UP_ || currentDirection == ObjectDirection.DOWN_) {
                int temp_x = GameVariables.calculateCellIndex(this.getCenterX());

                double newPositionX = temp_x * cellLength + adjustPosition;

                if (deltaY != 0 && checkCanMove(newPositionX, nowY + cellLength * (deltaY > 0 ? 1 : -1))) {
                    setX(newPositionX);
                    setY(nowY + deltaY);

                    currentState = ObjectMovementState.VERTICAL_;
                }
            }
        } else if (currentState == ObjectMovementState.VERTICAL_) {
            if (currentDirection == ObjectDirection.UP_ || currentDirection == ObjectDirection.DOWN_) {
                if (deltaY != 0 && checkCanMove(nowX, nowY + deltaY)) {
                    setY(nowY + deltaY);
                }
            } else if (currentDirection == ObjectDirection.LEFT_ || currentDirection == ObjectDirection.RIGHT_) {
                int temp_y = GameVariables.calculateCellIndex(this.getCenterY());

                double newPositionY = temp_y * cellLength + adjustPosition;

                if (deltaX != 0 && checkCanMove(nowX + cellLength * (deltaX > 0 ? 1 : -1), newPositionY)) {
                    setX(nowX + deltaX);
                    setY(newPositionY);

                    currentState = ObjectMovementState.HORIZONTAL_;
                }
            }
        }
    }

    // *************************** GRAPHIC **********************************************************

    ObjectDirection lastDirection = ObjectDirection.LEFT_;

    /**
     * Chỉ số direction để tính được renderY cho image.
     */
    private int getYByDirection() {
        if (lastDirection == ObjectDirection.UP_) {
            return 0;
        }

        if (lastDirection == ObjectDirection.DOWN_) {
            return 1;
        }

        if (lastDirection == ObjectDirection.LEFT_) {
            return 2;
        }

        return 3;
    }

    @Override
    public void draw() {
        // Tính lastDirection
        if (currentDirection != ObjectDirection.NONE_) {
            lastDirection = currentDirection;
        }

        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double imageWidth = currentImage.getHeight();
        double imageLength = currentImage.getWidth();

        double spriteSize = imageWidth / 4;

        numberOfSprite = (int) (imageLength / spriteSize) - 1;

        // Tính toán thông tin render
        double renderX;
        double renderY = getYByDirection() * spriteSize;

        if (currentDirection == ObjectDirection.NONE_) {
            renderX = 0;

            resetFrameCount();
        } else {
            // Tính toán currentFrame
            if (gameFrameCount >= (numberOfSprite * numberOfFramePerSprite)) {
                gameFrameCount = gameFrameCount % (numberOfSprite * numberOfFramePerSprite);
            }

            currentSprite = gameFrameCount / numberOfFramePerSprite;

            gameFrameCount++;

            renderX = (currentSprite + 1) * spriteSize;
        }

        // Render
        setPosRender(0, 0, 0, 0);

        render(currentImage, renderX, renderY, spriteSize, spriteSize);
    }
}