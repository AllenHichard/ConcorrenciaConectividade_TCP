package Server.Util.Protocol;

/**
 * Default messages for server-client communication.
 *
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class Protocol {

    public static final char SEND_USER_NAME = 1;
    public static final char GET_USER_HIGH_SCORE = 2;
    public static final int GET_WORD = 3;
    public static final int GET_TIP = 4;
    public static final int IS_ROULETTE_AVAILABLE = 5;
    public static final int IS_ROUND_FINISHED = 6;
    public static final int GET_ROULETTE_VALUE = 7;
    public static final int TRY_CHARACTER = 8;
    public static final int ACCUMULATED_SCORE = 9;
    public static final int GET_CURRENT_SCORE = 10;
    public static final int GET_ROUND_NUMBER = 11;
    public static final int NEXT_ROUND = 12;
    public static final int HAS_NEXT_ROUND = 13;
    public static final int RANKING_TOP3 = 14;
     public static final int GAME_OVER = 15;
    public static final String SEPARATOR = "-";
}

