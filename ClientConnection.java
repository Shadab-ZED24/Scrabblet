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
//import sun.misc.Cleaner;

class ClientConnection implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private String host;
    private Server server;
    private static final String CRLF = "\r\n";
    private String name = null; //for humans.
    private String id;
    private boolean busy = false;
    
    public ClientConnection(Server srv, Socket s, int i) {
        try {
            server = srv;
            socket = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = s.getOutputStream();
            host = s.getInetAddress().getHostName();
            id = ""+ i;
            //tell the new who it is.
            write("id" + id + CRLF);
            new Thread(this).start();
        } catch (IOException e) {
            System.out.println("Failed Client Connection" + e);
        }
    }
    @Override
    public String toString() {
        return id + " " + host + " " +name;
    }
    public String getHost() {
        return host;
    }
    public String getId() {
        return id;
    }
    public boolean isBusy() {
        return busy;
    }
    public void setBusy (boolean  b) {
        busy = b;
    }
    public void close() {
        server.kill(this);
        
        try {
            socket.close(); //close in and out too.
        } catch(IOException e) { }
    }
    public final void write(String s) {
        byte buf[];
        buf = s.getBytes();
        try {
            out.write(buf, 0, buf.length);
        } catch (IOException e) {
            close();
        }
    }
    private String readline() {
        try {
            return in.readLine();
        } catch(IOException e) {
            return null;
        }
    }
    static private final int NAME = 1;
    static private final int QUIT = 2;
    static private final int TO = 3;
    static private final int DELETE = 4;
    
    static private final Hashtable key = new Hashtable();
    static private final String keystrings [] = {"","Name","quit","To","Delete"};
    static {
        for (int i = 0; i < keystrings.length; i++) 
            key.put(keystrings [i] , new Integer(i));
    }
    private int lookup(String s) {
        Integer i = (Integer) key.get(s);
        return i == null ? -1 : i.intValue();
    }
    @Override
    public void run() {
        String s;
        StringTokenizer st;
        while((s = readline()) != null) {
            st = new StringTokenizer(s);
            String keyword = st.nextToken();
            switch (lookup(keyword)) {
                default: 
                    System.out.println("bogus keyword" + keyword + "\r");
                    break;
                case NAME :
                    name = st.nextToken() + (st.hasMoreTokens() ? "" + st.nextToken(CRLF) : "");
                    System.out.println("[" + new Date() + "] " + this + "\r");
                    server.set(id,this);
                    break;
                case QUIT :
                    close();
                    return;
                case TO :
                    String dest = st.nextToken();
                    String body = st.nextToken(CRLF);
                    server.sendTo(dest,body);
                    break;
                case DELETE : 
                    busy = true;
                    server.delete(id);
                    break;
            }
        }
        close();
    }
}
