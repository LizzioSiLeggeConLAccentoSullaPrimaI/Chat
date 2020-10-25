/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sas.chatserver;

/**
 *
 * @author Francesco Lizzio
 */

import java.io.*; 
import java.util.*; 
import java.net.*;

public class Server  
{ 
    static Vector<ClientHandler> v = new Vector<>(); // vettore per immagazzinare i client attivi
      
    static int i = 0; // contatore per i client 
  
    public static void main(String[] args) throws IOException  
    {  
        ServerSocket ss = new ServerSocket(2108); // scelgo la porta 2108 per la comunicazione dei client
          
        Socket s; 
          
        while (true) // loop infinito per le richieste dei client 
        { 
            s = ss.accept(); // accetta la richiesta 
  
            System.out.println("Nuova richiesta ricevuta : " + s); 
               
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creando un nuovo gestore per questo client..."); 
  
            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); // crea un nuovo oggetto per la gestione di questa richiesta
  
            Thread t = new Thread(mtch); // crea un nuovo thread con questo oggetto
              
            System.out.println("Aggiungo questo client alla lista dei client attivi"); 
  
            v.add(mtch); // aggiunge questo client alla lista di client attivi
  
            t.start(); // avvia il thread
  
            i++; // incrementa i per ogni nuovo client
  
        } 
    } 
} 
  
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String nome; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean connesso; 
      
    // costruttore
    public ClientHandler(Socket s, String nome, DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.nome = nome; 
        this.s = s; 
        this.connesso=true; 
    } 
  
    @Override
    public void run() { 
  
        String ricevuto; 
        while (true)  
        { 
            try
            { 
                ricevuto = dis.readUTF(); // riceve la stringa
                  
                System.out.println(ricevuto); 
                  
                if(ricevuto.equals("logout")){ 
                    this.connesso=false; 
                    this.s.close(); 
                    break; 
                } 
                  
                StringTokenizer st = new StringTokenizer(ricevuto, "-"); // divide la stringa in due parti, messaggio e ricevente, separati dal trattino
                String MsgToSend = st.nextToken(); 
                String recipient = st.nextToken(); 
  
                for (ClientHandler mc : Server.v) // ricerca nella lista dei client
                { 
                    if (mc.nome.equals(recipient) && mc.connesso==true) // se il ricevente è trovato, scrive sul suo output stream
                    { 
                        mc.dos.writeUTF(this.nome + " : " + MsgToSend); 
                        break; 
                    } 
                } 
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            this.dis.close(); // chiude gli stream
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 