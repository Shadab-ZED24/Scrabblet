/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblet;

/**
 *
 * @author Shadab Khan Zed
 */
import java.io.*;
import java.net.*;
import java.util.*;

class ServerConnection implements Runnable{
    private static final int port = 6564;
    private static final String CRLF = "\r\n";
    private final BufferedReader in;
    private final PrintWriter out;
    private String id, toid = null;
    private final Scrabblet scrabblet;
    
    public ServerConnection(Scrabblet sc , String site) throws IOException {
        scrabblet = sc;
        Socket server = new Socket(site, port);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
    }
    private String readline() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    void setName(String s)
    {
        out.println("name" + s);        
    }
    void delete () {
        out.println("Delete" + id);
    }
    void setTo(String to) {
        toid =  to;
    }
    void send(String s) {
        out.println("to" + toid + " " + s);
    }
    void challenge(String destid) {
        setTo(destid);
        send("Challenge" + id);
    }
    void accept (String destid, int seed) {
        setTo(destid);
        send("Accept" + id + " " + seed);
    }
    void chat (String s) {
        send("Chat" + id + " " + s);
    }
    void move(String letter, int x, int y) {
        send("Move" + letter + " " + x + " " + y);
    }
    void turn(String words, int score) {
        send("Turn" + score + " " + words);
    }
    void quit() {
        send("Quit" + id); //tell other player.
        out.println("Quit"); //unhook.
    }
    //reading from server.....
    private Thread t;
    void start() {
        t = new Thread(this);
        t.start();
    }
    private static final int ID = 1;
    private static final int ADD = 2;
    private static final int DELETE = 3;
    private static final int MOVE = 4;
    private static final int CHAT = 5;
    private static final int QUIT = 6;
    private static final int TURN = 7;
    private static final int ACCEPT = 8;
    private static final int CHALLENGE = 9;
    private static final Hashtable keys = new Hashtable();
    private static final String keystrings [] = {"", "id", "add", "delete", "move", "chat", "quit", "turn", "accept", "challenge"};
    static {
        for(int i = 0; i < keystrings.length; i++) 
            keys.put(keystrings[i], new Integer(i));
    }
    private int lookup (String s){
        Integer i = (Integer) keys.get(s);
        return i == null ? -1 : i.intValue();
    }
    
    @Override
    public void run() {
        String s;
        StringTokenizer st;
        while ((s = readline()) != null) {
            st = new StringTokenizer(s);
            String keyword = st.nextToken();
            switch(lookup(keyword)) {
                default: 
                    System.out.println("begous Keyword" + keyword + "\r");
                    break;
                case ID: 
                    id = st.nextToken();
                    break;
                case ADD : {
                    String id = st.nextToken();
                    String name = st.nextToken();
                    String hostname = st.nextToken(CRLF);
                    scrabblet.add(id, hostname, name);
                }
                    break;
                case DELETE : 
                    scrabblet.delete(st.nextToken());
                    break;
                case MOVE : {
                    String ch  = st.nextToken();
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());
                    scrabblet.move(ch, x, y);
                } break;
                case CHAT : {
                    String from = st.nextToken();
                    scrabblet.chat(from,st.nextToken(CRLF));
                } break;
                case QUIT : {
                    String from = st.nextToken();
                    scrabblet.quit(from);
                } break;
                case TURN : {
                    int score = Integer.parseInt(st.nextToken());
                    scrabblet.turn(score, st.nextToken(CRLF));
                } break;
                case ACCEPT : {
                    String from = st.nextToken();
                    int seed = Integer.parseInt(st.nextToken());
                    scrabblet.accept(from,seed);
                }  break;
                case CHALLENGE : {
                    String from  = st.nextToken();
                    scrabblet.challenge(from);
                } break;
            }
        } 
    }   
}
