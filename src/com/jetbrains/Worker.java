package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Worker implements Runnable {

    private Socket clSock;
    private int id;
    private Repositorio repositorio;

    public Worker(Socket clsock, Repositorio repositorio){
        this.clSock =  clsock;
        this.repositorio= repositorio;
    }

    public void run(){
        try {
            if (clSock == null) System.out.println("clSock null");

            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));
            //escreve no canal
            PrintWriter out = new PrintWriter(clSock.getOutputStream());

            //passa o que li do canal para uma string
            String inComing=in.readLine();

             String[] comandos = inComing.split(" ");



            while(inComing!=null){



                switch (comandos[0]){


                    case "nome":

                        out.println("ServidorWorker encontrou ???");
                        out.flush();
                        break;

/*
                    case "interprete":

                            out.println("ServidorWorker encontrou ??? asdad");
                            out.flush();
                            break;
*/
                    default:
                        out.println("Opcao invalida");
                        out.flush();
                }

                inComing = in.readLine();
                comandos = inComing.split(" ");
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


