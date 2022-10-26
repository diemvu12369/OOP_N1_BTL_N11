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

    public int numberOfRow() {
        return numberOfRow;
    }

    /**
     * Số lượng cột.
     */
    private int numberOfColumn;

    public int numberOfColumn() {
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

    public GameObject getCell(int tempX, int tempY) {
        return cells[tempX][tempY];
    }

    /**
     * List player.
     */
    private ArrayList<Bomber> playerList = new ArrayList<>();

    public ArrayList<Bomber> getPlayerList() {
        return playerList;
    }

    public void resetPlayerList() {
        playerList.clear();
    }

    /**
     * Threats.
     */
    private ArrayList<Enemy> enemyList = new ArrayList<>();

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }

    public void resetEnemies() {
        enemyList.clear();
    }

    public void removeEnemy(int position) {
        enemyList.remove(position);
    }

    /**
     * List bomb.
     */
    private ArrayList<Bomb> bombList = new ArrayList<>();

    public ArrayList<Bomb> getBombList() {
        return bombList;
    }

    public void resetBombs() {
        bombList.clear();
    }

    public void addBomb(Bomb currentBomb) {
        bombList.add(currentBomb);
    }

    public void removeBomb(int index) {
        bombList.remove(index);
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
    private ArrayList<Flame> flameList = new ArrayList<>();

    public ArrayList<Flame> getFlameList() {
        return flameList;
    }

    public void resetFlames() {
        flameList.clear();
    }

    public void addFlame(Flame tempFlame) {
        flameList.add(tempFlame);
    }

    public void deleteFlame(int index) {
        flameList.remove(index);
    }

    /**
     * kích thước bản đồ theo chiều ngang.
     */
    private double playgroundLength;

    public double getMapLength() {
        return playgroundLength;
    }

    /**
     * kích thước bản đồ theo chiều dọc.
     */
    private double playgroundWidth;

    public double getMapWidth() {
        return playgroundWidth;
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
     * @param playgroundPath đường dẫn đến playground
     */
    public PlayGround(String playgroundPath) {
        readMapsFromFile(playgroundPath);

        createMapAtLevel();
    }

    /**
     * Nhập dữ liệu của playground từ file.
     *
     * @param playgroundPath đường dẫn đến playground
     */
    private void readMapsFromFile(String playgroundPath) {
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = FilesPath.class.getResource(playgroundPath).openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String[] item;

            String command = reader.readLine();
            item = command.split(" ");
            maxLevel = Integer.parseInt(item[0]);
            numberOfRow = Integer.parseInt(item[1]);
            numberOfColumn = Integer.parseInt(item[2]);

            playgroundLength = unitLength * numberOfColumn;
            playgroundWidth = unitLength * numberOfRow;

            for (int currentLevel = 0; currentLevel < maxLevel; currentLevel++) {
                for (int i = 0; i < numberOfRow; i++) {
                    command = reader.readLine();
                    for (int j = 0; j < numberOfColumn; j++) {
                        allLevelMaps[currentLevel][i][j] = command.charAt(j);
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
     * Tạo tình trạng playground của level hiện tại.
     */
    public void createMapAtLevel() {
        resetPlayerList();
        resetEnemies();
        resetBombs();
        resetFlames();

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                char tmp = allLevelMaps[level][i][j];
                switch (tmp) {
                    case 'p':
                        playerList.add(new Bomber(this, unitLength * j, unitLength * i));
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
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.ITEM_BOMB_);
                        break;
                    case 'f':
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.ITEM_FLAME_);
                        break;
                    case 's':
                        cells[i][j] = new Item(this, unitLength * j, unitLength * i, unitLength, unitLength, Item.typeOfItems.ITEM_SPEED_);
                        break;
                    case '1':
                        enemyList.add(new Balloom(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '2':
                        enemyList.add(new Oneal(this, unitLength * j, unitLength * i));
                        cells[i][j] = new Grass(this, unitLength * j, unitLength * i, unitLength, unitLength);
                        break;
                    case '3':
                        enemyList.add(new Teleport(this, unitLength * j, unitLength * i));
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
            if (!((Brick) cells[current_x][current_y]).isEndingState()) {
                return true;
            }
        }

        // ô item chưa bị nổ
        if ((cells[current_x][current_y] instanceof Item)) {
            if (!((Item) cells[current_x][current_y]).isEndingState()) {
                return true;
            }
        }

        // ô portal chưa bị nổ
        if ((cells[current_x][current_y] instanceof Portal)) {
            if (!((Portal) cells[current_x][current_y]).isEndingState()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Render playground ra screen.
     */
    public void render() {
        backGround.draw();

        for (int i = 0; i < numberOfRow; i++) {
            for (int j = 0; j < numberOfColumn; j++) {
                cells[i][j].draw();
            }
        }

        for (Bomb x : bombList) {
            x.draw();
        }

        for (Bomber player : playerList) {
            if (player != null) {
                player.draw();
            }
        }

        for (Enemy enemy : enemyList) {
            enemy.draw();
        }

        for (Flame flame : flameList) {
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
