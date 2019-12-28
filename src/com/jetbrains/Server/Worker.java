package com.jetbrains.Server;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.ServerHelper;

import java.io.*;
import java.net.Socket;

public class Worker implements Runnable {

    private Socket clSock;
    private ServerHelper serverhelper;
    private PrintWriter out;

    public Worker(Socket clientesock, Repositorio repositorio) throws IOException{
        this.clSock = clientesock;
        this.serverhelper = new ServerHelper(clientesock, repositorio);
    }

    public void responde(String s){
        String result;
        String[] comandos = s.split(" ");
        System.out.println("o que tenho e, comandos[0} "+comandos[0]);

        switch (comandos[0]){
            case "login":
                String pass = (comandos[2]);
                try {
                    serverhelper.login(comandos[1],pass);
                }
                catch (IOException e ){}
                catch(CredenciaisInvalidasException e){}
                catch(ClientesSTUBException e){}
                break;

            case "logout":
                //vale a pena manter no repositorio quem está login?
                try {
                    serverhelper.logout(comandos[0]);}
                catch (IOException e){}
                catch (ClientesSTUBException e){}

                break;

            case "registar":
                try{
                String password = (comandos[2]);
                serverhelper.registarUtilizador(comandos[1],password);}
                catch (ClientesSTUBException e){}
                catch (UtilizadorJaExisteException e){}

                break;

            case "upload":
                try{

                serverhelper.upload(comandos[1],comandos[2], comandos[3], comandos[4] ,comandos[5]);

                }
                catch (IOException e){}
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}

                break;
            case "download":
                try{
                    int nrm = Integer.parseInt(comandos[1]);
                    System.out.println("id da musica reecebida: "+nrm);
                    serverhelper.download(nrm);
                    System.out.println("ServerHelper funcionou!");
                }
                catch (MusicaInexistenteException e){
                    out.println("2");
                    out.flush();
                }
                catch (IOException e){
                    out.println("0");
                }
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}
                break;
            case "procurarMusica":
                try{
                    serverhelper.procuraMusica(comandos[1]);
                }
                catch (MusicaInexistenteException e){}
                catch (IOException e){}
                catch (UtilizadorNaoAutenticadoException e){}
                catch (ClientesSTUBException e) {}

                break;

            default:
                System.out.println ("Opcao invalida"); //mandar para o cliente
        }
    }


    //O wrker so fala com o stub quando dá erro
    public void run(){
        try {
            if (clSock == null) {
                System.out.println("clSock null");
                return;
            }

            //escreve no canal
            //PrintWriter out = new PrintWriter(clSock.getOutputStream());
            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));

            //passa o que li do canal para uma string
            String inComing = in.readLine();

            //System.out.println(inComing);
            while(inComing != null) {
                responde(inComing);

               // out.println();
              //  out.flush();

                inComing = in.readLine();
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


