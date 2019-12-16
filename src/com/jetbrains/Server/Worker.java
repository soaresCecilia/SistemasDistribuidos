package com.jetbrains.Server;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.ServerHelper;

import java.io.*;
import java.net.Socket;

public class Worker implements Runnable {

    private Socket clSock;
    private int id;
    private ServerHelper serverhelper;


    public Worker(Socket clisock, ServerHelper helper) throws IOException{
        this.clSock =  clisock;
        this.serverhelper = helper;
    }


    public void responde(String s){
        String result;
        String[] comandos = s.split(" ");

        switch (comandos[0]){
            case "login":
                int id = Integer.parseInt(comandos[2]);
                try{
                serverhelper.login(comandos[1],id);}
                catch (IOException e ){}
                catch(CredenciaisInvalidasException e){}
                catch(ClientesSTUBException e){}
                break;

            case "logout":
                //vale a pena manter no repositorio quem está login?
                try{
                serverhelper.logout(comandos[0]);}
                catch (IOException e){}
                catch (ClientesSTUBException e){}

                break;

            case "registar":
                try{
                int pass=Integer.parseInt(comandos[2]);
                serverhelper.registarUtilizador(comandos[1],pass);}
                catch (ClientesSTUBException e){}
                catch (UtilizadorJaExisteException e){}

                break;

            case "upload":
                try{
                int ano=Integer.parseInt(comandos[3]);
                serverhelper.upload(comandos[1],comandos[2],ano,comandos[4]);}
                catch (IOException e){}
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}

                break;
            case "download":
                try{
                    int nrm= Integer.parseInt(comandos[1]);
                    serverhelper.download(nrm);
                }
                catch (MusicaInexistenteException e){}
                catch (IOException e){}
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


    public void run(){
        try {
            if (clSock == null) System.out.println("clSock null");

            //escreve no canal
            PrintWriter out = new PrintWriter(clSock.getOutputStream());
            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));


            //passa o que li do canal para uma string
            String inComing=in.readLine();

            //System.out.println(inComing);
            while(inComing!=null){

                if(inComing.equals("teste"))
                //responde(inComing);

                out.println("teste");
                out.flush();

                inComing = in.readLine();

            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


