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
     * (Đặt là belongTo để tránh trùng lặp với owner của bomb)
     */
    private PlayGround belongTo;

    public void setBelongTo(PlayGround belongTo) {
        this.belongTo = belongTo;
    }

    public PlayGround getBelongTo() {
        return belongTo;
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
        calculateCenterPoint();
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

        calculateCenterPoint();
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

        calculateCenterPoint();
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

        calculateCenterPoint();
    }

    /**
     * Tọa độ x của tâm object.
     */
    private double centerX;

    public double getCenterX() {
        return centerX;
    }

    /**
     * Tọa độ y của tâm object.
     */
    private double centerY;

    public double getCenterY() {
        return centerY;
    }

    /**
     * Constructor cho object của game.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public GameObject(PlayGround belongTo, double x, double y, double width, double length) {
        this.belongTo = belongTo;

        this.x = x;
        this.y = y;

        this.width = width;
        this.length = length;

        calculateCenterPoint();

        currentSprite = 0;
        gameFrameCount = 0;

        setGraphicSetting();
    }

    /**
     * Tính tọa độ tâm của object.
     */
    public void calculateCenterPoint() {
        centerX = x + width / 2;
        centerY = y + length / 2;
    }

    /**
     * Kiểm tra xem một điểm có nằm trong object hay không.
     *
     * @param _x tọa độ x
     * @param _y tọa độ y
     * @return có hoặc không
     */
    public boolean checkPointInside(double _x, double _y) {
        return x <= _x && _x <= x + width && y <= _y && _y <= y + length;
    }

    /**
     * Kiểm tra xem object này có chạm với object kia không.
     *
     * @param other object kia
     * @return có hoặc không
     */
    public boolean checkIntersect(GameObject other) {
        return !(x >= other.getX() + other.getWidth()) &&
                !(y >= other.getY() + other.getLength()) &&
                !(x + width <= other.getX()) &&
                !(y + length <= other.getY());
    }

    /**
     * Kiểm tra xem object này có chạm với khối kia không.
     *
     * @param temp_x_1 min x
     * @param temp_x_2 max x
     * @param temp_y_1 min y
     * @param temp_y_2 max y
     * @return có hoặc không
     */
    public boolean checkIntersect(double temp_x_1, double temp_x_2, double temp_y_1, double temp_y_2) {
        return !(x >= temp_x_2) &&
                !(y >= temp_y_2) &&
                !(x + width <= temp_x_1) &&
                !(y + length <= temp_y_1);
    }

    // ****************************************** RENDER *************************************************

    /**
     * Số lượng sprite của object này.
     */
    protected int numberOfSprite;

    /**
     * Chỉ số sprite hiện tại.
     * (Đánh số từ 0 đến numberOfSprite - 1)
     */
    protected int currentSprite;

    /**
     * Số lượng frame của game chạy để hiển thị 1 sprite của object.
     * Giải thích : Chương trình mặc định chạy 60 frame/1 giây (60fps),
     * để kéo dài thời gian 1 sprite của object hiển thị ra
     * thì cứ mỗi 1 hoặc 1 vài frame của chương trình thì hiển thị
     * 1 sprite của object.
     * Ví dụ numberOfGameFramePerSprite là 2, thì vòng lặp play() của game
     * chạy 2 lần thì Object đổi 1 sprite.
     */
    protected int numberOfFramePerSprite;

    protected void setNumberOfFramePerSprite(int numberOfFramePerSprite) {
        this.numberOfFramePerSprite = numberOfFramePerSprite;
    }

    /**
     * Biến đếm game frame để tính toán currentSprite.
     */
    protected int gameFrameCount;

    /**
     * Reset lại để sprite sheet chạy từ đầu.
     */
    protected void resetFrameCount() {
        gameFrameCount = 0;
    }

    /**
     * Trả về image hiện tại của object.
     */
    public abstract Image getImage();

    /**
     * Set thông tin về Sprite và hình ảnh(với MovingObject) cho mỗi object riêng biệt.
     * (Set NumberOfFramePerSprite cho từng object)
     */
    public abstract void setGraphicSetting();

    // Vị trí để render Object trên màn hình.
    // Bình thường thì nó là kích thước của object.
    // Dùng trong trường hợp muốn hình render ra khác kích thước của object.
    private double posRenderX;
    private double posRenderY;
    private double posRenderWidth;
    private double posRenderLength;

    /**
     * Dùng để thay đổi các posRender dựa trên vị trí thực tế của object.
     */
    public void setPosRender(double deltaX, double deltaY, double deltaWidth, double deltaLength) {
        posRenderX = x + deltaX;
        posRenderY = y + deltaY;
        posRenderWidth = width + deltaWidth;
        posRenderLength = length + deltaLength;
    }

    /**
     * Vẽ object.
     */
    public void draw() {
        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double imageWidth = currentImage.getHeight();
        double imageLength = currentImage.getWidth();

        double spriteSize = imageWidth;

        numberOfSprite = (int) (imageLength / spriteSize);

        // Tính toán currentFrame
        if (gameFrameCount >= (numberOfSprite * numberOfFramePerSprite)) {
            gameFrameCount = gameFrameCount % (numberOfSprite * numberOfFramePerSprite);
        }

        currentSprite = gameFrameCount / numberOfFramePerSprite;

        gameFrameCount++;

        // Render
        setPosRender(0, 0, 0, 0);

        render(currentImage, currentSprite * spriteSize, 0, spriteSize, spriteSize);
    }

    /**
     * Gửi thông tin đến server hoặc render ở client
     */
    protected void render(Image currentImage, double renderX, double renderY, double renderWidth, double renderLength) {
        if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Image", FilesPath.encodeImageName(currentImage));
                jsonObject.put("imageX", "" + renderX);
                jsonObject.put("imageY", "" + renderY);
                jsonObject.put("imageWidth", "" + renderWidth);
                jsonObject.put("imageLength", "" + renderLength);
                jsonObject.put("x", "" + posRenderX);
                jsonObject.put("y", "" + posRenderY);
                jsonObject.put("width", "" + posRenderWidth);
                jsonObject.put("length", "" + posRenderLength);
                GameVariables.tempCommandList.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            RenderVariable.gc.drawImage(currentImage, renderX, renderY, renderWidth, renderLength,
                    posRenderX, posRenderY, posRenderWidth, posRenderLength);
        }
    }
}


