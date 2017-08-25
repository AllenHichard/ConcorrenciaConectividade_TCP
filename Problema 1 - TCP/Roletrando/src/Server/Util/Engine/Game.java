package Server.Util.Engine;

import Server.Util.Exceptions.DatabaseParsingException;
import Server.Util.Exceptions.PropertiesFileNotFoundException;
import Server.Util.WordManager.WordManager;
import Server.Util.WordManager.WordTuple;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Roda a Roda's game engine.
 *
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class Game {

    private String word; //Game's word.
    private StringBuilder wordBuilder; //User's version of the game's word.
    private String tip; //Word's tip
    private final WordManager wordManager;
    private final int[] rouletteValues;
    private int rouletteValue; //Current roulette values
    private int roundNumber; //Current round's number.
    private int score; //Current score
    private boolean isRouletteAvailable;
    private final int numberOfRounds;
    private int accumulatedScore;

    public Game() throws PropertiesFileNotFoundException, DatabaseParsingException {
        this.roundNumber = 1;
        this.numberOfRounds = 4;
        this.score = 0;
        this.isRouletteAvailable = true;
        this.wordManager = new WordManager();
        this.wordBuilder = new StringBuilder();
        this.rouletteValues = new int[]{100, 200, 300, 400, 500, 600, 700, 800, 900, 100, 0};
        this.refreshData();
    }

    /**
     * Check if it's time to spin the roulette. The roulette can used after try
     * to tryCharacter a word character.
     *
     * @return true if roulette is available, false otherwise.
     */
    public boolean isIsRouletteAvailable() {
        return this.isRouletteAvailable;
    }

    /**
     * Get the user's word version with hidden characters.
     *
     * @return the user's word
     */
    public String getUserWord() {
        return this.wordBuilder.toString();
    }

    /**
     * Get current word's tip. The 1st roundNumber doesn't allow tips.
     *
     * @return The current word tip.
     */
    public String getTip() {
        return this.roundNumber == 1 ? "" : this.tip;
    }

    /**
     * Refresh game's word/tip if a new roundNumber is reached.
     */
    private boolean refreshData() throws DatabaseParsingException {
        if (this.isRoundFinished()) {
            WordTuple next = this.wordManager.getTuple();
            this.word = next.getWord();
            this.tip = next.getTip();
            this.wordBuilder = new StringBuilder();

            for (int i = 0; i < this.word.length() - 1; i++) {
                this.wordBuilder.append('-');
            }
            return true;
        }
        return false;
    }

    /**
     * Get and replace ch ocurrences in the current word. After the changes, the
     * score is updated.
     *
     * @param ch char to be revealed
     * @return amount of revealed chars
     * @throws Server.Util.Exceptions.DatabaseParsingException
     */
    public int tryCharacter(char ch) throws DatabaseParsingException{
        if (this.isRoundFinished()) {
            this.refreshData();
        }

        this.isRouletteAvailable = true;

        int occurrences = 0;
        int index = 0;

        while ((index = this.word.indexOf("" + ch, index)) != -1) {
            this.wordBuilder.setCharAt(index, ch);
            index++;
            occurrences++;
        }

        this.score += occurrences * this.rouletteValue;

        return occurrences;
    }

    /**
     * Random roulette value or the current roullete value in case the roulette
     * isn't available.
     *
     * @return a roulette value
     */
    public int roulette() {
        if (this.isRouletteAvailable) {
            Random random = new Random();
            this.rouletteValue = this.rouletteValues[random.nextInt(this.rouletteValues.length)];

            if (this.rouletteValue == 0) {
                this.score = 0;
            }
        }
        return this.rouletteValue;
    }

    /**
     * Check if the turn is over and prepare for another roundNumber.
     *
     * @return true if the roundNumber is finished, false otherwise.
     */
    public boolean isRoundFinished() {
        return this.wordBuilder.indexOf("-") == -1;

    }

    /**
     * Go to the next roundNumber, if it exists. If the game has more rounds, the
 roundNumber number is updated and the word is refreshed. Also, the accumulated 
 score is updated and the roundNumber's score is set to 0.
     *
     * @return true if a next roundNumber is reached, false otherwise.
     * @throws Server.Util.Exceptions.DatabaseParsingException
     */
    public boolean nextRound() throws DatabaseParsingException {
        if (this.isRoundFinished() && this.hasNextRound()) {
            this.refreshData();
            this.roundNumber++;
            this.accumulatedScore += this.score;
            this.score = 0;
            return true;
        }
        return false;
    }

    /**
     * Check if there's a next roundNumber.
     * 
     * @return false if the current roundNumber is the last one, true otherwise.
     */
    public boolean hasNextRound() {
        return this.roundNumber <= this.numberOfRounds;
    }

    /**
     *
     * @return user's current score.
     */
    public int getRoundScore() {
        return score;
    }

    /**
     *
     * @return user's game accumulatedScore
     */
    public int getAccumulatedScore() {
        return this.accumulatedScore;
    }

    /**
     *
     * @return current roundNumber number.
     */
    public int getRoundNumber() {
        return this.roundNumber;
    }

    /**
     * Single user game implementation.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);

        while (game.hasNextRound()) {
            System.out.println("Round " + game.getRoundNumber());
            while (!game.isRoundFinished()) {
                System.out.println(game.getTip());
                System.out.println(game.getUserWord());
                int roulette = game.roulette();
                System.out.println("Cada letra vale " + roulette);
                char ch = scanner.nextLine().toUpperCase().charAt(0);
                int found = game.tryCharacter(ch);
                System.out.println("Conseguiu " + found * roulette);
                System.out.println("Saldo " + game.getRoundScore());
            }
            game.nextRound();
        }
    }
}
