package bomberman.GlobalVariable;

import bomberman.PvB_GamePlay;
import bomberman.PvP_GamePlay;
import org.json.JSONArray;

public class GameVariables {
    /**
     * Chế độ chơi PvB.
     * (Khởi tạo biến này khi chế độ PvB bắt đầu chơi.
     */
    public static PvB_GamePlay PvB;

    /**
     * Chế độ PvP trên 2 máy thông qua LAN
     */
    public static PvP_GamePlay PvP;

    /**
     * Độ lớn của một cell trong game.
     */
    public static final double unitLength = 40;

    /**
     * Hàm trả về giá trị đầu vào chia cho độ lớn của cell để ra chỉ số ô cần thiết.
     *
     * @param Input giá trị đầu vào
     * @return kết quả
     */
    public static int calculateCellIndex(double Input) {
        return (int) (Input / unitLength);
    }

    public enum role {
        PLAYER_1,
        PLAYER_2,
        NOT,
    }

    public static role playerRole;

    public static JSONArray commandList = new JSONArray();
    public static JSONArray temporaryCommandList = new JSONArray();

    public static String commandListString = new String();
}
