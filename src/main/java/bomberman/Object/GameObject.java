package bomberman.Object;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;
import javafx.scene.image.Image;

import bomberman.GlobalVariable.RenderVariable;

import bomberman.Map.PlayGround;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Object của game.
 */
public abstract class GameObject {
    /**
     * Tham chiếu đến PlayGround mà object này thuộc về trong đó.
     * (Đặt là correspondingPlayGround để tránh trùng lặp với bombPlacer của bomb)
     */
    private PlayGround correspondingPlayGround;

    public void setCorrespondingPlayGround(PlayGround correspondingPlayGround) {
        this.correspondingPlayGround = correspondingPlayGround;
    }

    public PlayGround getCorrespondingPlayGround() {
        return correspondingPlayGround;
    }

    /**
     * Tọa độ x.
     */
    private double x;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        calculatePointCenter();
    }

    /**
     * Tọa độ y.
     */
    private double y;

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;

        calculatePointCenter();
    }

    /**
     * Chiều rộng của object.
     */
    private double width;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;

        calculatePointCenter();
    }

    /**
     * Chiều dài của object.
     */
    private double length;

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;

        calculatePointCenter();
    }

    /**
     * Tọa độ x của tâm object.
     */
    private double xCenter;

    public double getXCenter() {
        return xCenter;
    }

    /**
     * Tọa độ y của tâm object.
     */
    private double yCenter;

    public double getYCenter() {
        return yCenter;
    }

    /**
     * Constructor cho object của game.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public GameObject(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        this.correspondingPlayGround = correspondingPlayGround;

        this.x = x;
        this.y = y;

        this.width = width;
        this.length = length;

        calculatePointCenter();

        processingSprite = 0;
        countGameFrame = 0;

        setSettingGraphic();
    }

    /**
     * Tính tọa độ tâm của object.
     */
    public void calculatePointCenter() {
        xCenter = x + width / 2;
        yCenter = y + length / 2;
    }

    /**
     * Kiểm tra xem một điểm có nằm trong object hay không.
     *
     * @param _x tọa độ x
     * @param _y tọa độ y
     * @return có hoặc không
     */
    public boolean checkPointInside(double x, double y) {
        return this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + length;
    }

    /**
     * Kiểm tra xem object này có chạm với object kia không.
     *
     * @param other object kia
     * @return có hoặc không
     */
    public boolean isIntersect(GameObject other) {
        return !(x >= other.getX() + other.getWidth()) &&
                !(y >= other.getY() + other.getLength()) &&
                !(x + width <= other.getX()) &&
                !(y + length <= other.getY());
    }

    /**
     * Kiểm tra xem object này có chạm với khối kia không.
     *
     * @param current_x_1 min x
     * @param current_x_2 max x
     * @param current_y_1 min y
     * @param current_y_2 max y
     * @return có hoặc không
     */
    public boolean isIntersect(double current_x_1, double current_x_2, double current_y_1, double current_y_2) {
        return !(x >= current_x_2) &&
                !(y >= current_y_2) &&
                !(x + width <= current_x_1) &&
                !(y + length <= current_y_1);
    }

    // ****************************************** RENDER *************************************************

    /**
     * Số lượng sprite của object này.
     */
    protected int spriteNumber;

    /**
     * Chỉ số sprite hiện tại.
     * (Đánh số từ 0 đến spriteNumber - 1)
     */
    protected int processingSprite;

    /**
     * Số lượng frame của game chạy để hiển thị 1 sprite của object.
     * Giải thích : Chương trình mặc định chạy 60 frame/1 giây (60fps),
     * để kéo dài thời gian 1 sprite của object hiển thị ra
     * thì cứ mỗi 1 hoặc 1 vài frame của chương trình thì hiển thị
     * 1 sprite của object.
     * Ví dụ numberOfGameFramePerSprite là 2, thì vòng lặp execute() của game
     * chạy 2 lần thì Object đổi 1 sprite.
     */
    protected int framePerSprite;

    protected void setFramePerSprite(int framePerSprite) {
        this.framePerSprite = framePerSprite;
    }

    /**
     * Biến đếm game frame để tính toán processingSprite.
     */
    protected int countGameFrame;

    /**
     * Reset lại để sprite sheet chạy từ đầu.
     */
    protected void resetCountFrame() {
        countGameFrame = 0;
    }

    /**
     * Trả về image hiện tại của object.
     */
    public abstract Image getImage();

    /**
     * Set thông tin về Sprite và hình ảnh(với MovingObject) cho mỗi object riêng biệt.
     * (Set FramePerSprite cho từng object)
     */
    public abstract void setSettingGraphic();

    // Vị trí để render Object trên màn hình.
    // Bình thường thì nó là kích thước của object.
    // Dùng trong trường hợp muốn hình render ra khác kích thước của object.
    private double posXRendered;
    private double posYRendered;
    private double posWidthRendered;
    private double posLengthRendered;

    /**
     * Dùng để thay đổi các posRender dựa trên vị trí thực tế của object.
     */
    public void setPosRender(double deltaX, double deltaY, double deltaWidth, double deltaLength) {
        posXRendered = x + deltaX;
        posYRendered = y + deltaY;
        posWidthRendered = width + deltaWidth;
        posLengthRendered = length + deltaLength;
    }

    /**
     * Vẽ object.
     */
    public void draw() {
        // Image hiện tại
        Image displayImage = getImage();

        // Tính toán thông tin image hiện tại
        double widthOfImage = displayImage.getHeight();
        double lengthOfImage = displayImage.getWidth();

        double sizeOfSprite = widthOfImage;

        spriteNumber = (int) (lengthOfImage / sizeOfSprite);

        // Tính toán currentFrame
        if (countGameFrame >= (spriteNumber * framePerSprite)) {
            countGameFrame = countGameFrame % (spriteNumber * framePerSprite);
        }

        processingSprite = countGameFrame / framePerSprite;

        countGameFrame++;

        // Render
        setPosRender(0, 0, 0, 0);

        render(displayImage, processingSprite * sizeOfSprite, 0, sizeOfSprite, sizeOfSprite);
    }

    /**
     * Gửi thông tin đến server hoặc render ở client
     */
    protected void render(Image displayImage, double renderX, double renderY, double renderWidth, double renderLength) {
        if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
            JSONObject json = new JSONObject();
            try {
                json.put("Image", FilesPath.getImageName(displayImage));
                json.put("imageX", "" + renderX);
                json.put("imageY", "" + renderY);
                json.put("widthOfImage", "" + renderWidth);
                json.put("lengthOfImage", "" + renderLength);
                json.put("x", "" + posXRendered);
                json.put("y", "" + posYRendered);
                json.put("width", "" + posWidthRendered);
                json.put("length", "" + posLengthRendered);
                GameVariables.temporaryCommandList.put(json);
            } catch (JSONException event) {
                event.printStackTrace();
            }
        } else {
            RenderVariable.gc.drawImage(displayImage, renderX, renderY, renderWidth, renderLength,
                    posXRendered, posYRendered, posWidthRendered, posLengthRendered);
        }
    }
}


