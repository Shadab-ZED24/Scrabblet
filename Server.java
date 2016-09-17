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
//import com.sun.javaws.Main;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{
    private final int port = 6564;
    private final Hashtable idcon = new Hashtable();
    private int id = 0;
    static final String CRLF = "\r\n";
    
    synchronized void addConnection(Socket s) {
        ClientConnection cn = new ClientConnection (this, s, id);
        /*We Will wait for client connection to do a clean
        handshake setting up its "name" before calling
        set() below which makes this connection is live.
        */
        id++;
    }
    synchronized void set(String the_id, ClientConnection con) {
        idcon.remove(the_id); //make sure u r not in the twice.
        con.setBusy(false);
        //tell this one about other client.
        Enumeration e = idcon.keys();
        while(e.hasMoreElements()) {
            String ids = (String)e.nextElement();
            ClientConnection other = (ClientConnection) idcon.get(ids);
            if(!other.isBusy()) 
                con.write("add" + other + CRLF);
        }
        idcon.put(the_id, con);
        broadcast(the_id, "add" + con);
    }
    
    synchronized void sendTo(String dest, String body) {
        ClientConnection con = (ClientConnection) idcon.get(dest);
        if(con != null) {
            con.write(body + CRLF);
        }
    }
    
    synchronized void broadcast(String exclude, String body) {
        Enumeration e = idcon.keys();
        while(e.hasMoreElements()) {
            String ids = (String)e.nextElement();
            if(!exclude.equals(ids)) {
                ClientConnection con = (ClientConnection) idcon.get(ids);
                con.write(body + CRLF);
            }
        }
    }
    
    synchronized void delete(String the_id) {
        broadcast(the_id, "Delete" + the_id);
    }
    
    synchronized void kill(ClientConnection c) {
        if(idcon.remove(c.getId()) == c) {
            delete(c.getId());
        }
    }
    
    @Override
    public void run() {
        try {
            ServerSocket acceptSocket = new ServerSocket(port);
            System.out.println("Server listening on port" + port);
            while(true) {
                Socket s = acceptSocket.accept();
                addConnection(s);
            }
        } catch (IOException e) {
            System.out.println("Accept loop exception :" + e); 
        }
    }
    
    public static void main(String[] args) {
        new Thread(new Server()).start();
        try {
            Thread.currentThread().join();
        } catch(InterruptedException e) { }
    }
    
}
