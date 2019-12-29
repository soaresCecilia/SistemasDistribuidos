package com.jetbrains.Server;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.ServerHelper;

import java.io.*;
import java.net.Socket;


/*
O worker só envia mensagens ao Cliente quando algo correu mal
 */
public class Worker implements Runnable {

    private Socket clSock;
    private ServerHelper serverhelper;
    private PrintWriter out;
    private String nome;

    public Worker(Socket clientesock, Repositorio repositorio) throws IOException{
        this.clSock = clientesock;
        this.serverhelper = new ServerHelper(clientesock, repositorio);
    }

    public void responde(String s){
        String result;
        String[] comandos = s.split(" ");


        System.out.println("o que tenho e, comandos[0] é " +comandos[0]);

        switch (comandos[0]){

            case "login":
                String nome = (comandos[1]);

                String pass = (comandos[2]);

                System.out.println("A minha pass " + pass);
                System.out.println("O meu nome é " + nome);

                try {
                    serverhelper.login(nome, pass);
                    this.nome = nome;
                }
                catch (IOException e ){}
                catch(CredenciaisInvalidasException e){
                }
                catch(ClientesSTUBException e){}
                break;

            case "logout":
                try {
                    serverhelper.logout(this.nome);
                }
                catch (IOException e){}
                catch (ClientesSTUBException e){}

                break;

            case "registar":
                try{
                    String password = (comandos[2]);
                    System.out.println("A minha pass " + password);
                    nome = comandos[1];
                    System.out.println("O meu nome é " + nome);

                    serverhelper.registarUtilizador(nome,password);}
                catch (ClientesSTUBException e){}
                catch (UtilizadorJaExisteException e) {
                    out.println("0");
                    out.flush();
                }
                catch (CredenciaisInvalidasException e){}

                break;

            case "upload":
                try{
                serverhelper.upload(comandos[1],comandos[2], comandos[3], comandos[4] ,comandos[5]);
                }
                catch (IOException e){}
                catch (ClientesSTUBException e){
                }
                catch (UtilizadorNaoAutenticadoException e){
                    out.println("0");
                    out.flush();
                }

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
                    out.flush();
                }
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}
                break;
            case "procura":
                try{
                    serverhelper.procuraMusica(comandos[1]);
                }
                catch (MusicaInexistenteException e){
                    out.println("2");  //saber se a não existir etiqueta é uma excepção!!!!!
                    out.flush();
                }
                catch (IOException e){}
                catch (UtilizadorNaoAutenticadoException e){}
                catch (ClientesSTUBException e) {}

                break;

            default:
                System.out.println("Opcao invalida"); //mandar para o cliente
        }
    }


    //O worker so fala com o stub quando dá erro
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


