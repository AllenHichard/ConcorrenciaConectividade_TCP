package Server.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Server.Util.Engine.Game;
import Server.Util.Engine.Ranking;
import Server.Util.Engine.RankingItem;
import Server.Util.Exceptions.DatabaseParsingException;
import Server.Util.Exceptions.PropertiesFileNotFoundException;
import Server.Util.Exceptions.RankingLoadException;
import Server.Util.Protocol.Protocol;

/**
 * Network gaming abstraction class. Use the game engine as base to the
 * server-side part of 'Roda-a-Roda' game.
 *
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class ClientHost implements Runnable {

    private final Game game;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Ranking generalRanking;
    private String username;

    /**
     *
     * @param client to be hosted
     * @throws Server.Util.Exceptions.PropertiesFileNotFoundException
     * @throws Server.Util.Exceptions.DatabaseParsingException
     * @throws Server.Util.Exceptions.RankingLoadException
     * @throws IOException
     */
    public ClientHost(Socket client) throws PropertiesFileNotFoundException, DatabaseParsingException, RankingLoadException, IOException  {
        this.game = new Game();
        this.generalRanking = Ranking.instance();
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.input = new ObjectInputStream(client.getInputStream());
    }

    /**
     * Send a message to the client connected in the socket.
     *
     * @param data objecto to be sent
     * @throws IOException
     */
    private void sendMessage(Object data) throws IOException {
        this.output.reset();
        this.output.writeObject(data);
        this.output.flush();
    }

    /**
     * Read a message from the client connected in the socket.
     *
     * @return the read message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object readMessage() throws IOException, ClassNotFoundException {
        return this.input.readObject();
    }

    /**
     * Routine to response user's requests. After receive a request, answer it with
     * the corresponding game operation/result. Info:response is represented by 
     * an integer given in the Protocol class.
     * 
     * @see Protocol
     */
    @Override
    public void run() {
        try {
            int option;
            while (true) {
                option = Integer.parseInt(this.readMessage().toString());
                switch (option) {
                    case Protocol.SEND_USER_NAME:
                        this.username = this.readMessage().toString();
                        break;
                    case Protocol.GET_USER_HIGH_SCORE:
                        this.sendMessage(this.generalRanking.getUserHighscore(this.username));
                        break;
                    case Protocol.NEXT_ROUND:
                        boolean nextRound = this.game.nextRound();
                        if (nextRound) {
                            this.generalRanking.refreshUserHighscore(this.username, this.game.getRoundScore());
                        }
                        this.sendMessage(nextRound);
                        break;
                    case Protocol.GET_WORD:
                        this.sendMessage(this.game.getUserWord());
                        break;
                    case Protocol.GET_TIP:
                        this.sendMessage(this.game.getTip());
                        break;
                    case Protocol.IS_ROULETTE_AVAILABLE:
                        this.sendMessage(this.game.isIsRouletteAvailable());
                        break;
                    case Protocol.GET_ROULETTE_VALUE:
                        this.sendMessage(this.game.roulette());
                        break;
                    case Protocol.TRY_CHARACTER:
                        Character ch = this.readMessage().toString().charAt(0);
                        this.sendMessage(this.game.tryCharacter(ch));
                        break;
                    case Protocol.GET_ROUND_NUMBER:
                        this.sendMessage(this.game.getRoundNumber());
                        break;
                    case Protocol.IS_ROUND_FINISHED:
                        this.sendMessage(this.game.isRoundFinished());
                        break;
                    case Protocol.HAS_NEXT_ROUND:
                        this.sendMessage(this.game.hasNextRound());
                        break;
                    case Protocol.GET_CURRENT_SCORE:
                        this.sendMessage(this.game.getRoundScore());
                        break;
                    case Protocol.ACCUMULATED_SCORE:
                        this.sendMessage(this.game.getAccumulatedScore());
                        break;
                    case Protocol.RANKING_TOP3:
                        RankingItem[] top3 = this.generalRanking.getTop3();
                        StringBuilder rankingInfo = new StringBuilder();
                        for (RankingItem rankingItem : top3) {
                            rankingInfo.append(rankingItem.getUsername());
                            rankingInfo.append(Protocol.SEPARATOR);
                            rankingInfo.append(rankingItem.getScore());
                            rankingInfo.append(Protocol.SEPARATOR);
                        }
                        this.sendMessage(rankingInfo.toString());
                        break;
                    case Protocol.GAME_OVER:
                        this.input.close();
                        this.output.close();
                        System.out.println(this.username + " is now offline");
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException | NumberFormatException ex) {
            System.err.println("ERROR: " + ex);
            System.err.println("MESSAGE " + ex.getLocalizedMessage());
        }
    }

}
