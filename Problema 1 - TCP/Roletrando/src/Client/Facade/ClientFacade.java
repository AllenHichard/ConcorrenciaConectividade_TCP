package Client.Facade;

import Client.Model.Client;
import Server.Util.Engine.RankingItem;
import java.io.IOException;
import java.util.Iterator;

/**
 * Client's abstraction layer.
 *
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class ClientFacade {

    private final Client client;

    public ClientFacade(String ip, int port) throws IOException {
        this.client = new Client(ip, port);
    }

    /**
     * Set the user's name.
     *
     * @param username
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setUsername(String username) throws IOException, ClassNotFoundException {
        this.client.setUsername(username);
    }

    public String getUsername() {
        return this.client.getUsername();
    }

    /**
     * Get the user's version of the game's word.
     *
     * @return the word
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String getWord() throws IOException, ClassNotFoundException {
        return this.client.getWord();
    }

    /**
     * Get the tip of the current word.
     *
     * @return the tip if round >1, empty String otherwise.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String getTip() throws IOException, ClassNotFoundException {
        return this.client.getTip();
    }

    /**
     * Get the current roulette value.
     *
     * @return the current roulette value.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getRouletteValue() throws IOException, ClassNotFoundException {
        return this.client.getRouletteValue();
    }

    /**
     * Test if the current word has the ch character.
     *
     * @param ch character to be tested
     * @return the amount of ch occurrences in the current word.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int tryCharacter(char ch) throws IOException, ClassNotFoundException {
        return this.client.tryCharacter(ch);
    }

    /**
     * Check if the game has more rounds
     *
     * @return true if there is more rounds, false if the current round is the
     * last one.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean hasNextRound() throws IOException, ClassNotFoundException {
        return this.client.hasNextRound();
    }

    /**
     * Go to the next round if the current round is done.
     *
     * @return true if the round changed, false otherwise.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean nextRound() throws IOException, ClassNotFoundException {
        return this.client.nextRound();
    }

    /**
     * Check if the current round is finished.
     *
     * @return True if the round is ended, false if the round continues.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean isRoundFinished() throws IOException, ClassNotFoundException {
        return this.client.isRoundFinished();
    }

    /**
     * Get the user's current round score.
     *
     * @return the current round user's score.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getScore() throws IOException, ClassNotFoundException {
        return this.client.getScore();
    }

    /**
     * Get the current round number.
     *
     * @return the current round number.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getRoundNumber() throws IOException, ClassNotFoundException {
        return this.client.getRoundNumber();
    }

    /**
     * Get the user's highscore in the game.
     *
     * @return the user's highscore
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getHighscore() throws IOException, ClassNotFoundException {
        return this.client.getHighscore();
    }

    /**
     * Get user's the accumulated score in the current game.
     *
     * @return the accumulated score
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int getAccumulatedScore() throws IOException, ClassNotFoundException {
        return this.client.getAccumulatedScore();
    }

    /**
     * Disconnects from the server.
     * @throws IOException 
     */
    public void disconnect() throws IOException{
        this.client.disconnect();
    }
    
    /**
     * Get the top 3 best players.
     *
     * @return the iterator of the top3 list.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Iterator<RankingItem> getTop3() throws IOException, ClassNotFoundException {
        return this.client.getTop3();
    }

}
