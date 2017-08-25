package Server.Model;

import Server.Util.Engine.Ranking;
import Server.Util.Exceptions.RankingLoadException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 * Server entity, redirects a new client to one Client Host.
 * @author Allen Hichard
 * @author Daniel Andrade
 */
public class Server {
    private final int port;

    public Server(int port) throws RankingLoadException {
        this.port = port;
        this.loadRankingFiles();
    }
    
    private void loadRankingFiles() throws RankingLoadException{
        //Scanner reader = new Scanner(System.in);
        //System.out.print("Ranking path: ");
        //String rankingPath = reader.nextLine();
        //System.out.print("Top3 path: ");
        //String top3 = reader.nextLine();
        Ranking.instance().loadRankings("ranking.data", "top3.data");
    }
    /**
     * Run the server. Wait for a client and redirects it to the game thread.
     * @throws IOException 
     */
    public void run() throws IOException{
        ServerSocket server = new ServerSocket(this.port);
        System.out.println("Server online");
        
        while(true){
            System.out.println("Waiting...");
            Socket client = server.accept(); // Waiting for a client
            System.out.println("New client connected " + client.getInetAddress().getHostAddress());
            
            ClientHost clientHost = new ClientHost(client);
            Thread clientThread = new Thread(clientHost);
            clientThread.start();
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        new Server(12345).run();
    }
}
