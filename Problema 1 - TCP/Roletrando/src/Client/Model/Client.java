package Client.Model;

import Server.Util.Engine.RankingItem;
import Client.Util.Protocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Client-side implementation of "Roda-a-Roda" game. This class is responsable
 * to give an abstraction layer between the client's choice and the corresponding 
 * request to the server. A request to the serve is represented by a message
 * established on the Protocol class.
 * 
 * @see Protocol
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class Client {

    private final ObjectInputStream input; //Client's input stream
    private final ObjectOutputStream output; //Client's output stream
    private String username; //Client's username

    public Client(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
    }

    public String getUsername() {
        return username;
    }
    
    /**
     * Set the user's name.
     * 
     * @param username
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void setUsername(String username) throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.SEND_USER_NAME);
        this.sendMessage(username);
    }
    
    /**
     * Get the user's version of the game's word.
     * 
     * @return the word
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String getWord() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_WORD);
        return this.readMessage().toString();
    }
    
    /**
     * Get the tip of the current word.
     * @return the tip if round >1, empty String otherwise.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String getTip() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_TIP);
        return this.readMessage().toString();
    }

    /**
     * Get the current roulette value.
     * 
     * @return the current roulette value.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int getRouletteValue() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_ROULETTE_VALUE);
        return Integer.parseInt(this.readMessage().toString());
    }
    
    /**
     * Test if the current word has the ch character.
     * @param ch character to be tested
     * @return the amount of ch occurrences in the current word.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int tryCharacter(char ch) throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.TRY_CHARACTER);
        this.sendMessage(ch);
        return Integer.parseInt(this.readMessage().toString());
    }

    /**
     * Check if the current round is finished.
     * @return True if the round is ended, false if the round continues.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public boolean isRoundFinished() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.IS_ROUND_FINISHED);
        return Boolean.parseBoolean(this.readMessage().toString());
    }
    
    /**
     * Get the user's current round score.
     * @return the current round user's score.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int getScore() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_CURRENT_SCORE);
        return Integer.parseInt(this.readMessage().toString());
    }
    
    /**
     * Get the user's highscore in the game.
     * 
     * @return the user's highscore
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int getHighscore() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_USER_HIGH_SCORE);
        return Integer.parseInt(this.readMessage().toString());
    }

    /**
     * Get the current round number.
     * @return the current round number.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int getRoundNumber() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.GET_ROUND_NUMBER);
        return Integer.parseInt(this.readMessage().toString());
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
        this.sendMessage(Protocol.HAS_NEXT_ROUND);
        return Boolean.parseBoolean(this.readMessage().toString());
    }

    /**
     * Go to the next round if the current round is done.
     * 
     * @return true if the round changed, false otherwise.
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public boolean nextRound() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.NEXT_ROUND);
        return Boolean.parseBoolean(this.readMessage().toString());
    }
    
    /**
     * Get user's the accumulated score in the current game.
     * @return the accumulated score
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public int getAccumulatedScore() throws IOException, ClassNotFoundException{
        this.sendMessage(Protocol.ACCUMULATED_SCORE);
        return Integer.parseInt(this.readMessage().toString());
    }

    /**
     * Get the top 3 best players.
     * @return the iterator of the top3 list.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public Iterator<RankingItem> getTop3() throws IOException, ClassNotFoundException {
        this.sendMessage(Protocol.RANKING_TOP3);
        List<RankingItem> top3 = new ArrayList<>();
        StringTokenizer rankingInfo = new StringTokenizer(this.readMessage().toString(), Protocol.SEPARATOR);
        while(rankingInfo.hasMoreTokens()){
            top3.add(new RankingItem(rankingInfo.nextToken(), Integer.parseInt(rankingInfo.nextToken())));
        }
        return top3.iterator();
    }
    
    /**
     * Reads a message from de server.
     * 
     * @return the read message.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public Object readMessage() throws IOException, ClassNotFoundException {
        return this.input.readObject();
    }

    /**
     * Send a message to the server.
     * 
     * @param message message to be sent.
     * @throws IOException 
     */
    public void sendMessage(Object message) throws IOException {
        this.output.reset();
        this.output.writeObject(message);
        this.output.flush();
    }
    
    /**
     * Disconnects from the server.
     * @throws IOException 
     */
    public void disconnect() throws IOException{
        this.sendMessage(Protocol.GAME_OVER);
        this.input.close();
        this.output.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client("localhost", 12345);
       Iterator<RankingItem> top3 = client.getTop3();
       int i=1;
        System.out.println("RANKING:");
        while (top3.hasNext()) {
            System.out.println((i++)+"-"+top3.next());
        }
        Scanner read = new Scanner(System.in);
        System.out.print("Nome: ");
        client.setUsername(read.nextLine());

        while (client.hasNextRound()) {
            client.disconnect();
            System.out.println(client.getRoundNumber() + "ª rodada");
            while (!client.isRoundFinished()) {
                System.out.println("Palavra: " + client.getWord() + " Dica: " + client.getTip());
                int roulette = client.getRouletteValue();
                System.out.println("Cada letra vale: " + roulette);
                System.out.print("Letra: ");
                int revealCharacters = client.tryCharacter(read.nextLine().toUpperCase().charAt(0));
                System.out.print("Acertou " + revealCharacters + " letras ");
                System.out.println("Ganhou " + (revealCharacters * roulette));
            }
            client.nextRound();
        }
    }
}
