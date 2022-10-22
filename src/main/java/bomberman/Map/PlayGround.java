package bomberman.Map;

import java.io.*;
import java.util.ArrayList;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;

import bomberman.GlobalVariable.RenderVariable;
import bomberman.Object.MovingObject.Bomber.Bomber;
import bomberman.Object.MovingObject.Threats.Enemy;
import bomberman.Object.MovingObject.Threats.Balloom;
import bomberman.Object.MovingObject.Threats.Oneal;
import bomberman.Object.MovingObject.Threats.Teleport;
import bomberman.Object.NonMovingObject.*;
import bomberman.Object.GameObject;

public class PlayGround {
    // ********** VARIABLES, SETTER, GETTER, VARIABLES OPERATION ********************************

    /**
     * số level.
     */
    private int maxLevel;

    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Level hiện tại.
     */
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Số lượng hàng.
     */
    private int numberOfRow;

    public int getNumberOfRow() {
        return numberOfRow;
    }

    /**
     * Số lượng cột.
     */
    private int numberOfColumn;

    public int getNumberOfColumn() {
        return numberOfColumn;
    }

    /**
     * BackGround của màn game.
     */
    private BackGround backGround = new BackGround(this);

    /**
     * Các ô trên bản đồ, mỗi ô là 1 object.
     */
    private GameObject[][] cells = new GameObject[50][50];

    public GameObject getCells(int tempX, int tempY) {
        return cells[tempX][tempY];
    }

    /**
     * List player.
     */
    private ArrayList<Bomber> players = new ArrayList<>();

    public ArrayList<Bomber> getPlayers() {
        return players;
    }

    public void resetPlayers() {
        players.clear();
    }

    /**
     * Threats.
     */
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void resetEnemies() {
        enemies.clear();
    }

    public void removeEnemy(int position) {
        enemies.remove(position);
    }

    /**
     * List bomb.
     */
    private ArrayList<Bomb> bombs = new ArrayList<>();

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void resetBombs() {
        bombs.clear();
    }

    public void addBomb(Bomb currentBomb) {
        bombs.add(currentBomb);
    }

    public void removeBomb(int index) {
        bombs.remove(index);
    }

    /**
     * Trạng thái ô đó có bomb hay không.
     * True là có bomb, false là không có bomb.
     */
    private boolean[][] bombState = new boolean[50][50];

    public boolean getBombState(int tempX, int tempY) {
        return bombState[tempX][tempY];
    }

    public void setBombState(int tempX, int tempY, boolean value) {
        bombState[tempX][tempY] = value;
    }

    /**
     * List flame.
     */
    private ArrayList<Flame> flames = new ArrayList<>();

    public ArrayList<Flame> getFlames() {
        return flames;
    }

    public void resetFlames() {
        flames.clear();
    }

    public void addFlame(Flame tempFlame) {
        flames.add(tempFlame);
    }

    public void removeFlame(int index) {
        flames.remove(index);
    }

    /**
     * kích thước bản đồ theo chiều ngang.
     */
    private double mapLength;

    public double getMapLength() {
        return mapLength;
    }

    /**
     * kích thước bản đồ theo chiều dọc.
     */
    private double mapWidth;

    public double getMapWidth() {
        return mapWidth;
    }

    /**
     * Lưu tất cả các bản đồ của các level.
     */
    private char[][][] allLevelMaps = new char[5][50][50];

    /**
     * Độ dài cạnh của một ô.
     */
    private final double unitLength = GameVariables.unitLength;

    // ***********************************************************************************************

    /**
     * Khởi tạo 1 PlayGround(Map).
     *
     * @param mapPath đường dẫn đến map
     */
    public PlayGround(String mapPath) {
        readMapsFromFile(mapPath);

        createMapAtLevel();
    }

    /**
     * Nhập dữ liệu của map từ file.
     *
     * @param mapPath đường dẫn đến map
     */
    private void readMapsFromFile(String mapPath) {
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = FilesPath.class.getResource(mapPath).openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String[] item;

            String line = reader.readLine();
            item = line.split(" ");
            maxLevel = Integer.parseInt(item[0]);
            numberOfRow = Integer.parseInt(item[1]);
            numberOfColumn = Integer.parseInt(item[2]);

            mapLength = unitLength * numberOfColumn;
            mapWidth = unitLength * numberOfRow;

            for (int currentLevel = 0; currentLevel < maxLevel; currentLevel++) {
                for (int i = 0; i < numberOfRow; i++) {
                    line = reader.readLine();
                    for (int j = 0; j < numberOfColumn; j++) {
                        allLevelMaps[currentLevel][i][j] = line.charAt(j);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            System.out.println("IO exception");
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException ex) {
                System.out.println("IO exception");
            }
        }
    }

    /**
     * Tạo tình trạng map của level hiện tại.
     */
    public void createMapAtLevel() {
        resetPlayers();
        resetEnemies();
        resetBombs();
        resetFlames();

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                char tmp = allLevelMaps[level][i][j];
                switch (tmp) {
                    case 'p':
                        players.add(new Bomber(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '#':
                        cells[i][j] = new Wall(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '*':
                        cells[i][j] = new Brick(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case 'x':
                        cells[i][j] = new Portal(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case 'b':
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.BOMB_ITEM_);
                        break;
                    case 'f':
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.FLAME_ITEM_);
                        break;
                    case 's':
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.SPEED_ITEM_);
                        break;
                    case '1':
                        enemies.add(new Balloom(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '2':
                        enemies.add(new Oneal(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '3':
                        enemies.add(new Teleport(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    default:
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                }
            }
        }

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                bombState[i][j] = false;
            }
        }
    }

    /**
     * Kiểm tra một ô có bị chặn không.
     *
     * @param current_x chỉ số x
     * @param current_y chỉ số y
     * @return có bị block hoặc không
     */
    public boolean isCellBlocked(int current_x, int current_y) {
        //các trường hợp bị chặn
        //ô tường
        if ((cells[current_x][current_y] instanceof Wall)) {
            return true;
        }

        // ô gạch chưa bị nổ
        if (cells[current_x][current_y] instanceof Brick) {
            if (!((Brick) cells[current_x][current_y]).isFinalState()) {
                return true;
            }
        }

        // ô item chưa bị nổ
        if ((cells[current_x][current_y] instanceof Item)) {
            if (!((Item) cells[current_x][current_y]).isFinalState()) {
                return true;
            }
        }

        // ô portal chưa bị nổ
        if ((cells[current_x][current_y] instanceof Portal)) {
            if (!((Portal) cells[current_x][current_y]).isFinalState()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Render map ra screen.
     */
    public void render() {
        backGround.draw();

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                cells[i][j].draw();
            }
        }

        for (Bomb x : bombs) {
            x.draw();
        }

        for (Bomber player : players) {
            if (player != null) {
                player.draw();
            }
        }

        for (Enemy enemy : enemies) {
            enemy.draw();
        }

        for (Flame flame : flames) {
            flame.draw();
        }
        if (RenderVariable.stateSound) {
            RenderVariable.gc.drawImage(FilesPath.Sound, 1201, 0, 38, 38);
        }
        else {
            RenderVariable.gc.drawImage(FilesPath.SoundOff, 1201, 0, 38, 38);
        }
    }
}
