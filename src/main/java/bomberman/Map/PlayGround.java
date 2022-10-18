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

    public void clearPlayers() {
        players.clear();
    }

    /**
     * Threats.
     */
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void clearEnemies() {
        enemies.clear();
    }

    public void removeEnemy(int index) {
        enemies.remove(index);
    }

    /**
     * List bomb.
     */
    private ArrayList<Bomb> bombs = new ArrayList<>();

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void clearBombs() {
        bombs.clear();
    }

    public void addBomb(Bomb tempBomb) {
        bombs.add(tempBomb);
    }

    public void removeBomb(int index) {
        bombs.remove(index);
    }

    /**
     * Trạng thái ô đó có bomb hay không.
     * True là có bomb, false là không có bomb.
     */
    private boolean[][] stateBomb = new boolean[50][50];

    public boolean getStateBomb(int tempX, int tempY) {
        return stateBomb[tempX][tempY];
    }

    public void setStateBomb(int tempX, int tempY, boolean value) {
        stateBomb[tempX][tempY] = value;
    }

    /**
     * List flame.
     */
    private ArrayList<Flame> flames = new ArrayList<>();

    public ArrayList<Flame> getFlames() {
        return flames;
    }

    public void clearFlames() {
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
    private final double cellLength = GameVariables.cellLength;

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
        InputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = FilesPath.class.getResource(mapPath).openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String[] item;

            String line = bufferedReader.readLine();
            item = line.split(" ");
            maxLevel = Integer.parseInt(item[0]);
            numberOfRow = Integer.parseInt(item[1]);
            numberOfColumn = Integer.parseInt(item[2]);

            mapLength = cellLength * numberOfColumn;
            mapWidth = cellLength * numberOfRow;

            for (int levelNow = 0; levelNow < maxLevel; levelNow++) {
                for (int i = 0; i < numberOfRow; i++) {
                    line = bufferedReader.readLine();
                    for (int j = 0; j < numberOfColumn; j++) {
                        allLevelMaps[levelNow][i][j] = line.charAt(j);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            System.out.println("IO exception");
        } finally {
            try {
                bufferedReader.close();
                fileInputStream.close();
            } catch (IOException ex) {
                System.out.println("IO exception");
            }
        }
    }

    /**
     * Tạo tình trạng map của level hiện tại.
     */
    public void createMapAtLevel() {
        clearPlayers();
        clearEnemies();
        clearBombs();
        clearFlames();

        for (int y = 0; y < numberOfRow; y++) {
            for (int i = 0; i < numberOfColumn; i++) {
                char tmp = allLevelMaps[level][y][i];
                switch (tmp) {
                    case 'p':
                        players.add(new Bomber(this, cellLength * i, cellLength * y));
                        cells[y][i] = new Grass(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case '#':
                        cells[y][i] = new Wall(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case '*':
                        cells[y][i] = new Brick(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case 'x':
                        cells[y][i] = new Portal(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case 'b':
                        cells[y][i] = new Item(this, cellLength * i, cellLength * y, cellLength, cellLength, Item.typeOfItems.BOMB_ITEM_);
                        break;
                    case 'f':
                        cells[y][i] = new Item(this, cellLength * i, cellLength * y, cellLength, cellLength, Item.typeOfItems.FLAME_ITEM_);
                        break;
                    case 's':
                        cells[y][i] = new Item(this, cellLength * i, cellLength * y, cellLength, cellLength, Item.typeOfItems.SPEED_ITEM_);
                        break;
                    case '1':
                        enemies.add(new Balloom(this, cellLength * i, cellLength * y));
                        cells[y][i] = new Grass(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case '2':
                        enemies.add(new Oneal(this, cellLength * i, cellLength * y));
                        cells[y][i] = new Grass(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    case '3':
                        enemies.add(new Teleport(this, cellLength * i, cellLength * y));
                        cells[y][i] = new Grass(this, cellLength * i, cellLength * y, cellLength, cellLength);
                        break;
                    default:
                        cells[y][i] = new Grass(this, cellLength * i, cellLength * y, cellLength, cellLength);
                }
            }
        }

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                stateBomb[i][j] = false;
            }
        }
    }

    /**
     * Kiểm tra một ô có bị chặn không.
     *
     * @param temp_x chỉ số x
     * @param temp_y chỉ số y
     * @return có bị block hoặc không
     */
    public boolean isBlockCell(int temp_x, int temp_y) {
        //các trường hợp bị chặn
        //ô tường
        if ((cells[temp_x][temp_y] instanceof Wall)) {
            return true;
        }

        // ô gạch chưa bị nổ
        if (cells[temp_x][temp_y] instanceof Brick) {
            if (!((Brick) cells[temp_x][temp_y]).isFinalState()) {
                return true;
            }
        }

        // ô item chưa bị nổ
        if ((cells[temp_x][temp_y] instanceof Item)) {
            if (!((Item) cells[temp_x][temp_y]).isFinalState()) {
                return true;
            }
        }

        // ô portal chưa bị nổ
        if ((cells[temp_x][temp_y] instanceof Portal)) {
            if (!((Portal) cells[temp_x][temp_y]).isFinalState()) {
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
