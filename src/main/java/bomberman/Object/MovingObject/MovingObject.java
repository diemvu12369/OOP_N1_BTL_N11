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
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public MovingObject(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        setDirectionOfObject(DirectionOfObject.NONE_, true);

        currentState = ObjectMovementState.HORIZONTAL_;
    }

    // MOVEMENT STATE ---------------------------------------------------------------------------

    /**
     * Hướng di chuyển của object.
     */
    public enum DirectionOfObject {
        LEFT_,
        RIGHT_,
        UP_,
        DOWN_,
        NONE_
    }

    /**
     * Hàng chờ hướng di chuyển.
     */
    private ArrayList<DirectionOfObject> directionQueue = new ArrayList<>();

    /**
     * Trạng thái hiện tại.
     */
    private DirectionOfObject currentDirection;

    /**
     * Set hướng di chuyển cho object.
     *
     * @param directionOfObject hướng di chuyển
     * @param tempDirection   trạng thái có hoặc không
     */
    public void setDirectionOfObject(DirectionOfObject directionOfObject, boolean tempDirection) {
        directionQueue.remove(directionOfObject);

        if (tempDirection) {
            directionQueue.add(directionOfObject);
        }

        currentDirection = directionQueue.get(directionQueue.size() - 1);
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
     * @param current_x tạo độ trái trên x
     * @param current_y tọa độ trái trên y
     * @return có hoặc không đứng được
     */
    boolean checkCanMove(double current_x, double current_y) {
        int minX = GameVariables.calculateCellIndex(current_x);
        int maxX = GameVariables.calculateCellIndex(current_x + this.getWidth() - 1);
        int minY = GameVariables.calculateCellIndex(current_y);
        int maxY = GameVariables.calculateCellIndex(current_y + this.getLength() - 1);

        //gặp bomb
        for (Bomb bomb : this.getCorrespondingPlayGround().getBombs()) {
            if (bomb.checkIntersect(current_x, current_x + this.getWidth() - 1,
                    current_y, current_y + this.getLength() - 1) &&
                    bomb.checkBlockStatusWithObject(this)) {
                return false;
            }
        }

        //đứng ở ô không cho phép
        return !this.getCorrespondingPlayGround().isCellBlocked(minY, minX) &&
                !this.getCorrespondingPlayGround().isCellBlocked(minY, maxX) &&
                !this.getCorrespondingPlayGround().isCellBlocked(maxY, minX) &&
                !this.getCorrespondingPlayGround().isCellBlocked(maxY, maxX);
    }

    /**
     * Xử lí object di chuyển.
     */
    public void move() {
        calculateDistance();

        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        double unitLength = GameVariables.unitLength;

        double currentX = getX();
        double currentY = getY();

        //Biến này cộng vào tọa độ để object luôn nằm giữa cell.
        double adjustPosition = (unitLength - this.getLength()) / 2;

        if (currentState == ObjectMovementState.HORIZONTAL_) {
            if (currentDirection == DirectionOfObject.LEFT_ || currentDirection == DirectionOfObject.RIGHT_) {
                if (deltaX != 0 && checkCanMove(currentX + deltaX, currentY)) {
                    setX(currentX + deltaX);
                }
            } else if (currentDirection == DirectionOfObject.UP_ || currentDirection == DirectionOfObject.DOWN_) {
                int current_x = GameVariables.calculateCellIndex(this.getCenterX());

                double newPositionX = current_x * unitLength + adjustPosition;

                if (deltaY != 0 && checkCanMove(newPositionX, currentY + unitLength * (deltaY > 0 ? 1 : -1))) {
                    setX(newPositionX);
                    setY(currentY + deltaY);

                    currentState = ObjectMovementState.VERTICAL_;
                }
            }
        } else if (currentState == ObjectMovementState.VERTICAL_) {
            if (currentDirection == DirectionOfObject.UP_ || currentDirection == DirectionOfObject.DOWN_) {
                if (deltaY != 0 && checkCanMove(currentX, currentY + deltaY)) {
                    setY(currentY + deltaY);
                }
            } else if (currentDirection == DirectionOfObject.LEFT_ || currentDirection == DirectionOfObject.RIGHT_) {
                int current_y = GameVariables.calculateCellIndex(this.getCenterY());

                double newPositionY = current_y * unitLength + adjustPosition;

                if (deltaX != 0 && checkCanMove(currentX + unitLength * (deltaX > 0 ? 1 : -1), newPositionY)) {
                    setX(currentX + deltaX);
                    setY(newPositionY);

                    currentState = ObjectMovementState.HORIZONTAL_;
                }
            }
        }
    }

    // *************************** GRAPHIC **********************************************************

    DirectionOfObject lastDirection = DirectionOfObject.LEFT_;

    /**
     * Chỉ số direction để tính được renderY cho image.
     */
    private int getYByDirection() {
        if (lastDirection == DirectionOfObject.UP_) {
            return 0;
        }

        if (lastDirection == DirectionOfObject.DOWN_) {
            return 1;
        }

        if (lastDirection == DirectionOfObject.LEFT_) {
            return 2;
        }

        return 3;
    }

    @Override
    public void draw() {
        // Tính lastDirection
        if (currentDirection != DirectionOfObject.NONE_) {
            lastDirection = currentDirection;
        }

        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double widthOfImage = currentImage.getHeight();
        double lengthOfImage = currentImage.getWidth();

        double sizeOfSprite = widthOfImage / 4;

        numberOfSprite = (int) (lengthOfImage / sizeOfSprite) - 1;

        // Tính toán thông tin render
        double renderX;
        double renderY = getYByDirection() * sizeOfSprite;

        if (currentDirection == DirectionOfObject.NONE_) {
            renderX = 0;

            resetFrameCount();
        } else {
            // Tính toán currentFrame
            if (gameFrameCount >= (numberOfSprite * numberOfFramePerSprite)) {
                gameFrameCount = gameFrameCount % (numberOfSprite * numberOfFramePerSprite);
            }

            currentSprite = gameFrameCount / numberOfFramePerSprite;

            gameFrameCount++;

            renderX = (currentSprite + 1) * sizeOfSprite;
        }

        // Render
        setPosRender(0, 0, 0, 0);

        render(currentImage, renderX, renderY, sizeOfSprite, sizeOfSprite);
    }
}