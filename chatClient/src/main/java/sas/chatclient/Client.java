/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sas.chatclient;

/**
 *
 * @author Francesco Lizzio
 */

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class Client  
{ 
    final static int ServerPort = 2108; 
  
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in); 
          
        InetAddress ip = InetAddress.getByName("localhost"); // imposto l'ip come localhost
          
        Socket s = new Socket(ip, ServerPort); // stabilisco la connessione
         
        DataInputStream dis = new DataInputStream(s.getInputStream()); // ottengo gli stream di input e output
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
  
        Thread sendMessage = new Thread(new Runnable() // creo il thread sendMessage 
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    String msg = scn.nextLine(); // leggo il messaggio da mandare
                      
                    try { 
                        dos.writeUTF(msg); // scrivo sullo stream di output
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
          
        Thread readMessage = new Thread(new Runnable() // creo il thread readMessage
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // legge il messaggio inviato a questo client
                        String msg = dis.readUTF(); 
                        System.out.println(msg); 
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
  
    } 
} 
